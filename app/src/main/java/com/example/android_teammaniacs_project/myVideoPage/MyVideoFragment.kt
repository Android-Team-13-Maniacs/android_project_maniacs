package com.example.android_teammaniacs_project.myVideoPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.android_teammaniacs_project.Test3Fragment
import com.example.android_teammaniacs_project.data.Video
import com.example.android_teammaniacs_project.databinding.MyVideoFragmentBinding

class MyVideoFragment : Fragment() {

    private var _binding : MyVideoFragmentBinding? = null
    private val binding get() = _binding!!
    private var videoList = ArrayList<Video>()
    private val listAdapter by lazy {
        MyVideoAdapter()
    }
    companion object {
        fun newInstance() = MyVideoFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  MyVideoFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDemoData()
        initView()
    }

    private fun setDemoData() {
        for(i in 1..10) {
            videoList.add(Video(null, "title$i", null))
        }
        listAdapter.addItems(videoList)
    }

    private fun initView() = with(binding) {
        videoRecyclerView.adapter = listAdapter
        val gridManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        videoRecyclerView.layoutManager = gridManager
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}