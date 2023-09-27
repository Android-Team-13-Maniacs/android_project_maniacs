package com.example.android_teammaniacs_project.myVideoPage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android_teammaniacs_project.data.Video
import java.util.concurrent.atomic.AtomicLong

class MyVideoViewModel : ViewModel() {

    private val _list: MutableLiveData<List<Video>> = MutableLiveData()
    val list: LiveData<List<Video>> get() = _list

    //live= 액티비티나 프래그먼트에서 읽기 전용
    //_live= 뷰모델 내부에서 컨트롤 전용
    // id 를 부여할 값
    private val idGenerate = AtomicLong(1L)

    init {
//        //test
//        _list.value = arrayListOf<Video>().apply {
//            for (i in 1..10) {
//                add(
//                    Video(null, "title$i", null)
//                )
//            }
//        }
    }

}
