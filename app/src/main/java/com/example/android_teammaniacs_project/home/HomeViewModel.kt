package com.example.android_teammaniacs_project.home

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android_teammaniacs_project.data.Video
import java.util.concurrent.atomic.AtomicLong

class HomeViewModel : ViewModel() {

    private val _list: MutableLiveData<List<Video>> = MutableLiveData()
    val list: LiveData<List<Video>> get() = _list

    //live= 액티비티나 프래그먼트에서 읽기 전용
    //_live= 뷰모델 내부에서 컨트롤 전용
    // id 를 부여할 값
    private val idGenerate = AtomicLong(1L)

    init {
        //test
        _list.value = arrayListOf<Video>().apply {
            add(
            Video(
                Uri.parse("https://search.pstatic.net/common/?src=http%3A%2F%2Fimgnews.naver.net%2Fimage%2F241%2F2022%2F07%2F01%2F0003218781_001_20220701115801485.jpg&type=sc960_832"),
                "STAYC",
                "https://search.pstatic.net/common/?src=http%3A%2F%2Fimgnews.naver.net%2Fimage%2F241%2F2022%2F07%2F01%2F0003218781_001_20220701115801485.jpg&type=sc960_832"
            )
        )
        add(
            Video(
                Uri.parse("https://search.pstatic.net/sunny/?src=https%3A%2F%2Fimg.theqoo.net%2Fimg%2FVFgJV.png&type=a340"),
                "NewJeans",
                "https://search.pstatic.net/sunny/?src=https%3A%2F%2Fimg.theqoo.net%2Fimg%2FVFgJV.png&type=a340"
            )
        )
        add(
            Video(
                Uri.parse("https://search.pstatic.net/common/?src=http%3A%2F%2Fimgnews.naver.net%2Fimage%2F5095%2F2022%2F07%2F09%2F0000974046_001_20220709084001248.jpg&type=sc960_832"),
                "aespa",
                "https://search.pstatic.net/common/?src=http%3A%2F%2Fimgnews.naver.net%2Fimage%2F5095%2F2022%2F07%2F09%2F0000974046_001_20220709084001248.jpg&type=sc960_832"
            )
        )
        }
    }

//    fun generateTestData(): List<Video> {
//        testData.add(
//            Video(
//                Uri.parse("https://search.pstatic.net/common/?src=http%3A%2F%2Fimgnews.naver.net%2Fimage%2F241%2F2022%2F07%2F01%2F0003218781_001_20220701115801485.jpg&type=sc960_832"),
//                "STAYC",
//                "https://search.pstatic.net/common/?src=http%3A%2F%2Fimgnews.naver.net%2Fimage%2F241%2F2022%2F07%2F01%2F0003218781_001_20220701115801485.jpg&type=sc960_832"
//            )
//        )
//        testData.add(
//            Video(
//                Uri.parse("https://search.pstatic.net/sunny/?src=https%3A%2F%2Fimg.theqoo.net%2Fimg%2FVFgJV.png&type=a340"),
//                "NewJeans",
//                "https://search.pstatic.net/sunny/?src=https%3A%2F%2Fimg.theqoo.net%2Fimg%2FVFgJV.png&type=a340"
//            )
//        )
//        testData.add(
//            Video(
//                Uri.parse("https://search.pstatic.net/common/?src=http%3A%2F%2Fimgnews.naver.net%2Fimage%2F5095%2F2022%2F07%2F09%2F0000974046_001_20220709084001248.jpg&type=sc960_832"),
//                "aespa",
//                "https://search.pstatic.net/common/?src=http%3A%2F%2Fimgnews.naver.net%2Fimage%2F5095%2F2022%2F07%2F09%2F0000974046_001_20220709084001248.jpg&type=sc960_832"
//            )
//        )
//        return testData
//    }
}

