package com.example.android_teammaniacs_project.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android_teammaniacs_project.data.Category
import com.example.android_teammaniacs_project.data.Channel
import com.example.android_teammaniacs_project.data.Video
import com.example.android_teammaniacs_project.retrofit.CategoryModel
import com.example.android_teammaniacs_project.retrofit.PopularVideoModel
import com.example.android_teammaniacs_project.retrofit.RetrofitInterface
import com.example.android_teammaniacs_project.retrofit.SearchVideoModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.NullPointerException
import java.util.concurrent.atomic.AtomicLong

class HomeViewModel(private val apiService: RetrofitInterface) : ViewModel() {

    //인기 영상을 위한 LiveData 선언
    private val _popularList: MutableLiveData<List<Video>> = MutableLiveData()
    val popularList: LiveData<List<Video>> get() = _popularList

    //카테고리별 영상을 위한 LiveData 선언
    private val _categoryUpperVideoList: MutableLiveData<List<Video>> = MutableLiveData()
    val categoryUpperVideoList: LiveData<List<Video>> get() = _categoryUpperVideoList

    //카테고리 목록을 받기 위한 LiveData 선언
    private val _categoryListUpper: MutableLiveData<List<Category>> = MutableLiveData()
    val categoryListUpper: LiveData<List<Category>> get() = _categoryListUpper

    //채널 데이터 전송을 위한 LiveData
    private val _channelList : MutableLiveData<List<Channel>> = MutableLiveData()
    val channelList : LiveData<List<Channel>> get() = _channelList

    //Exception 처리 LiveData
    private val _homeError : MutableLiveData<Boolean> = MutableLiveData()
    val homeError : LiveData<Boolean> get() = _homeError

    private val popularResultList = ArrayList<Video>()
    private val categoryVideoResultUpperList = ArrayList<Video>()
    private val categoryResultList = ArrayList<Category>()
    private val channelResultList = ArrayList<Channel>()

    private val idGenerate = AtomicLong(1L)

    //Popular Video 받는 API 연동 / 받은 후 Video List 에 데이터 추가
    fun setBanner(key: String, part: String, chart: String, maxResults: Int) {
        apiService.getVideoList(key, part, chart, maxResults)
            ?.enqueue(object : Callback<PopularVideoModel> {
                override fun onResponse(
                    call: Call<PopularVideoModel>,
                    response: Response<PopularVideoModel>
                ) {
                    try {
                        popularResultList.clear()
                        for (i in response.body()?.items!!) {
                            Log.d("channelid", i.snippet.channelId)
                            popularResultList.add(
                                Video(
                                    i.snippet.thumbnails.high.url,
                                    i.snippet.title,
                                    i.snippet.channelId,
                                    i.snippet.publishedAt,
                                    i.snippet.channelTitle,
                                    i.snippet.description
                                )
                            )
                        }
                        _popularList.value = popularResultList
                    } catch (e:NullPointerException) {
                        _homeError.value = false
                        Log.e("Error", e.message.toString())
                        _homeError.value = true
                    }
                }

                override fun onFailure(call: Call<PopularVideoModel>, t: Throwable) {

                }

            })
    }

    //카테고리 별 Video 받는 API 연동 / 받은 후 Video List 에 데이터 추가
    fun getCategoryVideo(
        key: String,
        part: String,
        chart: String,
        maxResults: Int,
        videoCategoryId: String,
        viewLocation : String
    ) {
        apiService.getCategoryVideoList(key, part, chart, maxResults, videoCategoryId)
            ?.enqueue(object : Callback<PopularVideoModel> {
                override fun onResponse(
                    call: Call<PopularVideoModel>,
                    response: Response<PopularVideoModel>
                ) {
                    try {
                        if (viewLocation == "upper") {
                            categoryVideoResultUpperList.clear()
                            for (i in response.body()?.items!!) {
                                categoryVideoResultUpperList.add(
                                    Video(
                                        i.snippet.thumbnails.high.url,
                                        i.snippet.title,
                                        i.snippet.channelId,
                                        i.snippet.publishedAt,
                                        i.snippet.channelTitle,
                                        i.snippet.description
                                    )
                                )
                            }
                            _categoryUpperVideoList.value = categoryVideoResultUpperList
                        }
                    } catch (e:NullPointerException) {
                        _homeError.value = false
                        Log.e("Error", e.message.toString())
                        _homeError.value = true
                    }

                }

                override fun onFailure(call: Call<PopularVideoModel>, t: Throwable) {

                }

            })
    }

    //카테고리 리스트 받는 API 연동 / 받은 후 List에 데이터 추가
    fun getCategory(key: String, part: String, regionCode: String) {
        apiService.getCategoryList(key, part, regionCode)
            ?.enqueue(object : Callback<CategoryModel> {
                override fun onResponse(
                    call: Call<CategoryModel>,
                    response: Response<CategoryModel>
                ) {
                    try {
                        for (i in response.body()?.items!!) {
                            if (i.snippet.assignable && i.id != "19") {
                                categoryResultList.add(
                                    Category(
                                        i.id,
                                        i.snippet.title
                                    )
                                )
                            }
                        }
                        _categoryListUpper.value = categoryResultList
                    } catch (e:NullPointerException) {
                        _homeError.value = false
                        Log.e("Error", e.message.toString())
                        _homeError.value = true
                    }
                }

                override fun onFailure(call: Call<CategoryModel>, t: Throwable) {

                }

            })
    }

    //Chanel List API 연동
    fun getChannel(
        key: String,
        part: String,
        maxResults: Int,
        order: String,
        selectedCategory: String,
        regionCode: String,
        type: String
    ) {
        apiService.getChannelList(key,part,maxResults,order,selectedCategory,regionCode,type)
            ?.enqueue(object : Callback<SearchVideoModel>{
                override fun onResponse(
                    call: Call<SearchVideoModel>,
                    response: Response<SearchVideoModel>
                ) {
                    try {
                        channelResultList.clear()
                        for (i in response.body()?.items!!) {
                            channelResultList.add(
                                Channel(i.snippet.thumbnails.high.url, i.snippet.channelTitle)
                            )
                        }
                        _channelList.value = channelResultList
                    } catch (e:NullPointerException) {
                        _homeError.value = false
                        Log.e("Error", e.message.toString())
                        _homeError.value = true
                    }
                }

                override fun onFailure(call: Call<SearchVideoModel>, t: Throwable) {

                }

            })
    }


}
