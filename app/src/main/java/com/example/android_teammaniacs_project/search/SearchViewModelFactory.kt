package com.example.android_teammaniacs_project.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android_teammaniacs_project.retrofit.RetrofitInterface

class SearchViewModelFactory(private val apiService : RetrofitInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(apiService) as T
    }
}