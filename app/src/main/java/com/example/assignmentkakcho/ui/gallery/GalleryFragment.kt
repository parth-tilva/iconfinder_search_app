package com.example.assignmentkakcho.ui.gallery

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.example.assignmentkakcho.MainActivity
import com.example.assignmentkakcho.R
import com.example.assignmentkakcho.data.model.Icon
import com.example.assignmentkakcho.databinding.FragmentGalleryBinding
import com.example.assignmentkakcho.ui.download.DownloadBottomSheet
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

private const val TAG = "GalleryFragment"

@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery), IconAdapter.OnItemClickListener {
    private val viewModel: GalleryViewModel by activityViewModels()
    private val args: GalleryFragmentArgs by navArgs()
    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private var writePermissionGranted = false
    private lateinit var searchView: SearchView
    private lateinit var searchItem: MenuItem
    private lateinit var permissionLauncher: ActivityResultLauncher<String>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGalleryBinding.bind(view)

        val iconSetId = args.iconSetId
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                writePermissionGranted = it
            }
        updateRequestPermission()
        val adapter = IconAdapter(this)
        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.itemAnimator = null
            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = IconLoadStateAdapter { adapter.retry() },
                footer = IconLoadStateAdapter { adapter.retry() }
            )
            btnRetry.setOnClickListener { adapter.retry() }
        }

        if (iconSetId != -1) {
            viewModel.getIconsInIconSet(iconSetId)
            viewModel.iconList.observe(viewLifecycleOwner) {
                adapter.submitData(viewLifecycleOwner.lifecycle, it)
            }
        } else {
            setHasOptionsMenu(true)
            viewModel.searchResult.observe(viewLifecycleOwner) {
                adapter.submitData(viewLifecycleOwner.lifecycle, it)
            }
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
        downloadUpdates()
    }

    private fun downloadUpdates() {
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


    private fun askPermission() {
        if (!writePermissionGranted) {
            permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private fun downloadImage(icon: Icon) {
        val bottomSheet = DownloadBottomSheet()
        bottomSheet.show(parentFragmentManager, "xyz")
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.serach_menu, menu)
        searchItem = menu.findItem(R.id.action_serach)
        searchView = searchItem.actionView as SearchView
        if (viewModel.searchResult.value == null) {
            searchItem.expandActionView()
            searchView.setQuery("", false)
        }


        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                val action = GalleryFragmentDirections.actionGalleryFragmentToCategoryFragment()
                findNavController().navigate(action)
                return true
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                if (query != null && query != "") {
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
            if (writePermissionGranted) {
                downloadImage(icon)
            } else {
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