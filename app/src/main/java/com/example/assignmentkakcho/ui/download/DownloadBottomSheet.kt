package com.example.assignmentkakcho.ui.download

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.example.assignmentkakcho.R
import com.example.assignmentkakcho.data.model.Icon
import com.example.assignmentkakcho.databinding.FragmentDownloadBottomSheetBinding
import com.example.assignmentkakcho.ui.gallery.GalleryViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DownloadBottomSheet(val icon: Icon) : BottomSheetDialogFragment() {
     val TAG ="bottomSheet"
    private val viewModel by viewModels<GalleryViewModel>()
    lateinit var binding: FragmentDownloadBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding  = FragmentDownloadBottomSheetBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG,"currentIcon:${icon}")
            binding.rvQualities.adapter = DownloadAdapter(icon)
            binding.rvQualities.hasFixedSize()
    }
}