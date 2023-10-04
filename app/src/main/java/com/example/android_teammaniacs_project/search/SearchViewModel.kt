package com.example.android_teammaniacs_project.search

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android_teammaniacs_project.data.Video
import com.example.android_teammaniacs_project.retrofit.RetrofitInterface
import com.example.android_teammaniacs_project.retrofit.SearchVideoModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.lang.NullPointerException

class SearchViewModel(private val apiService: RetrofitInterface) : ViewModel() {

    private val _searchResults = MutableLiveData<List<Video>>()
    val searchResults: LiveData<List<Video>> get() = _searchResults
    var resItems: ArrayList<Video> = ArrayList()

    //nextPageToken
    var receivedNextPageToken : String? = ""

    //Scroll 동작을 위한 Boolean Live Data
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _searchError : MutableLiveData<Boolean> = MutableLiveData()
    val searchError : LiveData<Boolean> get() = _searchError

    //검색 API 호출 함수
    fun searchVideo(key: String, part: String, maxResults: Int, order: String, q: String?, type: String) {
        apiService.getSearchList(key,part,maxResults, order, q, type, null)
            ?.enqueue(object : Callback<SearchVideoModel>{
                override fun onResponse(
                    call: Call<SearchVideoModel>,
                    response: Response<SearchVideoModel>
                ) {
                    try {
                        resItems.clear()
                        receivedNextPageToken = response.body()?.nextPageToken
                        Log.d("searchToken", receivedNextPageToken.toString())
                        for (i in response.body()?.items!!) {
                            resItems.add(
                                Video(
                                    i.snippet.thumbnails.high.url, i.snippet.title,
                                    i.snippet.channelId,
                                    i.snippet.publishedAt,
                                    i.snippet.channelTitle,
                                    i.snippet.description
                                )
                            )
                        }
                        _searchResults.value = resItems
                    } catch (e: NullPointerException) {
                        _searchError.value = false
                        Log.e("Error", e.message.toString())
                        _searchError.value = true
                    }
                }

                override fun onFailure(call: Call<SearchVideoModel>, t: Throwable) {
                    Log.d("fail", t.toString())
                }

            })
    }

    fun searchVideoScrolled(key : String, part: String, maxResults: Int, order : String, q: String?, type: String) {
        apiService.getSearchList(key,part,maxResults,order,q,type,receivedNextPageToken)
            ?.enqueue(object  : Callback<SearchVideoModel>{
                override fun onResponse(
                    call: Call<SearchVideoModel>,
                    response: Response<SearchVideoModel>
                ) {
                    try {
                        receivedNextPageToken = response.body()?.nextPageToken
                        for (i in response.body()?.items!!) {
                            resItems.add(
                                Video(
                                    i.snippet.thumbnails.high.url, i.snippet.title,
                                    i.snippet.channelId,
                                    i.snippet.publishedAt,
                                    i.snippet.channelTitle,
                                    i.snippet.description
                                )
                            )
                        }
                        _searchResults.value = resItems
                        _isLoading.value = true
                    } catch (e: NullPointerException) {
                        _searchError.value = false
                        Log.e("Error", e.message.toString())
                        _searchError.value = true
                    }
                }

                override fun onFailure(call: Call<SearchVideoModel>, t: Throwable) {

                }
            })
    }
}