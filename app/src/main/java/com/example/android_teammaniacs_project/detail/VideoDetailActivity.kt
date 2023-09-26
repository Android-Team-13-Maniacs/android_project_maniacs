package com.example.android_teammaniacs_project.detail

import SearchFragment
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.android_teammaniacs_project.R
import com.example.android_teammaniacs_project.databinding.VideoDetailActivityBinding
import com.example.android_teammaniacs_project.home.HomeFragment
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
        val homevideo = intent.getStringExtra(HomeFragment.HOME_VIDEO_MODEL)
        val position = intent.getIntExtra(SearchFragment.VIDEO_POSITION, -1)
        val myposition = intent.getIntExtra(MyVideoFragment.MY_VIDEO_POSITION, -1)
        val homeposition = intent.getIntExtra(HomeFragment.HOME_VIDEO_POSITION, -1)


        initView()

    }

    private fun initView() = with(binding) {
        //recycler view
        commentList.adapter = recyclerView

        //test data
        val list = ArrayList<CommentModel>()
        for (i in 0..3) {
            list.add(
                CommentModel(
                    0,
                    com.example.android_teammaniacs_project.R.drawable.dog,
                    "$i name",
                    "$i date",
                    "$i coment"
                )
            )
            for (i in 0..3) {
                list.add(CommentModel(0, R.drawable.dog, "$i name", "$i date", "$i coment"))
            }
            recyclerView.addItems(list)
        }

        val shareButton =
            findViewById<Button>(com.example.android_teammaniacs_project.R.id.btn_Share)


        shareButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "video/*"

            // String으로 받아서 넣기
            val sendMessage = "이렇게 스트링으로 만들어서 넣어주면 됩니다."
            intent.putExtra(Intent.EXTRA_TEXT, sendMessage)
            val shareIntent = Intent.createChooser(intent, "share")
            startActivity(shareIntent)
        }

        binding.btnLike.setOnClickListener {
            // 상태 변경
            isLiked = !isLiked

            // 버튼의 상태에 따라 이미지 변경
            binding.btnLike.isSelected = isLiked

            // 토스트 메시지 추가
            val toastMessage = if (isLiked) "좋아요가 눌렸습니다." else "좋아요가 취소되었습니다."
            Toast.makeText(this@VideoDetailActivity, toastMessage, Toast.LENGTH_SHORT).show()
        }


        binding.btnAddMylist.setOnClickListener {
            // 상태 변경
            isAdded = !isAdded

            // 버튼의 상태에 따라 이미지 변경
            binding.btnAddMylist.isSelected = isAdded
        }
        val backButton = findViewById<Button>(R.id.btn_back)
        backButton.setOnClickListener {
            onBackPressed()
        }

    }

    private fun initView() = with(binding) {
        //recycler view
        commentList.adapter = recyclerView

            // 토스트 메시지 추가
            val toastMessage = if (isAdded) "내 목록에 추가되었습니다." else "내 목록에서 삭제되었습니다."
            Toast.makeText(this@VideoDetailActivity, toastMessage, Toast.LENGTH_SHORT).show()
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.drawable.fade_in, R.drawable.fade_out)
    }
}