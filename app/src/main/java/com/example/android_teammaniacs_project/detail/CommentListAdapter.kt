package com.example.android_teammaniacs_project.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android_teammaniacs_project.databinding.CommentItemBinding

class CommentListAdapter : RecyclerView.Adapter<CommentListAdapter.ViewHolder>() {
    private val list = ArrayList<CommentModel>()

    fun addItems(items: ArrayList<CommentModel>) {
        list.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentListAdapter.ViewHolder {
        return ViewHolder(
            CommentItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(
        private val binding: CommentItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CommentModel) = with(binding) {
//            Glide.with(itemView).load(item.url)
//                .placeholder(R.drawable.loding) // 이미지 로딩 중 사진
//                .error(R.drawable.no) // 이미지를 불러오지 못했을 때 사진
//                .into(imageView)
            tvDate.text = item.date
            tvComent.text = item.comment
            tvName.text = item.name


        }

    }
}

