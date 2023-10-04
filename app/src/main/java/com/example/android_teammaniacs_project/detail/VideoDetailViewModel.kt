package com.example.android_teammaniacs_project.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android_teammaniacs_project.data.Video
import com.example.android_teammaniacs_project.retrofit.ChannelModel
import com.example.android_teammaniacs_project.retrofit.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VideoDetailViewModel(private val apiService : RetrofitInterface) : ViewModel(){

    private val _category: MutableLiveData<String> = MutableLiveData()
    val category: LiveData<String> get() = _category

    fun getChannelImage(key: String, part: String, channelId: String, maxResults: Int) {
        apiService.getChannel(key,part,channelId,maxResults)
            ?.enqueue(object : Callback<ChannelModel>{
                override fun onResponse(
                    call: Call<ChannelModel>,
                    response: Response<ChannelModel>
                ) {
                    for(i in response.body()?.items!!) {
                        _category.value = i.snippet.thumbnails.high.url
                    }
                }

                override fun onFailure(call: Call<ChannelModel>, t: Throwable) {

                }

            })
    }
}