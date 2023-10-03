package com.example.android_teammaniacs_project.detail

import SearchFragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.android_teammaniacs_project.R
import com.example.android_teammaniacs_project.constants.Constants
import com.example.android_teammaniacs_project.data.Video
import com.example.android_teammaniacs_project.databinding.VideoDetailActivityBinding
import com.example.android_teammaniacs_project.home.HomeFragment
import com.example.android_teammaniacs_project.myVideoPage.MyVideoFragment
import com.example.android_teammaniacs_project.utils.Utils.convertDateFormat
import com.google.gson.Gson


class VideoDetailActivity : AppCompatActivity() {
    private lateinit var binding: VideoDetailActivityBinding
    private var isLiked = false // "Like" 상태를 나타내는 변수
    private var isAdded = false // "My List" 상태를 나타내는 변수
    private var currentVideo: Video? = null

    private val recyclerView by lazy {
        CommentListAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = VideoDetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val homeVideo = intent.getParcelableExtra<Video>(HomeFragment.HOME_VIDEO_MODEL)
        val homePosition = intent.getIntExtra(HomeFragment.HOME_VIDEO_POSITION, -1)

        if (homeVideo != null) {
            // HomeFragment에서 전달한 데이터가 있는 경우
            this.currentVideo = homeVideo
            Glide.with(this)
                .load(homeVideo.image)
                .into(binding.ivVideo)
            binding.tvTitle.text = homeVideo.title
            binding.tvDate.text = convertDateFormat(homeVideo.date)
            binding.tvDescription.text = homeVideo.description
            binding.tvChannel.text = homeVideo.channelName
        } else {
            // HomeFragment에서 전달한 데이터가 없는 경우
            val searchVideo = intent.getParcelableExtra<Video>(SearchFragment.VIDEO_MODEL)
            val searchPosition = intent.getIntExtra(SearchFragment.VIDEO_POSITION, -1)

            if (searchVideo != null) {
                // SearchFragment에서 전달한 데이터가 있는 경우
                this.currentVideo = searchVideo
                Glide.with(this)
                    .load(searchVideo.image)
                    .into(binding.ivVideo)
                binding.tvTitle.text = searchVideo.title
                binding.tvDate.text = convertDateFormat(searchVideo.date)
                binding.tvDescription.text = searchVideo.description
                binding.tvChannel.text = searchVideo.channelName
            } else {
                // MyVideoFragment에서 전달한 데이터를 확인
                val myVideo = intent.getParcelableExtra<Video>(MyVideoFragment.MY_VIDEO_MODEL)
                val myVideoPosition = intent.getIntExtra(MyVideoFragment.MY_VIDEO_POSITION, -1)

                if (myVideo != null) {
                    // MyVideoFragment에서 전달한 데이터가 있는 경우
                    this.currentVideo = myVideo
                    Glide.with(this)
                        .load(myVideo.image)
                        .into(binding.ivVideo)
                    binding.tvTitle.text = myVideo.title
                    binding.tvDescription.text = myVideo.description
                    binding.tvChannel.text = myVideo.channelName
                    binding.tvDate.text = convertDateFormat(myVideo.date)
                }
            }
        }

        // 현재 비디오가 내 비디오에 저장 되어 있는지 확인
        if(currentVideo != null) {
            val sharedPref = this?.getSharedPreferences(Constants.MY_VIDEOS_KEY, Context.MODE_PRIVATE)
            val checkIfVideoExist = sharedPref?.getString(currentVideo?.title, null)
            if(checkIfVideoExist == null) {
                this.isAdded = false
                binding.btnAddMylist.isSelected = isAdded
            } else {
                this.isAdded = true
                binding.btnAddMylist.isSelected = isAdded
            }

            // 좋아요 상태를 가져옴
            val likeStatus = loadLikeStatusFromSharedPreferences()
            isLiked = likeStatus
            binding.btnLike.isSelected = isLiked
        }

        initView()
    }

    private fun initView() = with(binding) {

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

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
//            val intent = Intent(Intent.ACTION_SEND)
//            intent.type = "video/*"
//
//            // String으로 받아서 넣기
//            val sendMessage = "이렇게 스트링으로 만들어서 넣어주면 됩니다."
//            intent.putExtra(Intent.EXTRA_TEXT, sendMessage)
//            val shareIntent = Intent.createChooser(intent, "share")
//            startActivity(shareIntent)

            val videoUrl = currentVideo?.title
            val sendMessage = videoUrl ?: "비디오 URL을 찾을 수 없습니다."
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, sendMessage)
            val shareIntent = Intent.createChooser(intent, "비디오 공유")
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

            // 좋아요 상태를 SharedPreferences에 저장
            saveLikeStatusAndVideoInfoToSharedPreferences()
        }

        binding.btnAddMylist.setOnClickListener {
            // currentVideo가 있는지 검사
            if (currentVideo == null) {
                Error("비디오 없음!")
            }

            // sharedPreference에 데이터 저장
            val sharedPref = this@VideoDetailActivity.getSharedPreferences(
                Constants.MY_VIDEOS_KEY,
                Context.MODE_PRIVATE
            )
            // 기존에 있는 값을 불러옴
            val checkIfVideoExist = sharedPref.getString(currentVideo?.title, null)
            if (checkIfVideoExist == null) {
                // Object to JSON string
                // 저장이 안되어있는 비디오. 저장 수행
                with(sharedPref.edit()) {
                    putString(currentVideo?.title, Gson().toJson(currentVideo))
                    apply()
                }
                isAdded = true
                binding.btnAddMylist.isSelected = isAdded
            } else {
                // 이미 저장되어 있는 비디오. 제거 수행
                with(sharedPref.edit()) {
                    remove(currentVideo?.title)
                    apply()
                }
                isAdded = false
                binding.btnAddMylist.isSelected = isAdded
            }

            // 토스트 메시지 추가
            val toastMessage = if (isAdded) "내 목록에 추가되었습니다." else "내 목록에서 삭제되었습니다."
            Toast.makeText(this@VideoDetailActivity, toastMessage, Toast.LENGTH_SHORT).show()

            // 좋아요 상태를 SharedPreferences에 저장
            saveLikeStatusAndVideoInfoToSharedPreferences()
        }

        val backButton = findViewById<Button>(R.id.btn_back)
        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    // 좋아요 상태와 비디오 정보를 SharedPreferences에 저장하는 함수
    private fun saveLikeStatusAndVideoInfoToSharedPreferences() {
        if (currentVideo != null) {
            val sharedPref = getSharedPreferences(Constants.LIKED_VIDEOS_KEY, Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putBoolean(currentVideo?.title, isLiked)
                // 비디오 정보를 JSON 문자열로 저장
                putString(currentVideo?.title + "_info", Gson().toJson(currentVideo))
                apply()
            }
        }
    }

    // 좋아요 상태를 SharedPreferences에서 가져오는 함수
    private fun loadLikeStatusFromSharedPreferences(): Boolean {
        return if (currentVideo != null) {
            val sharedPref = getSharedPreferences(Constants.LIKED_VIDEOS_KEY, Context.MODE_PRIVATE)
            sharedPref.getBoolean(currentVideo?.title, false)
        } else {
            false
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.drawable.fade_in, R.drawable.fade_out)
    }
}