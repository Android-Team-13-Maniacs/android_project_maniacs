package com.example.android_teammaniacs_project.myVideoPage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.android_teammaniacs_project.R
import com.example.android_teammaniacs_project.data.Video
import com.example.android_teammaniacs_project.databinding.MyVideoFragmentBinding
import com.example.android_teammaniacs_project.detail.VideoDetailActivity

class MyVideoFragment : Fragment() {

    private var _binding : MyVideoFragmentBinding? = null
    private val binding get() = _binding!!
    private var videoList = ArrayList<Video>()

    companion object {
        fun newInstance() = MyVideoFragment()
        val MY_VIDEO_POSITION = "my_video_position"
        val MY_VIDEO_MODEL = "my_video_model"
    }

    private val listAdapter by lazy {
        MyVideoAdapter { position, video ->
            val intent = Intent(context, VideoDetailActivity::class.java)
            intent.apply {
                putExtra(MY_VIDEO_POSITION,position)
                putExtra(MY_VIDEO_MODEL,video)
            }
            startActivity(intent)
            activity?.overridePendingTransition(R.drawable.fade_in, R.drawable.fade_out)
        }
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
        rvVideo.adapter = listAdapter
        val gridManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rvVideo.layoutManager = gridManager
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}