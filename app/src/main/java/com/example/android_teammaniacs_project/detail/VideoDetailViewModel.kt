package com.example.android_teammaniacs_project.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android_teammaniacs_project.retrofit.ChannelModel
import com.example.android_teammaniacs_project.retrofit.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.NullPointerException

class VideoDetailViewModel(private val apiService : RetrofitInterface) : ViewModel(){

    private val _category: MutableLiveData<String> = MutableLiveData()
    val category: LiveData<String> get() = _category
    private val _detailError : MutableLiveData<Boolean> = MutableLiveData()
    val detailError : LiveData<Boolean> get() = _detailError

    fun getChannelImage(key: String, part: String, channelId: String, maxResults: Int) {
        apiService.getChannel(key,part,channelId,maxResults)
            ?.enqueue(object : Callback<ChannelModel>{
                override fun onResponse(
                    call: Call<ChannelModel>,
                    response: Response<ChannelModel>
                ) {
                    try {
                        for (i in response.body()?.items!!) {
                            _category.value = i.snippet.thumbnails.high.url
                        }
                    } catch (e: NullPointerException) {
                        _detailError.value = false
                        Log.e("Error", e.message.toString())
                        _detailError.value = true
                    }
                }

                override fun onFailure(call: Call<ChannelModel>, t: Throwable) {

                }

            })
    }
}