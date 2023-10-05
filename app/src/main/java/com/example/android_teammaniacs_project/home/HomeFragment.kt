package com.example.android_teammaniacs_project.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.android_teammaniacs_project.R
import com.example.android_teammaniacs_project.constants.GoogleKey
import com.example.android_teammaniacs_project.data.Category
import com.example.android_teammaniacs_project.data.Video
import com.example.android_teammaniacs_project.databinding.FragmentHomeBinding
import com.example.android_teammaniacs_project.detail.VideoDetailActivity
import com.example.android_teammaniacs_project.retrofit.RetrofitClient
import me.relex.circleindicator.CircleIndicator2


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

    private val categoryToSpinnerUpperList = ArrayList<Category>()

    private val bannerAdapter by lazy {
        HomeBannerAdapter(onClickItem = { position, video ->
            startVideoDetailActivity(position, video)
        })
    }

    private val categoryVideoAdapter by lazy {
        HomeVideoAdapter(onClickItem = { position, video ->
            startVideoDetailActivity(position, video)
        })
    }

    private val categoryChannelAdapter by lazy {
        HomeChannelAdapter()
    }

    //API 연동을 위해 입력할 값들 정의
    private val key = GoogleKey.KEY
    private val part = "snippet"
    private val chart = "mostPopular"
    private val maxResults = 20
    private val maxResultsPopular = 10
    private val regionCode = "KR"
    private var viewLocation = "upper"
    private val order = "viewCount"
    private val type = "channel"


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
        initView()
        initViewModel()
        setBanner()

    }

    private fun initView() = with(binding) {
        //섹션 어댑터 1,2 설정
        rvHomeSection1.adapter = categoryVideoAdapter
        rvHomeSection2.adapter = categoryChannelAdapter
    }

    //live data를 받아와서 RecyclerView Adapter에 데이터 전달
    private fun initViewModel() = with(viewModel) {
        popularList.observe(viewLifecycleOwner) {
            bannerAdapter.submitList(it)
        }
        categoryUpperVideoList.observe(viewLifecycleOwner) {
            categoryVideoAdapter.submitList(it)
            categoryVideoAdapter.notifyDataSetChanged()
        }
        categoryListUpper.observe(viewLifecycleOwner) {
            categoryToSpinnerUpperList.addAll(it)
            setupSpinner()
        }
        channelList.observe(viewLifecycleOwner) {
            categoryChannelAdapter.submitList(it)
            categoryChannelAdapter.notifyDataSetChanged()
        }
        homeError.observe(viewLifecycleOwner) {
            if(!it) {
                Toast.makeText(context,"API 연동 Error 입니다.",Toast.LENGTH_SHORT).show()
            }
        }
    }

    //ViewModel의 PopularVideo, Category, Category 별 Video를 받아오는 Api 연동 함수 실행
    private fun setBanner()= with(binding) {
        //홈배너 어댑터 설정
        val adapter = bannerAdapter
        rvHomeBanner.adapter = adapter
        //홈배너 인디케이터 설정
        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(rvHomeBanner)

        val indicator: CircleIndicator2 = requireView().findViewById(com.example.android_teammaniacs_project.R.id.indicator)
        indicator.attachToRecyclerView(rvHomeBanner, pagerSnapHelper)
        adapter.registerAdapterDataObserver(indicator.adapterDataObserver)

        val handler = Handler()

        val scrollRunnable = object : Runnable {
            override fun run() {
                val currentPosition = (rvHomeBanner.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                val nextPosition = if (currentPosition < adapter.itemCount - 1) currentPosition + 1 else 0
                rvHomeBanner.smoothScrollToPosition(nextPosition)
                handler.postDelayed(this, 3000) // 3초마다 실행
            }
        }

        handler.postDelayed(scrollRunnable, 3000) // 초기 실행

        //Banner Setting 및 Category Setting
        viewModel.setBanner(key, part, chart, maxResultsPopular)
        viewModel.getCategory(key, part, regionCode)
    }

    //Spinner 세팅 및 Spinner에 Category들 추가
    private fun setupSpinner()= with(binding){
        val arraySpinnerUpper = categoryToSpinnerUpperList.map {it.title}.toTypedArray()

        val spinnerAdapterUpper: ArrayAdapter<String> = ArrayAdapter<String>(
            contexts,
            R.layout.home_spinner_item, // 스피너 아이템 레이아웃
            arraySpinnerUpper
        )

        spinnerAdapterUpper.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        homeSpinner.adapter = spinnerAdapterUpper

        // 스피너 아이템 선택 리스너 설정
        homeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View?,
                position: Int,
                id: Long,
            ) {
                val selectedCategory = arraySpinnerUpper[position]
                //카테고리 별 영상
                for(i in categoryToSpinnerUpperList) {
                    if (selectedCategory == i.title) {
                        viewLocation = "upper"
                        viewModel.getCategoryVideo(key,part,chart,maxResults,i.id,viewLocation)
                    }
                }

                //카테고리 별 채널
                viewModel.getChannel(key,part,maxResults,order,selectedCategory,regionCode,type)
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {

            }
        }

    }

    //intent adapter
    private fun startVideoDetailActivity(position: Int, video: Video) {
        val intent = Intent(context, VideoDetailActivity::class.java)
        intent.apply {
            putExtra(HOME_VIDEO_POSITION, position)
            putExtra(HOME_VIDEO_MODEL, video)
        }
        startActivity(intent)
        activity?.overridePendingTransition(R.drawable.fade_in, R.drawable.fade_out)
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}