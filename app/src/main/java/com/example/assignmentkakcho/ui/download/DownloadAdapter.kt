package com.example.assignmentkakcho.ui.download

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmentkakcho.R
import com.example.assignmentkakcho.data.model.Icon
import com.example.assignmentkakcho.ui.download.DownloadAdapter.*
import com.example.assignmentkakcho.ui.gallery.GalleryViewModel

private const val TAG = "DownloadAdapter"
class DownloadAdapter(val context: Context, val icon: Icon, val listener: OnItemClicked): RecyclerView.Adapter<DownloadViewHolder>() {
    private val list = icon.raster_sizes

    class  DownloadViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val tvQuality:TextView = view.findViewById(R.id.tvSize)
        val tvFormat: TextView = view.findViewById(R.id.tvFormat)
        val constraintL:ConstraintLayout = view.findViewById(R.id.constraintL)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_download,parent,false)
        Log.d(TAG,"oncreate view holder called")
        return DownloadViewHolder(view)
    }

    override fun onBindViewHolder(holder: DownloadViewHolder, position: Int) {
        val item = list[position]
        Log.d(TAG,"list: $list")
        val size = item.size.toString()
        val format = item.formats[0].format
        holder.tvQuality.text = "Size: $size ✖ $size "
        holder.tvFormat.text = "Format: $format"
        holder.itemView.setOnClickListener {
            listener.onItemClicked(position)
        }

    }

    override fun getItemCount(): Int {
        Log.d(TAG,"listcount called : ${list.size}")
        return list.size

    }

    interface OnItemClicked{
        fun onItemClicked(position: Int)
    }

}