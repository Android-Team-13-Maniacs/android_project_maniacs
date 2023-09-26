package com.example.android_teammaniacs_project.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android_teammaniacs_project.data.Video
import com.example.android_teammaniacs_project.databinding.HomeBannerItemBinding
import com.example.android_teammaniacs_project.databinding.VideoItemBinding

class HomeVideoAdapter(
    private val onClickItem: (Int, Video) -> Unit,
) : RecyclerView.Adapter<HomeVideoAdapter.ViewHolder>() {

    var list = ArrayList<Video>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeVideoAdapter.ViewHolder {
        return ViewHolder(
            VideoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClickItem
        )
    }


    override fun onBindViewHolder(holder: HomeVideoAdapter.ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }


    class ViewHolder(
        private val binding: VideoItemBinding,
        private val onClickItem: (Int, Video) -> Unit,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Video) = with(binding) {
            Glide.with(itemView).load(item.sourceUri)
//                .placeholder(R.drawable.loding) // 이미지 로딩 중 사진
//                .error(R.drawable.no) // 이미지를 불러오지 못했을 때 사진
                .into(ivItem)
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