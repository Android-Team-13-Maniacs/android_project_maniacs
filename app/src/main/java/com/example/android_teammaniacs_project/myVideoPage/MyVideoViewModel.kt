package com.example.android_teammaniacs_project.myVideoPage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android_teammaniacs_project.data.Video
import java.util.concurrent.atomic.AtomicLong

class MyVideoViewModel : ViewModel() {

    private val _list: MutableLiveData<ArrayList<Video>> = MutableLiveData()
    val list: LiveData<ArrayList<Video>> get() = _list
    private val _profileName = MutableLiveData<String>()
    val profileName: LiveData<String> get() = _profileName

    //live= 액티비티나 프래그먼트에서 읽기 전용
    //_live= 뷰모델 내부에서 컨트롤 전용
    // id 를 부여할 값
    private val idGenerate = AtomicLong(1L)

    fun setData(data: ArrayList<Video>) {
        _list.value = data
    }

    fun setProfile(name: String) {
        _profileName.value = name

    }

}
