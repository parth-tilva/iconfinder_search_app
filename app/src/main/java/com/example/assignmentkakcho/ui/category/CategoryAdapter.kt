package com.example.assignmentkakcho.ui.category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmentkakcho.R
import com.example.assignmentkakcho.data.model.Category

class CategoryAdapter(private val listener: OnItemClickListener):
    PagingDataAdapter<Category, CategoryAdapter.CategoryViewHolder>(CATEGORY_COMPARATOR) {


    class CategoryViewHolder( view: View): RecyclerView.ViewHolder(view){
        val tvName:TextView  = view.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1,parent,false)
        return CategoryViewHolder(view)
    }


    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = getItem(position)
        if (category != null) {
            holder.tvName.text = category.name
        }
    }

    companion object {
        private val CATEGORY_COMPARATOR = object : DiffUtil.ItemCallback<Category>() {
            override fun areItemsTheSame(oldItem: Category, newItem: Category) =
                oldItem.identifier == newItem.identifier

            override fun areContentsTheSame(oldItem: Category, newItem: Category) =
                oldItem == newItem
        }
    }

    interface OnItemClickListener {
         fun onItemClick()
    }


}


