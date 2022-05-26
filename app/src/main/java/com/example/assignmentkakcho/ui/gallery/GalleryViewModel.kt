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
import kotlinx.coroutines.cancel
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

    private val TAG = "galleyViewModel"

    private val currentQuery : MutableLiveData<String> = MutableLiveData()


    val searchResult = currentQuery.distinctUntilChanged().switchMap {

        repository.getSearchResults(it).cachedIn(viewModelScope)
    }



    private val _sharedMsg = MutableSharedFlow<String>()
    val sharedMsg = _sharedMsg.asSharedFlow()


    fun searchPhotos(query: String) {
        if(query!="")
        currentQuery.value = query
    }

    private val currentIconSet: MutableLiveData<Int> = MutableLiveData()


    val iconList = currentIconSet.distinctUntilChanged().switchMap{
        repository.getIconsResults(it).cachedIn(viewModelScope)
    }


    fun getIconsInIconSet(iconSetId: Int){
        currentIconSet.value = iconSetId
    }



    lateinit var currentIcon:Icon
    var position =0


    @SuppressLint("Range")
    fun download(icon: Icon, position: Int) {

        viewModelScope.launch {
                var downloadID: Long = 0
                _sharedMsg.emit("Downloading...")
                val downloadManager = getApplication<IconfinderApplication>().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                val urlString = icon.raster_sizes[position].formats[0].preview_url
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
                    _sharedMsg.emit(e.toString())
                }

                val query = DownloadManager.Query().setFilterById(downloadID)
                var lastMsg = ""
                var downloading = true
                while (downloading) {
                    val cursor: Cursor = downloadManager.query(query)
                    cursor.moveToFirst()
                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL or DownloadManager.STATUS_FAILED) {
                        downloading = false
                    }
                    val msg = when (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                        DownloadManager.STATUS_SUCCESSFUL -> "Download successful"
                        DownloadManager.STATUS_FAILED -> "Download failed"
                        else -> ""
                    }
                    delay(500L)
                    if (msg != lastMsg) {
                        _sharedMsg.emit(msg)
                        lastMsg = msg
                    }
                    cursor.close()
                }
            }
        }
}