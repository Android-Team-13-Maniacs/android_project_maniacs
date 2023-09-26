package com.example.android_teammaniacs_project.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.android_teammaniacs_project.databinding.VideoDetailActivityBinding


class VideoDetailActivity : AppCompatActivity() {
    private lateinit var binding: VideoDetailActivityBinding

    private val recyclerView by lazy {
        CommentListAdapter()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=VideoDetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

        val shareButton = findViewById<Button>(com.example.android_teammaniacs_project.R.id.btn_Share)


        shareButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "video/*"

            // String으로 받아서 넣기
            val sendMessage = "이렇게 스트링으로 만들어서 넣어주면 됩니다."
            intent.putExtra(Intent.EXTRA_TEXT, sendMessage)
            val shareIntent = Intent.createChooser(intent, "share")
            startActivity(shareIntent)
        }
    }

    private fun initView() = with(binding) {
        //recycler view
        commentList.adapter=recyclerView

        //test data
        val list = ArrayList<CommentModel>()
        for ( i in 0..3) {
            list.add(CommentModel(0, com.example.android_teammaniacs_project.R.drawable.dog,"$i name","$i date","$i coment"))
        }
        recyclerView.addItems(list)
    }


    private fun showShareSheet(text: String, imageUri: Uri) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "video/*"
        intent.putExtra(Intent.EXTRA_TEXT, text)
        intent.putExtra(Intent.EXTRA_STREAM, imageUri)

        val shareIntent = Intent.createChooser(intent, null)
        startActivity(shareIntent)
    }

}