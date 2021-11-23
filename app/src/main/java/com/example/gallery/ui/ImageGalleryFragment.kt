package com.example.gallery.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.paging.ExperimentalPagingApi
import com.example.gallery.R
import com.example.gallery.databinding.FragmentImageGalleryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalPagingApi
class ImageGalleryFragment : Fragment() {
    private var toolbar: Toolbar? = null
    private var navController: NavController? = null
    private var navHostFragment: NavHostFragment? = null
    private var _binding: FragmentImageGalleryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ImageGalleryViewModel by viewModels()

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageGalleryBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        toolbar = requireActivity().findViewById(R.id.toolbar)
        toolbar?.title = "Image Gallery"


    }

    private fun getNavController() {
        navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment?.navController
    }


}