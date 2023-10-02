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
    var nextPageToken : String? = ""

    //검색 API 호출 함수
    fun searchVideo(key: String, part: String, maxResults: Int, order: String, q: String?, type: String) {
        apiService.getSearchList(key,part,maxResults, order, q, type, null)
            ?.enqueue(object : Callback<SearchVideoModel>{
                override fun onResponse(
                    call: Call<SearchVideoModel>,
                    response: Response<SearchVideoModel>
                ) {
                    resItems.clear()
                    nextPageToken = response.body()?.nextPageToken
                    Log.d("searchToken", nextPageToken.toString())
                    for(i in response.body()?.items!!) {
                        resItems.add(Video(i.snippet.thumbnails.high.url,i.snippet.title,i.snippet.channelId))
                    }
                    _searchResults.value = resItems
                    Log.d("returnToken", response.body()?.nextPageToken.toString())
                }

                override fun onFailure(call: Call<SearchVideoModel>, t: Throwable) {
                    Log.d("fail", t.toString())
                }

            })
    }

    fun searchVideoScrolled(key : String, part: String, maxResults: Int, order : String, q: String?, type: String) {
        apiService.getSearchList(key,part,maxResults,order,q,type,nextPageToken)
            ?.enqueue(object  : Callback<SearchVideoModel>{
                override fun onResponse(
                    call: Call<SearchVideoModel>,
                    response: Response<SearchVideoModel>
                ) {
                    resItems.clear()
                    for (i in response.body()?.items!!) {
                        resItems.add(Video(i.snippet.thumbnails.high.url, i.snippet.title, i.snippet.channelId))
                    }
                    Log.d("nextToken", nextPageToken.toString())
                    _searchResults.value = resItems
                    nextPageToken = response.body()?.nextPageToken
                    Log.d("scrollToken", nextPageToken.toString())
                }

                override fun onFailure(call: Call<SearchVideoModel>, t: Throwable) {

                }
            })
    }
}