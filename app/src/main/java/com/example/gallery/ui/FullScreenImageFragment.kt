package com.example.gallery.ui

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.gallery.R
import com.example.gallery.databinding.FragmentFullScreenImageBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.*
import java.util.*


@AndroidEntryPoint
class FullScreenImageFragment : Fragment() {

    private var toolbar: Toolbar? = null
    private var URL: String = ""
    private var _binding: FragmentFullScreenImageBinding? = null
    private val binding get() = _binding!!

    private val TAG = "LOG!!"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFullScreenImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        URL = requireArguments().getString("url").toString()
        toolbar = requireActivity().findViewById(R.id.toolbar)
        (toolbar as Toolbar).visibility = View.VISIBLE
        toolbar?.title = ""


    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_ui, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.share -> {

                true
            }
            R.id.download -> {

                true
            }

            else ->
                super.onOptionsItemSelected(item)

        }

    }


}