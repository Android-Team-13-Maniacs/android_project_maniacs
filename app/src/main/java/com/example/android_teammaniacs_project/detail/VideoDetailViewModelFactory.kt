package com.example.android_teammaniacs_project.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android_teammaniacs_project.retrofit.RetrofitInterface

class VideoDetailViewModelFactory(private val apiService: RetrofitInterface) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VideoDetailViewModel::class.java)) {
            return VideoDetailViewModel(apiService) as T
        }
        throw IllegalArgumentException("Not Found ViewModel class.")
    }
}