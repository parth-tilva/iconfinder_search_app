package com.example.assignmentkakcho.ui.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.assignmentkakcho.MainActivity
import com.example.assignmentkakcho.R
import com.example.assignmentkakcho.data.model.Category
import com.example.assignmentkakcho.databinding.FragmentCategoryBinding
import com.example.assignmentkakcho.databinding.FragmentGalleryBinding
import com.example.assignmentkakcho.ui.gallery.GalleryViewModel
import com.example.assignmentkakcho.ui.gallery.IconAdapter
import com.example.assignmentkakcho.ui.gallery.IconLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

@AndroidEntryPoint
class CategoryFragment : Fragment(), CategoryAdapter.OnItemClickListener {

    private val viewModel by viewModels<CategoryViewModel>()

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!


    override fun onResume() {
        super.onResume()
        (activity as MainActivity?)?.let{
            it.supportActionBar?.setDisplayShowHomeEnabled(true)
            it.supportActionBar?.setIcon(R.drawable.iconfinder_logo_icon)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = CategoryAdapter(this)


        binding.apply {
            rvCategories.setHasFixedSize(true)
            rvCategories.adapter = adapter.withLoadStateHeaderAndFooter(
                header = IconLoadStateAdapter { adapter.retry() },
                footer = IconLoadStateAdapter { adapter.retry() }
            )
            btnRetry.setOnClickListener { adapter.retry() }
        }
        binding.rvCategories.adapter = adapter

        viewModel.categoryList.observe(viewLifecycleOwner, Observer {
            adapter.submitData(viewLifecycleOwner.lifecycle,it)
        })

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                rvCategories.isVisible = loadState.source.refresh is LoadState.NotLoading
                btnRetry.isVisible = loadState.source.refresh is LoadState.Error
                txtError.isVisible = loadState.source.refresh is LoadState.Error

                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1
                ) {
                    rvCategories.isVisible = false
                    txtEmpty.isVisible = true
                } else {
                    txtEmpty.isVisible = false
                }
            }
        }

    }

    override fun onItemClick(category: Category) {
        val action = CategoryFragmentDirections.actionCategoryFragmentToIconSetFragment(category.identifier)
        findNavController().navigate(action)
    }

}