package com.example.android_teammaniacs_project.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android_teammaniacs_project.data.Video
import com.example.android_teammaniacs_project.retrofit.RetrofitInterface
import com.example.android_teammaniacs_project.retrofit.SearchVideoModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel(private val apiService: RetrofitInterface) : ViewModel() {

    private val _searchResults = MutableLiveData<List<Video>>()
    val searchResults: LiveData<List<Video>> get() = _searchResults
    var resItems: ArrayList<Video> = ArrayList()

    fun searchView(key: String, part: String, maxResults: Int, order: String, q: String?, type: String) {
        apiService.getSearchList(key,part,maxResults, order, q, type)
            ?.enqueue(object : Callback<SearchVideoModel>{
                override fun onResponse(
                    call: Call<SearchVideoModel>,
                    response: Response<SearchVideoModel>
                ) {
                    resItems.clear()
                    for(i in response.body()?.items!!) {
                        resItems.add(Video(i.snippet.thumbnails.high.url,i.snippet.title,i.snippet.channelId))
                    }
                    _searchResults.value = resItems
                }

                override fun onFailure(call: Call<SearchVideoModel>, t: Throwable) {
                    Log.d("fail", t.toString())
                }

            })
    }
}