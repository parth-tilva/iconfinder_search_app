package com.example.assignmentkakcho.ui.gallery

import android.annotation.SuppressLint
import android.app.Application
import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.assignmentkakcho.IconfinderApplication
import com.example.assignmentkakcho.data.repository.IconfinderRepository
import com.example.assignmentkakcho.data.model.Icon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val repository: IconfinderRepository,
    private val app: Application,
) : AndroidViewModel(app) {

    val TAG = "galleyViewmodel"

    private val currentQuery = MutableLiveData("Social Media")

    private val _sharedMsg = MutableSharedFlow<String>()
    val sharedMsg = _sharedMsg.asSharedFlow()

    val photos = currentQuery.switchMap { queryString ->
        repository.getSearchResults(queryString).cachedIn(viewModelScope)
    }

    fun getIconsInIconSet(iconSetId: Int ):LiveData<PagingData<Icon>>{
        return repository.getIconsResults(iconSetId)
    }

    lateinit var currentIcon:Icon
    var position =0

    fun searchPhotos(query: String) {
        currentQuery.value = query
    }

    @SuppressLint("Range")
    fun download(icon: Icon, position: Int) {
        Log.d(TAG,"before return cliced postion: $position")

        viewModelScope.launch {
                var downloadID: Long = 0
                _sharedMsg.emit("Downloading...")
                Log.d(TAG,"download cliced postion: $position")

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
                    Log.d(TAG,"msg: $e")
                    _sharedMsg.emit(e.toString())
                    Log.d(TAG,"msg: $e")
                }

                val query = DownloadManager.Query().setFilterById(downloadID)
                var lastMsg = ""
                var downloading = true
                while (downloading) {
                    val cursor: Cursor = downloadManager.query(query)
                    cursor.moveToFirst()
                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                        downloading = false
                    }

                    val msg = when (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                        DownloadManager.STATUS_SUCCESSFUL -> "Download successful"
                        DownloadManager.STATUS_FAILED -> "Download failed"
                        else -> ""
                    }
                    delay(500L)
                    Log.e("DownloadManager", " Status is :$msg")
                    if (msg != lastMsg) {
                        Log.d(TAG,"msg: $msg")
                        _sharedMsg.emit(msg)
                        lastMsg = msg
                    }
                    cursor.close()
                }

            }
        }




}