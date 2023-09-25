package com.example.android_teammaniacs_project.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android_teammaniacs_project.data.Video
import com.example.android_teammaniacs_project.databinding.VideoItemBinding

class HomeVideoAdapter(var contexts: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items = mutableListOf<Video>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = VideoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Glide.with(contexts)
            .load(items[position].sourceUri)
            .into((holder as ItemViewHolder).imgThumbnail)

        holder.txtTitle.text = items[position].title
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ItemViewHolder(binding: VideoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val imgThumbnail: ImageView = binding.itemImageView
        var txtTitle: TextView = binding.itemTitleText
    }

}