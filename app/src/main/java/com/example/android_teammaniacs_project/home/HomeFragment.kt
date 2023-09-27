package com.example.android_teammaniacs_project.home

import android.R
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android_teammaniacs_project.constants.GoogleKey
import com.example.android_teammaniacs_project.data.Category
import com.example.android_teammaniacs_project.data.Video
import com.example.android_teammaniacs_project.databinding.FragmentHomeBinding
import com.example.android_teammaniacs_project.detail.VideoDetailActivity
import com.example.android_teammaniacs_project.retrofit.RetrofitClient


class HomeFragment : Fragment() {
    companion object {
        fun newInstance() = HomeFragment()
        val HOME_VIDEO_POSITION = "home_video_position"
        val HOME_VIDEO_MODEL = "home_video_model"
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var contexts: Context

    private val apiService = RetrofitClient.apiService

    private val viewModel: HomeViewModel by viewModels { HomeViewModelFactory(apiService) }

    private val bannerAdapter by lazy {
        HomeBannerAdapter(onClickItem = { position, video ->
            startVideoDetailActivity(position, video)
        })
    }

    private val section1Adapter by lazy {
        HomeVideoAdapter(onClickItem = { position, video ->
            startVideoDetailActivity(position, video)
        })
    }

    private val section2Adapter by lazy {
        HomeVideoAdapter(onClickItem = { position, video ->
            startVideoDetailActivity(position, video)
        })
    }

    private val key = GoogleKey.KEY
    private val part = "snippet"
    private val chart = "mostPopular"
    private val maxResults = 20
    private val videoCategoryId = "1"
    private val regionCode = "KR"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contexts = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //리사이클러뷰에 대한 초기화 필요
        initView()
        initViewModel()
        setBanner()

    }

    private fun setBanner() {
        viewModel.setBanner(key, part, chart, maxResults)
        viewModel.getCategory(key, part, regionCode)
        viewModel.getCategoryVideo(key, part, chart, maxResults, videoCategoryId)
    }

    private fun initView() = with(binding) {
        //임시 스피너
        val arraySpinner = arrayOf(
            "Gaming", "Sports", "Comedy", "Short Movies", "Entertainment"
        )
        val s = binding?.homeSpinner
        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            contexts,
            R.layout.simple_spinner_item, arraySpinner
        )
        spinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_item)
        s?.adapter = spinnerAdapter
    }

    private fun initViewModel() = with(viewModel) {
        list.observe(viewLifecycleOwner) {
            section2Adapter.submitList(it)
            setupRecyclerView()
        }
        popularList.observe(viewLifecycleOwner) {
            bannerAdapter.submitList(it)
            setupRecyclerView()
        }
        categoryVideoList.observe(viewLifecycleOwner) {
            section1Adapter.submitList(it)
            setupRecyclerView()
        }
    }

    private fun startVideoDetailActivity(position: Int, video: Video) {
        val intent = Intent(context, VideoDetailActivity::class.java)
        intent.apply {
            putExtra(HOME_VIDEO_POSITION, position)
            putExtra(HOME_VIDEO_MODEL, video)
        }
        startActivity(intent)
    }


    private fun setupRecyclerView() = with(binding) {

        //홈배너 어댑터 설정
        val adapter = bannerAdapter
        binding.rvHomeBanner.adapter = adapter

        //섹션 어댑터 1,2 설정
        val adapter1 = section1Adapter
        binding.rvHomeSection1.adapter = adapter1

        val adapter2 = section1Adapter
        binding.rvHomeSection2.adapter = adapter2
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}