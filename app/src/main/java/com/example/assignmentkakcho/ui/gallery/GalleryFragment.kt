package com.example.assignmentkakcho.ui.gallery

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context.DOWNLOAD_SERVICE
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.example.assignmentkakcho.MainActivity
import com.example.assignmentkakcho.R
import com.example.assignmentkakcho.data.model.Icon
import com.example.assignmentkakcho.databinding.FragmentGalleryBinding
import com.example.assignmentkakcho.ui.download.DownloadAdapter
import com.example.assignmentkakcho.ui.download.DownloadBottomSheet
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery), IconAdapter.OnItemClickListener
    {

    val TAG = "GalleryFragment"
    private val viewModel: GalleryViewModel by activityViewModels()
        private val args: GalleryFragmentArgs by navArgs()

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    private var writePermissionGranted = false
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

//        override fun onResume() {
//            super.onResume()
//            (activity as MainActivity?)?.let{
//                it.supportActionBar?.setDisplayShowHomeEnabled(false)
//            }
//        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val iconSetId = args.iconSetId
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                writePermissionGranted = it
            }

        super.onViewCreated(view, savedInstanceState)

        updateRequestPermission()

        _binding = FragmentGalleryBinding.bind(view)

        val adapter = IconAdapter(this)

        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = IconLoadStateAdapter { adapter.retry() },
                footer = IconLoadStateAdapter { adapter.retry() }
            )
            btnRetry.setOnClickListener { adapter.retry() }
        }

        viewModel.getIconsInIconSet(iconSetId).observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                btnRetry.isVisible = loadState.source.refresh is LoadState.Error
                txtError.isVisible = loadState.source.refresh is LoadState.Error

                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1
                ) {
                    recyclerView.isVisible = false
                    txtEmpty.isVisible = true
                } else {
                    txtEmpty.isVisible = false
                }
            }
        }

        setHasOptionsMenu(true)
        downloadUpdates()

    }

    private fun downloadUpdates(){
        lifecycleScope.launchWhenStarted {
            viewModel.sharedMsg.collectLatest {
                Snackbar.make(
                    binding.root,
                    it,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun updateRequestPermission() {
        val hasWritePermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        val minSDK29 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
        writePermissionGranted = hasWritePermission || minSDK29
    }


    private fun askPermission(){
        if (!writePermissionGranted) {
            permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }


    private fun downloadImage(icon: Icon) {
        val bottomSheet = DownloadBottomSheet()
        bottomSheet.show(parentFragmentManager,"xyz")
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.serach_menu, menu)
        val searchItem = menu.findItem(R.id.action_serach)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                if (query != null) {
                    binding.recyclerView.scrollToPosition(0)
                    viewModel.searchPhotos(query)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    override fun onItemClick(icon: Icon) {
//        val action = GalleryFragmentDirections.actionGalleryFragmentToCategoryFragment()
//        findNavController().navigate(action)
    }

    override fun onDownloadClicked(icon: Icon) {
        viewModel.currentIcon = icon

        if (writePermissionGranted) {
            downloadImage(icon)
        } else {
            askPermission()
            if(writePermissionGranted){
                onDownloadClicked(icon)
            }else{
                Toast.makeText(
                    requireContext(),
                    "Can't download photo allow permission from settings",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}