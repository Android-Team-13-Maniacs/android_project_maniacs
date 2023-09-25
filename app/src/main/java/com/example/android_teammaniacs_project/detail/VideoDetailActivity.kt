package com.example.android_teammaniacs_project.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.android_teammaniacs_project.R
import com.example.android_teammaniacs_project.databinding.VideoDetailActivityBinding

class VideoDetailActivity : AppCompatActivity() {
    private lateinit var binding: VideoDetailActivityBinding

    private val recyclerView by lazy {
        CommentListAdapter()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=VideoDetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() = with(binding) {
        //recycler view
        commentList.adapter=recyclerView

        //test data
        val list = ArrayList<CommentModel>()
        for ( i in 0..3) {
            list.add(CommentModel(0, R.drawable.dog,"$i name","$i date","$i coment"))
        }
        recyclerView.addItems(list)
    }
}