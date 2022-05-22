package com.example.assignmentkakcho.ui.download

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmentkakcho.data.model.Icon
import com.example.assignmentkakcho.ui.download.DownloadAdapter.*
import com.example.assignmentkakcho.ui.gallery.GalleryViewModel

const val TAG = "DownloadAdapter"
class DownloadAdapter(val icon: Icon): RecyclerView.Adapter<DownloadViewHolder>() {
    private val list = icon.raster_sizes

    class  DownloadViewHolder(view: View): RecyclerView.ViewHolder(view){
        val tvQuality:TextView = view.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1,parent,false)
        Log.d(TAG,"oncreate view holder called")
        return DownloadViewHolder(view)
    }

    override fun onBindViewHolder(holder: DownloadViewHolder, position: Int) {
        val item = list[position]
        Log.d(TAG,"list: $list")
        holder.tvQuality.text = item.size.toString()
    }

    override fun getItemCount(): Int {
        Log.d(TAG,"listcount called : $list")

        return list.size

    }


}