package com.example.android_teammaniacs_project.myVideoPage

import ProfileDialog
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.android_teammaniacs_project.R
import com.example.android_teammaniacs_project.constants.Constants
import com.example.android_teammaniacs_project.data.Video
import com.example.android_teammaniacs_project.databinding.MyVideoFragmentBinding
import com.example.android_teammaniacs_project.detail.VideoDetailActivity
import com.google.gson.Gson

class MyVideoFragment : Fragment() {

    private var _binding: MyVideoFragmentBinding? = null
    private val binding get() = _binding!!
    private var context: Context? = null

    companion object {
        fun newInstance() = MyVideoFragment()
        val MY_VIDEO_POSITION = "my_video_position"
        val MY_VIDEO_MODEL = "my_video_model"
    }

    private val listAdapter by lazy {
        MyVideoAdapter { position, video ->
            val intent = Intent(context, VideoDetailActivity::class.java)
            intent.apply {
                putExtra(MY_VIDEO_POSITION, position)
                putExtra(MY_VIDEO_MODEL, video)
            }
            startActivity(intent)
            activity?.overridePendingTransition(R.drawable.fade_in, R.drawable.fade_out)
        }
    }

    private val viewModel: MyVideoViewModel by viewModels { MyVidelViewModelFactory() }

    override fun onAttach(_context: Context) {
        context = _context
        super.onAttach(_context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = MyVideoFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViweModel()
    }

    private fun initView() = with(binding) {
        rvVideo.adapter = listAdapter

        btnEdit.setOnClickListener {
            showProfileDialog()
        }

    }

    private fun initViweModel() = with(viewModel) {
        list.observe(viewLifecycleOwner) {
            listAdapter.submitList(it)
        }
        profileName.observe(viewLifecycleOwner){name ->
            binding.tvProfileName.text=name
        }
    }

    override fun onResume()= with(binding) {
        // 데이터 불러오기
        //현재는 알파벳순으로 데이터가 불러오고 있음
        val sharedPref =
            context?.getSharedPreferences(Constants.MY_VIDEOS_KEY, Context.MODE_PRIVATE)

        if (sharedPref != null) {
            val dataList = ArrayList<Video>()
            for (video in sharedPref.all) {
                try {
                    val savedData = sharedPref.getString(video.key, null)
                    if (savedData != null) {
                        val data = Gson(). fromJson(savedData, Video::class.java)
                        dataList.add(data)
                    }
                } catch (e: Exception) {
                    Log.e("MINJI", e.toString())
                }
            }
            viewModel.setData(dataList)
        }
        super.onResume()
    }

    //선택한 사진 불러오기
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("MyVideoFragment", "onActivityResult - requestCode: $requestCode, resultCode: $resultCode")

        if (requestCode == ProfileDialog.PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = data?.data
            Log.d("MyVideoFragment", "Selected Image Uri: $imageUri")

            viewModel.setProfileImage(imageUri)
            binding.ivProfile.setImageURI(imageUri)
        }
    }


    // 다이얼로그를 띄우는 코드
    private fun showProfileDialog() {
        ProfileDialog(
            requireContext(),
            viewModel.profileName.value, // 현재 프로필 이름을 전달
            viewModel.profileImageUri.value // 현재 프로필 이미지 Uri를 전달
        ) { newName, newImageUri ->
            viewModel.setProfile(newName, newImageUri) // 뷰모델에 업데이트 요청
        }.show()
    }



    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}