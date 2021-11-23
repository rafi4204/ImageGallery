package com.example.gallery.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.gallery.data.ImageModel
import com.example.gallery.databinding.ImageItemLayoutBinding

class ImageAdapter( val context: Context) :
    PagingDataAdapter<ImageModel, ImageAdapter.CustomViewHolder>(REPO_COMPARATOR) {

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<ImageModel>() {
            override fun areItemsTheSame(oldItem: ImageModel, newItem: ImageModel): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: ImageModel, newItem: ImageModel): Boolean =
                oldItem == newItem
        }
    }
    var listener: ClickListener? = null

    interface ClickListener {
        fun onClick(model: ImageModel)
    }

    inner class CustomViewHolder(val binding: ImageItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: ImageModel) {
            Glide.with(context)
                .load(model.download_url)
                .diskCacheStrategy(DiskCacheStrategy.DATA)

                .into(binding.imageView)

            binding.rootLayout.setOnClickListener {
                listener?.onClick(model)
            }

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val binding =
            ImageItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(binding)
    }


    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val model = getItem(position)
        model?.let { holder.bind(it) }

    }



}