package com.example.android_teammaniacs_project

import android.R
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.android_teammaniacs_project.adapters.HomeBannerAdapter
import com.example.android_teammaniacs_project.adapters.HomeVideoAdapter
import com.example.android_teammaniacs_project.data.Video
import com.example.android_teammaniacs_project.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private lateinit var contexts: Context
    private var binding: FragmentHomeBinding? = null
    private lateinit var adapter: HomeVideoAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contexts = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupRecyclerView()
        return binding!!.root
    }

    private fun setupRecyclerView() {
        //임의로 uri 넣어둠
        val testData = mutableListOf<Video>()
        testData.add(
            Video(
                Uri.parse("https://search.pstatic.net/common/?src=http%3A%2F%2Fimgnews.naver.net%2Fimage%2F241%2F2022%2F07%2F01%2F0003218781_001_20220701115801485.jpg&type=sc960_832"),
                "STAYC",
                "https://search.pstatic.net/common/?src=http%3A%2F%2Fimgnews.naver.net%2Fimage%2F241%2F2022%2F07%2F01%2F0003218781_001_20220701115801485.jpg&type=sc960_832"
            )
        )
        testData.add(
            Video(
                Uri.parse("https://search.pstatic.net/sunny/?src=https%3A%2F%2Fimg.theqoo.net%2Fimg%2FVFgJV.png&type=a340"),
                "NewJeans",
                "https://search.pstatic.net/sunny/?src=https%3A%2F%2Fimg.theqoo.net%2Fimg%2FVFgJV.png&type=a340"
            )
        )
        testData.add(
            Video(
                Uri.parse("https://search.pstatic.net/common/?src=http%3A%2F%2Fimgnews.naver.net%2Fimage%2F5095%2F2022%2F07%2F09%2F0000974046_001_20220709084001248.jpg&type=sc960_832"),
                "aespa",
                "https://search.pstatic.net/common/?src=http%3A%2F%2Fimgnews.naver.net%2Fimage%2F5095%2F2022%2F07%2F09%2F0000974046_001_20220709084001248.jpg&type=sc960_832"
            )
        )

        val adapter = HomeBannerAdapter(contexts)
        adapter.items = testData
        adapter.notifyDataSetChanged()
        binding?.rvHomeBanner?.adapter = adapter


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
//        adapter.viewre setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        s?.adapter = spinnerAdapter

        val adapter2 = HomeVideoAdapter(contexts)
        adapter2.items = testData
        adapter2.notifyDataSetChanged()
        binding?.rvHomeSection1?.adapter = adapter2

        val adapter3 = HomeVideoAdapter(contexts)
        adapter3.items = testData
        adapter3.notifyDataSetChanged()
        binding?.rvHomeSection2?.adapter = adapter3
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}