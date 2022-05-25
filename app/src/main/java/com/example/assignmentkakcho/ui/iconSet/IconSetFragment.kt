package com.example.assignmentkakcho.ui.iconSet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.example.assignmentkakcho.MainActivity
import com.example.assignmentkakcho.data.model.IconSet
import com.example.assignmentkakcho.databinding.FragmentIconSetBinding
import com.example.assignmentkakcho.ui.category.CategoryViewModel
import com.example.assignmentkakcho.ui.gallery.IconLoadStateAdapter


class IconSetFragment : Fragment(),  IconSetAdapter.OnItemClicked {
    private val categoryViewModel: CategoryViewModel by activityViewModels()
    private val args: IconSetFragmentArgs by navArgs()
    lateinit var category: String

    override fun onResume() {
        super.onResume()
        (activity as MainActivity?)?.let{
            it.supportActionBar?.title = category
            it.supportActionBar?.setDisplayShowHomeEnabled(false)
        }
    }

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
        val adapter = IconSetAdapter(this)
        binding.rvIconSet.adapter = adapter
        binding.apply {
            rvIconSet.setHasFixedSize(true)
            rvIconSet.adapter = adapter.withLoadStateHeaderAndFooter(
                header = IconLoadStateAdapter { adapter.retry() },
                footer = IconLoadStateAdapter { adapter.retry() }
            )
            btnRetry.setOnClickListener { adapter.retry() }
        }

        category = args.categoryIdentifier
        categoryViewModel.getIconSets(category)
        categoryViewModel.iconSetList.observe(viewLifecycleOwner, Observer {
            adapter.submitData(viewLifecycleOwner.lifecycle,it)
        })

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                rvIconSet.isVisible = loadState.source.refresh is LoadState.NotLoading
                btnRetry.isVisible = loadState.source.refresh is LoadState.Error
                txtError.isVisible = loadState.source.refresh is LoadState.Error

                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1
                ) {
                    rvIconSet.isVisible = false
                    txtEmpty.isVisible = true
                } else {
                    txtEmpty.isVisible = false
                }
            }
        }
    }

    override fun onItemClicked(iconSet: IconSet) {
        val action = IconSetFragmentDirections.actionIconSetFragmentToGalleryFragment(iconSet.iconset_id,"")
        findNavController().navigate(action)
    }

}