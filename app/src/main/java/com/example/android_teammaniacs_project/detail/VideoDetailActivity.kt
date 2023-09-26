package com.example.android_teammaniacs_project.detail

import SearchFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.android_teammaniacs_project.R
import com.example.android_teammaniacs_project.databinding.VideoDetailActivityBinding
import com.example.android_teammaniacs_project.myVideoPage.MyVideoFragment

class VideoDetailActivity : AppCompatActivity() {
    private lateinit var binding: VideoDetailActivityBinding
    private var isLiked = false // "Like" 상태를 나타내는 변수
    private var isAdded = false // "My List" 상태를 나타내는 변수

    private val recyclerView by lazy {
        CommentListAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = VideoDetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val video = intent.getStringExtra(SearchFragment.VIDEO_MODEL)
        val myvideo = intent.getStringExtra(MyVideoFragment.MY_VIDEO_MODEL)
        val position = intent.getIntExtra(SearchFragment.VIDEO_POSITION, -1)
        val myposition = intent.getIntExtra(MyVideoFragment.MY_VIDEO_POSITION, -1)


        initView()

        binding.btnLike.setOnClickListener {
            // 상태 변경
            isLiked = !isLiked

            // 버튼의 상태에 따라 이미지 변경
            binding.btnLike.isSelected = isLiked
        }


        binding.btnAddMylist.setOnClickListener {
            // 상태 변경
            isAdded = !isAdded

            // 버튼의 상태에 따라 이미지 변경
            binding.btnAddMylist.isSelected = isAdded
        }

    }

    private fun initView() = with(binding) {
        //recycler view
        commentList.adapter = recyclerView

        //test data
        val list = ArrayList<CommentModel>()
        for (i in 0..3) {
            list.add(CommentModel(0, R.drawable.dog, "$i name", "$i date", "$i coment"))
        }
        recyclerView.addItems(list)
    }
}