package com.example.android_teammaniacs_project.myVideoPage

import android.view.LayoutInflater
import com.example.android_teammaniacs_project.data.Video
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android_teammaniacs_project.databinding.VideoItemBinding

class MyVideoAdapter(
    private val onClickItem: (Int, Video) -> Unit,
) : ListAdapter<Video, MyVideoAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<Video>() {
        override fun areItemsTheSame(
            oldItem: Video, newItem: Video
        ): Boolean {
            return oldItem.channelId == newItem.channelId
        }

        override fun areContentsTheSame(
            oldItem: Video,
            newItem: Video
        ): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVideoAdapter.ViewHolder {
        return ViewHolder(
            VideoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClickItem
        )
    }

    override fun onBindViewHolder(holder: MyVideoAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(
        private val binding: VideoItemBinding,
        private val onClickItem: (Int, Video) -> Unit,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Video) = with(binding) {
            tvItem.text = item.title

            //recyclerview item clicklistener
            video.setOnClickListener {
                onClickItem(
                    adapterPosition,
                    item
                )
            }
        }
    }
}