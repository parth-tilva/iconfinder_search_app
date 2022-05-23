package com.example.assignmentkakcho.ui.gallery

import android.app.Application
import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.assignmentkakcho.IconfinderApplication
import com.example.assignmentkakcho.data.Repository.IconfinderRepository
import com.example.assignmentkakcho.data.model.Icon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import java.text.FieldPosition
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val repository: IconfinderRepository,
    private val app: Application,
) : AndroidViewModel(app) {


    private val currentQuery = MutableLiveData("Social Media")

    val photos = currentQuery.switchMap { queryString ->
        repository.getSearchResults(queryString).cachedIn(viewModelScope)
    }

    lateinit var currentIcon:Icon

    fun searchPhotos(query: String) {
        currentQuery.value = query
    }

    fun download(icon: Icon,position: Int): Flow<String> {

        return flow {
            emit("Downloading...")
            val downloadManager = getApplication<IconfinderApplication>().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
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
                downloadID = downloadManager.enqueue(request)

            } catch (e: Exception) {
                Toast.makeText(context, "error: $e", Toast.LENGTH_LONG).show()
            }

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
                    delay(1000L)
                    Log.e("DownloadManager", " Status is :$msg")
                    if (msg != lastMsg) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(getApplication(), msg, Toast.LENGTH_SHORT).show()
                        }
                        lastMsg = msg
                    }
                    cursor.close()
                }
            }

        }
    }


}