package com.example.android_teammaniacs_project.home

import android.R
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android_teammaniacs_project.constants.GoogleKey
import com.example.android_teammaniacs_project.data.Category
import com.example.android_teammaniacs_project.data.Video
import com.example.android_teammaniacs_project.databinding.FragmentHomeBinding
import com.example.android_teammaniacs_project.detail.VideoDetailActivity
import com.example.android_teammaniacs_project.retrofit.CategoryItem
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


    }
    private fun setupSpinner(categories: List <CategoryItem>){
//        val arraySpinner = arrayOf(
//            "Gaming", "Sports", "Comedy", "Short Movies", "Entertainment"
//        )
        val arraySpinner = categories.map { it.snippet.title }.toTypedArray()
        val arraySpinner1 = categories.map { it.id }.toTypedArray()

        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            contexts,
            com.example.android_teammaniacs_project.R.layout.home_spinner_item, // 스피너 아이템 레이아웃
            arraySpinner
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.homeSpinner.adapter = spinnerAdapter

        // 스피너 아이템 선택 리스너 설정
        binding.homeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                val selectedCategory = arraySpinner[position]

            }

            override fun onNothingSelected(parentView: AdapterView<*>) {

            }
        }
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
        categoryList.observe(viewLifecycleOwner) {
            setupSpinner(it)
        }
    }

    private fun startVideoDetailActivity(position: Int, video: Video) {
        val intent = Intent(context, VideoDetailActivity::class.java)
        intent.apply {
            putExtra(HOME_VIDEO_POSITION, position)
            putExtra(HOME_VIDEO_MODEL, video)
        }
        startActivity(intent)
        activity?.overridePendingTransition(com.example.android_teammaniacs_project.R.drawable.fade_in, com.example.android_teammaniacs_project.R.drawable.fade_out)
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