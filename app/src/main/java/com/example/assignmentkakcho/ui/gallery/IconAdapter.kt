package com.example.assignmentkakcho.ui.gallery

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.assignmentkakcho.R
import com.example.assignmentkakcho.data.model.Icon
import com.example.assignmentkakcho.databinding.ItemIconBinding

class IconAdapter(private val listener: OnItemClickListener) :
    PagingDataAdapter<Icon, IconAdapter.PhotoViewHolder>(ICON_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding =
            ItemIconBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    inner class PhotoViewHolder(private val binding: ItemIconBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            binding.apply {
                root.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val item = getItem(position)
                        if (item != null) {
                            listener.onItemClick(item)
                        }
                    }
                }
                imgDownload.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val item = getItem(position)
                        if (item != null) {
                            listener.onDownloadClicked(item)
                        }
                    }
                }
            }
        }

        fun bind(icon: Icon) {
            val tag = icon.tags.random()
            //val name = icon.icon_id.toString() + "_" + tag
            if (icon.is_premium) {
                val price = "$" + icon.prices[0].price.toString()
                binding.apply {
                    imgPaid.isVisible = true
                    txtPrice.isVisible = true
                    txtPrice.text = price
                    imgDownload.isVisible = false
                }
            } else {
                binding.apply {
                    imgDownload.isVisible = true
                    imgPaid.isVisible = false
                    txtPrice.isVisible = false
                }
            }
            var index = icon.raster_sizes.size - 3
            if (index < 0 || icon.raster_sizes[index].size < 128) {
                index = icon.raster_sizes.size - 1
            }

            Log.d(
                "sapi",
                "${icon.raster_sizes[index].size} , index: $index , icon_id: ${icon.icon_id}"
            )
            binding.apply {
                Glide.with(itemView)
                    .load(icon.raster_sizes[index].formats[0].preview_url)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(imgView)
                txtName.text = tag

            }
        }
    }

    interface OnItemClickListener {
       fun onItemClick(icon: Icon)
        fun onDownloadClicked(icon: Icon)
    }

    companion object {
        private val ICON_COMPARATOR = object : DiffUtil.ItemCallback<Icon>() {
            override fun areItemsTheSame(oldItem: Icon, newItem: Icon) =
                oldItem.icon_id == newItem.icon_id

            override fun areContentsTheSame(oldItem: Icon, newItem: Icon) =
                oldItem == newItem
        }
    }
}