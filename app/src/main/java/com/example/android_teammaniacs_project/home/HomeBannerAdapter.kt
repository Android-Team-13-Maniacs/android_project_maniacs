package com.example.android_teammaniacs_project.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android_teammaniacs_project.data.Video
import com.example.android_teammaniacs_project.databinding.HomeBannerItemBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class HomeBannerAdapter(
    private val onClickItem: (Int, Video) -> Unit,
) : ListAdapter<Video, HomeBannerAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<Video>() {
        override fun areItemsTheSame(
            oldItem: Video, newItem: Video
        ): Boolean {
            return oldItem.sourceUri == newItem.sourceUri
        }

        override fun areContentsTheSame(
            oldItem: Video,
            newItem: Video
        ): Boolean {
            return oldItem == newItem
        }
    }
) {

    var list = ArrayList<Video>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeBannerAdapter.ViewHolder {
        return ViewHolder(
            HomeBannerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClickItem
        )
    }

    override fun onBindViewHolder(holder: HomeBannerAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }


    class ViewHolder(
        private val binding: HomeBannerItemBinding,
        private val onClickItem: (Int, Video) -> Unit,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Video) = with(binding) {
            Glide.with(itemView).load(item.sourceUri)
//                .placeholder(R.drawable.loding) // 이미지 로딩 중 사진
//                .error(R.drawable.no) // 이미지를 불러오지 못했을 때 사진
                .into(ivHomeBanner)
            tvHomeBanner.text = item.title

            //recyclerview item clicklistener
            banner.setOnClickListener {
                onClickItem(
                    adapterPosition,
                    item
                )
            }
        }
    }

}