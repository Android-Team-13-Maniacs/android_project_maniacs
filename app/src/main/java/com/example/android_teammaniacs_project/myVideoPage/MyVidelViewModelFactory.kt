package com.example.android_teammaniacs_project.myVideoPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MyVidelViewModelFactory : ViewModelProvider.Factory {

    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MyVideoViewModel::class.java)){
            return MyVideoViewModel() as T
        }
        throw IllegalArgumentException("Not Found ViewModel class.")
    }
}