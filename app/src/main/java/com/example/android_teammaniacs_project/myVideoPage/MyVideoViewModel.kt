package com.example.android_teammaniacs_project.myVideoPage

import android.content.Context
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
//    private val sharedPreferences =
//        context.getSharedPreferences("MyVideoPreferences", Context.MODE_PRIVATE)

    //live= 액티비티나 프래그먼트에서 읽기 전용
    //_live= 뷰모델 내부에서 컨트롤 전용
    // id 를 부여할 값
    private val idGenerate = AtomicLong(1L)

//    init {
//        // 앱이 시작될 때 SharedPreferences에서 프로필 이름을 불러온다.
//        _profileName.value = sharedPreferences.getString("profileName", "기본 이름")
//    }

    fun setData(data: ArrayList<Video>) {
        _list.value = data
    }

    fun setProfile(name: String) {
        _profileName.value = name
//        sharedPreferences.edit().putString("profileName", name).apply()

    }

}
