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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.example.assignmentkakcho.R
import com.example.assignmentkakcho.data.Icon
import com.example.assignmentkakcho.databinding.FragmentGalleryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File


@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery),
    IconAdapter.OnItemClickListener {

    private val viewModel by viewModels<GalleryViewModel>()
    var downloadID: Long = 0

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    private var writePermissionGranted = false
    private lateinit var permissionLauncher: ActivityResultLauncher<String>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                writePermissionGranted = it
            }
        updateRequestPermission()

        super.onViewCreated(view, savedInstanceState)

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

        viewModel.photos.observe(viewLifecycleOwner) {
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
    }


    private fun updateRequestPermission() {
        val hasWritePermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        val minSDK29 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
        writePermissionGranted = hasWritePermission || minSDK29
        if (!writePermissionGranted) {
            permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }


    @SuppressLint("Range")
    private fun downloadImage(icon: Icon) {
        val downloadManager = context?.getSystemService(DOWNLOAD_SERVICE) as DownloadManager

        val index = icon.raster_sizes.size - 1
        val urlString = icon.raster_sizes[index].formats[0].preview_url
        lateinit var request: DownloadManager.Request
        try {
            request = DownloadManager.Request(Uri.parse(urlString))
                .setDescription("Downloading...")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(false)
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_PICTURES,
                    File.separator + icon.icon_id.toString() + "_" + icon.tags[0] + ".png"
                )
            downloadManager.enqueue(request)
            Toast.makeText(context, "Downloading...", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            Toast.makeText(context, "error: $e", Toast.LENGTH_LONG).show()
        }

        downloadID = downloadManager.enqueue(request)
        val query = DownloadManager.Query().setFilterById(downloadID)

        lifecycleScope.launchWhenStarted {
            var lastMsg = ""
            var downloading = true
            while (downloading) {
                val cursor: Cursor = downloadManager.query(query)
                cursor.moveToFirst()
                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false
                }
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))

                val msg = when (status) {
                    DownloadManager.STATUS_SUCCESSFUL -> "Download successful"
                    DownloadManager.STATUS_FAILED -> "Download failed"
                    else -> ""
                }

                Log.e("DownloadManager", " Status is :$msg")
                if (msg != lastMsg) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
                    lastMsg = msg
                }
                cursor.close()
            }
        }
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

    override fun onDownloadClicked(icon: Icon) {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}