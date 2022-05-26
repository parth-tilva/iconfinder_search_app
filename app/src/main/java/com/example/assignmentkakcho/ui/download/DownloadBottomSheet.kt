package com.example.assignmentkakcho.ui.download

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.assignmentkakcho.databinding.FragmentDownloadBottomSheetBinding
import com.example.assignmentkakcho.ui.gallery.GalleryViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DownloadBottomSheet() : BottomSheetDialogFragment(), DownloadAdapter.OnItemClicked {
    private val galleryViewModel: GalleryViewModel by activityViewModels()
    private var _binding: FragmentDownloadBottomSheetBinding? = null
    private val binding = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDownloadBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val icon = galleryViewModel.currentIcon
        binding.rvQualities.adapter = DownloadAdapter(requireContext(), icon, this)
    }

    override fun onItemClicked(position: Int) {
        lifecycleScope.launchWhenCreated {
            galleryViewModel.download(galleryViewModel.currentIcon, position)
            dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}