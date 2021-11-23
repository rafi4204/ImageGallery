package com.example.gallery.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gallery.R
import com.example.gallery.data.ImageModel
import com.example.gallery.databinding.FragmentImageGalleryBinding
import com.example.gallery.ui.adapter.ImageAdapter
import com.example.gallery.ui.viewModel.ImageGalleryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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

        getNavController()
        val adapter = ImageAdapter(requireContext())
        binding.rvNewsList.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvNewsList.adapter = adapter
        lifecycleScope.launchWhenCreated {
            viewModel.getImage()
        }
        viewModel.list.observe(viewLifecycleOwner, {
            lifecycleScope.launch {
                adapter.submitData(it)
            }
        })
        adapter.listener = object : ImageAdapter.ClickListener{
            override fun onClick(model: ImageModel) {
                val bundle = bundleOf("url" to model.download_url)
                navController?.navigate(
                    R.id.action_imageGalleryFragment_to_fullScreenImageFragment,
                    bundle
                )
            }

        }
        toolbar = requireActivity().findViewById(R.id.toolbar)
        toolbar?.title = "Image Gallery"


    }

    private fun getNavController() {
        navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment?.navController
    }

}