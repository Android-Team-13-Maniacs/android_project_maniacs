package com.example.android_teammaniacs_project.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android_teammaniacs_project.R
import com.example.android_teammaniacs_project.data.Channel
import com.example.android_teammaniacs_project.databinding.VideoItemBinding

class HomeChannelAdapter() : ListAdapter<Channel, HomeChannelAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<Channel>() {
        override fun areItemsTheSame(
            oldItem: Channel, newItem: Channel
        ): Boolean {
            return oldItem.image == newItem.image
        }

        override fun areContentsTheSame(oldItem: Channel, newItem: Channel): Boolean {
            return oldItem == newItem
        }
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeChannelAdapter.ViewHolder {
        return ViewHolder(
            VideoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: HomeChannelAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(
        private val binding: VideoItemBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Channel) = with(binding) {
            Glide.with(itemView).load(item.image)
                .placeholder(R.drawable.img_loding) // 이미지 로딩 중 사진
                .error(R.drawable.img_no) // 이미지를 불러오지 못했을 때 사진
                .into(ivItem)
            tvItem.text = item.title

        }
    }

}

