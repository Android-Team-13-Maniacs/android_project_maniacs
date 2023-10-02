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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
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
    private val categoryToSpinnerBelowList = ArrayList<Category>()

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

    //API 연동을 위해 입력할 값들 정의
    private val key = GoogleKey.KEY
    private val part = "snippet"
    private val chart = "mostPopular"
    private val maxResults = 20
    private val videoCategoryId = "1"
    private val regionCode = "KR"
    private var viewLocation = "upper"

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
        val adapter1 = section1Adapter
        rvHomeSection1.adapter = adapter1

        val adapter2 = section2Adapter
        rvHomeSection2.adapter = adapter2
    }

    //live data를 받아와서 RecyclerView Adapter에 데이터 전달
    private fun initViewModel() = with(viewModel) {
        popularList.observe(viewLifecycleOwner) {
            bannerAdapter.submitList(it)
        }
        categoryUpperVideoList.observe(viewLifecycleOwner) {
            section1Adapter.submitList(it)
            section1Adapter.notifyDataSetChanged()
        }
        categoryBelowVideoList.observe(viewLifecycleOwner) {
            section2Adapter.submitList(it)
            section2Adapter.notifyDataSetChanged()
        }
        categoryListUpper.observe(viewLifecycleOwner) {
            categoryToSpinnerUpperList.addAll(it)
            setupSpinner()
        }
        categoryListBelow.observe(viewLifecycleOwner) {
            categoryToSpinnerBelowList.addAll(it)
            setupSpinner()
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
        viewModel.setBanner(key, part, chart, maxResults)
        viewModel.getCategory(key, part, regionCode)
    }

    //Spinner 세팅 및 Spinner에 Category들 추가
    private fun setupSpinner()= with(binding){
        val arraySpinnerUpper = categoryToSpinnerUpperList.map {it.title}.toTypedArray()
        val arraySpinnerBelow = categoryToSpinnerBelowList.map {it.title}.toTypedArray()

        val spinnerAdapterUpper: ArrayAdapter<String> = ArrayAdapter<String>(
            contexts,
            com.example.android_teammaniacs_project.R.layout.home_spinner_item, // 스피너 아이템 레이아웃
            arraySpinnerUpper
        )

        val spinnerAdapterBelow: ArrayAdapter<String> = ArrayAdapter<String>(
            contexts,
            com.example.android_teammaniacs_project.R.layout.home_spinner_item, // 스피너 아이템 레이아웃
            arraySpinnerBelow
        )
        spinnerAdapterUpper.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAdapterBelow.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        homeSpinner.adapter = spinnerAdapterUpper
        homeSpinner2.adapter = spinnerAdapterBelow

        // 스피너 아이템 선택 리스너 설정
        homeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View?,
                position: Int,
                id: Long,
            ) {
                val selectedCategory = arraySpinnerUpper[position]
                for(i in categoryToSpinnerUpperList) {
                    if (selectedCategory == i.title) {
                        viewLocation = "upper"
                        viewModel.getCategoryVideo(key,part,chart,maxResults,i.id,viewLocation)
                    }
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {

            }
        }
        homeSpinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View?,
                position: Int,
                id: Long,
            ) {
                val selectedCategory = arraySpinnerBelow[position]
                for(i in categoryToSpinnerBelowList) {
                    if (selectedCategory == i.title) {
                        viewLocation = "below"
                        viewModel.getCategoryVideo(key,part,chart,maxResults,i.id,viewLocation)
                    }
                }
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
        activity?.overridePendingTransition(com.example.android_teammaniacs_project.R.drawable.fade_in, com.example.android_teammaniacs_project.R.drawable.fade_out)
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}