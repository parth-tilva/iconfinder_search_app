package com.example.assignmentkakcho.ui.iconSet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmentkakcho.R
import com.example.assignmentkakcho.data.model.IconSet
import com.example.assignmentkakcho.ui.gallery.GalleryViewModel
import com.example.assignmentkakcho.ui.gallery.IconAdapter

class IconSetAdapter(val viewModel: GalleryViewModel, private val viewLifecycleOwner: LifecycleOwner, val listener: IconAdapter.OnItemClickListener): PagingDataAdapter<IconSet,IconSetAdapter.IconSetViewHolder>(
    ICON_SET_COMPARATOR)  {

    class IconSetViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val rvIcons: RecyclerView = view.findViewById(R.id.recycler_view)
        val tv: TextView = view.findViewById(R.id.iconset_id)
    }

    companion object {
        private val ICON_SET_COMPARATOR = object : DiffUtil.ItemCallback<IconSet>() {
            override fun areItemsTheSame(oldItem: IconSet, newItem: IconSet) =
                oldItem.iconset_id == newItem.iconset_id

            override fun areContentsTheSame(oldItem: IconSet, newItem: IconSet) =
                oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder:IconSetViewHolder, position: Int) {
        val iconSet = getItem(position)
        //val adapter = IconAdapter(listener)
        //holder.rvIcons.adapter =adapter
        //holder.rvIcons.setHasFixedSize(true)
        if (iconSet != null) {
            holder.tv.text = iconSet.iconset_id.toString()
//            viewModel.getIconsInIconSet(iconSet.iconset_id).observe(viewLifecycleOwner, Observer {
//                adapter.submitData(viewLifecycleOwner.lifecycle,it)
//            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconSetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_gallery,parent,false)
        return IconSetViewHolder(view)
    }
}
