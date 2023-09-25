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
                Uri.parse("https://th.bing.com/th/id/R.be910db951bafca7bbf5cd2c84600731?rik=aauxFHegqo3a5w&riu=http%3a%2f%2fstatic3.businessinsider.com%2fimage%2f5484d9d1eab8ea3017b17e29%2f9-science-backed-reasons-to-own-a-dog.jpg&ehk=xgoJIFryMSERKzmXor2k3oOtE1fWtrgYC7i0yek0fQU%3d&risl=&pid=ImgRaw&r=0"),
                "강아지는 귀엽다",
                "https://th.bing.com/th/id/R.be910db951bafca7bbf5cd2c84600731?rik=aauxFHegqo3a5w&riu=http%3a%2f%2fstatic3.businessinsider.com%2fimage%2f5484d9d1eab8ea3017b17e29%2f9-science-backed-reasons-to-own-a-dog.jpg&ehk=xgoJIFryMSERKzmXor2k3oOtE1fWtrgYC7i0yek0fQU%3d&risl=&pid=ImgRaw&r=0"
            )
        )
        testData.add(
            Video(
                Uri.parse("https://img.freepik.com/premium-vector/cute-dog-cartoon-illustration_569774-126.jpg"),
                "강아지는 귀엽다",
                "https://img.freepik.com/premium-vector/cute-dog-cartoon-illustration_569774-126.jpg"
            )
        )
        testData.add(
            Video(
                Uri.parse("https://img.freepik.com/premium-vector/cute-dog-cartoon-illustration_569774-126.jpg"),
                "강아지는 귀엽다",
                "https://img.freepik.com/premium-vector/cute-dog-cartoon-illustration_569774-126.jpg"
            )
        )

        val adapter = HomeBannerAdapter(contexts)
        adapter.items = testData
        adapter.notifyDataSetChanged()
        binding?.homeBannerSectionRecyclerview?.adapter = adapter

        val arraySpinner = arrayOf(
            "1", "2", "3", "4", "5", "6", "7"
        )
        val s = binding?.homeSectionSpinner
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
        binding?.homeSection1Recyclerview?.adapter = adapter2

        val adapter3 = HomeVideoAdapter(contexts)
        adapter3.items = testData
        adapter3.notifyDataSetChanged()
        binding?.homeSection2Recyclerview?.adapter = adapter3
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}