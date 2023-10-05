package com.example.android_teammaniacs_project.detail

import SearchFragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.android_teammaniacs_project.R
import com.example.android_teammaniacs_project.constants.Constants
import com.example.android_teammaniacs_project.constants.GoogleKey
import com.example.android_teammaniacs_project.data.Video
import com.example.android_teammaniacs_project.databinding.VideoDetailActivityBinding
import com.example.android_teammaniacs_project.home.HomeFragment
import com.example.android_teammaniacs_project.myVideoPage.MyVideoFragment
import com.example.android_teammaniacs_project.retrofit.RetrofitClient
import com.example.android_teammaniacs_project.utils.Utils.convertDateFormat
import com.google.gson.Gson

class VideoDetailActivity : AppCompatActivity() {
    private lateinit var binding: VideoDetailActivityBinding
    private var isLiked = false // "Like" 상태를 나타내는 변수
    private var isAdded = false // "My List" 상태를 나타내는 변수
    private var currentVideo: Video? = null

    private val apiService = RetrofitClient.apiService
    private val viewModel : VideoDetailViewModel by viewModels {VideoDetailViewModelFactory(apiService)}

    //Channel Api 호출 매개변수
    private val key = GoogleKey.KEY
    private val part = "snippet"
    private var channelId = ""
    private val maxResults = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = VideoDetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val homeVideo = intent.getParcelableExtra<Video>(HomeFragment.HOME_VIDEO_MODEL)

        if (homeVideo != null) {
            // HomeFragment에서 전달한 데이터가 있는 경우
            setContent(homeVideo)
        } else {
            // HomeFragment에서 전달한 데이터가 없는 경우
            val searchVideo = intent.getParcelableExtra<Video>(SearchFragment.VIDEO_MODEL)

            if (searchVideo != null) {
                // SearchFragment에서 전달한 데이터가 있는 경우
                setContent(searchVideo)
            } else {
                // MyVideoFragment에서 전달한 데이터를 확인
                val myVideo = intent.getParcelableExtra<Video>(MyVideoFragment.MY_VIDEO_MODEL)

                if (myVideo != null) {
                    // MyVideoFragment에서 전달한 데이터가 있는 경우
                    setContent(myVideo)
                }
            }
        }

        // 현재 비디오가 내 비디오에 저장 되어 있는지 확인
        if(currentVideo != null) {
            val sharedPref = this.getSharedPreferences(Constants.MY_VIDEOS_KEY, Context.MODE_PRIVATE)
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

        btnShare.setOnClickListener {
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

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        setChannelImage()
        observeVieModel()
    }

    private fun setContent(video : Video) {
        this.currentVideo = video
        Glide.with(this)
            .load(video.image)
            .into(binding.ivVideo)
        binding.tvTitle.text = video.title
        binding.tvDate.text = convertDateFormat(video.date)
        binding.tvDescription.text = video.description
        binding.tvChannel.text = video.channelName

        channelId = video.channelId
    }
    //API 호출
    private fun setChannelImage() {
        viewModel.getChannelImage(key,part,channelId,maxResults)
    }
    // LiveData observe
    private fun observeVieModel() {
        viewModel.category.observe(this) {
            Glide.with(this)
                .load(it)
                .into(binding.ivChannel)
        }
        viewModel.detailError.observe(this) {
            if(!it) {
                Toast.makeText(this,"API 연동 Error 입니다.",Toast.LENGTH_SHORT).show()
            }
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