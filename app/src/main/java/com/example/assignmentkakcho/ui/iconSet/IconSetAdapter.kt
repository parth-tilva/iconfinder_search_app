package com.example.assignmentkakcho.ui.iconSet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmentkakcho.R
import com.example.assignmentkakcho.data.model.IconSet

class IconSetAdapter(val listener: OnItemClicked) :
    PagingDataAdapter<IconSet, IconSetAdapter.IconSetViewHolder>(
        ICON_SET_COMPARATOR
    ) {

    inner class IconSetViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val tvId: TextView = view.findViewById(R.id.iconset_id)
        private val tvAuthName: TextView = view.findViewById(R.id.tvAuthorName)
        private val tvIconsCount: TextView = view.findViewById(R.id.tvIconsCount)
        private val tvPremium: TextView = view.findViewById(R.id.tvPremium)


        init {
            view.setOnClickListener {
                val position = bindingAdapterPosition
                if(position!= RecyclerView.NO_POSITION){
                    val iconSet = getItem(position)
                    if(iconSet!=null){
                        listener.onItemClicked(iconSet)
                    }
                }
            }
        }
        fun bind(iconSet: IconSet) {
            tvId.text = "Id: " + iconSet.iconset_id.toString()
            tvAuthName.text = "Author: " + iconSet.author.name
            tvIconsCount.text = "Count: " + iconSet.icons_count.toString()
            if(iconSet.is_premium){
                tvPremium.text = "is_premium: true"
            }else{
                tvPremium.text = "is_premium: false"
            }
        }
    }

    companion object {
        private val ICON_SET_COMPARATOR = object : DiffUtil.ItemCallback<IconSet>() {
            override fun areItemsTheSame(oldItem: IconSet, newItem: IconSet) =
                oldItem.iconset_id == newItem.iconset_id

            override fun areContentsTheSame(oldItem: IconSet, newItem: IconSet) =
                oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: IconSetViewHolder, position: Int) {
        val iconSet = getItem(position)
        if (iconSet != null) {
            holder.bind(iconSet)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconSetViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_icon_set, parent, false)
        return IconSetViewHolder(view)
    }

    interface OnItemClicked {
        fun onItemClicked(iconSet: IconSet)
    }
}
