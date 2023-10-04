package com.example.android_teammaniacs_project.myVideoPage

import android.net.Uri
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
    private val _profileImageUri = MutableLiveData<Uri?>() // 프로필 이미지 Uri를 저장하는 LiveData

    val profileImageUri: LiveData<Uri?> get() = _profileImageUri // 외부에서 프로필 이미지 Uri에 접근하는 LiveData


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

    fun setProfile(name: String, imageUri: Uri?) {
        _profileName.value = name
        _profileImageUri.value = imageUri
//        sharedPreferences.edit().putString("profileName", name).apply()

    }

    fun setProfileImage(imageUri: Uri?) {
        _profileImageUri.value = imageUri
    }

}
