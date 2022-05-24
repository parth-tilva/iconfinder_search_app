package com.example.assignmentkakcho.ui.iconSet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.assignmentkakcho.R
import com.example.assignmentkakcho.data.model.Icon
import com.example.assignmentkakcho.databinding.FragmentIconSetBinding
import com.example.assignmentkakcho.ui.category.CategoryViewModel
import com.example.assignmentkakcho.ui.gallery.GalleryViewModel
import com.example.assignmentkakcho.ui.gallery.IconAdapter


class IconSetFragment : Fragment(), IconAdapter.OnItemClickListener {

    private val categoryViewModel: CategoryViewModel by activityViewModels()
    private val galleryViewModel: GalleryViewModel by activityViewModels()
    val args: IconSetFragmentArgs by navArgs()


    lateinit var binding: FragmentIconSetBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIconSetBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val category = args.categoryIdentifier
        val adapter = IconSetAdapter(galleryViewModel,viewLifecycleOwner,this)
        binding.rvIconSet.adapter = adapter
        binding.rvIconSet.setHasFixedSize(true)
        categoryViewModel.getIconSets(category).observe(viewLifecycleOwner, Observer {
            adapter.submitData(viewLifecycleOwner.lifecycle,it)
        })
    }

    override fun onItemClick(icon: Icon) {
        TODO("Not yet implemented")
    }

    override fun onDownloadClicked(icon: Icon) {
        TODO("Not yet implemented")
    }
}