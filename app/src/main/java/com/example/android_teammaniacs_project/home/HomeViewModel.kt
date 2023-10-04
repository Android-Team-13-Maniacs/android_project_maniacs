package com.example.android_teammaniacs_project.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android_teammaniacs_project.data.Category
import com.example.android_teammaniacs_project.data.Channel
import com.example.android_teammaniacs_project.data.Video
import com.example.android_teammaniacs_project.retrofit.CategoryModel
import com.example.android_teammaniacs_project.retrofit.ChannelModel
import com.example.android_teammaniacs_project.retrofit.PopularVideoModel
import com.example.android_teammaniacs_project.retrofit.RetrofitInterface
import com.example.android_teammaniacs_project.retrofit.SearchVideoModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.atomic.AtomicLong

class HomeViewModel(private val apiService: RetrofitInterface) : ViewModel() {

    //인기 영상을 위한 LiveData 선언
    private val _popularList: MutableLiveData<List<Video>> = MutableLiveData()
    val popularList: LiveData<List<Video>> get() = _popularList

    //카테고리별 영상을 위한 LiveData 선언
    private val _categoryUpperVideoList: MutableLiveData<List<Video>> = MutableLiveData()
    val categoryUpperVideoList: LiveData<List<Video>> get() = _categoryUpperVideoList

    private val _categoryBelowVideoList: MutableLiveData<List<Video>> = MutableLiveData()
    val categoryBelowVideoList: LiveData<List<Video>> get() = _categoryBelowVideoList

    //카테고리 목록을 받기 위한 LiveData 선언
    private val _categoryListUpper: MutableLiveData<List<Category>> = MutableLiveData()
    val categoryListUpper: LiveData<List<Category>> get() = _categoryListUpper

    private val _categoryListBelow: MutableLiveData<List<Category>> = MutableLiveData()
    val categoryListBelow: LiveData<List<Category>> get() = _categoryListBelow

    //채널 아이디 수집을 위한 LiveData 선언
    private val _channelId : MutableLiveData<List<String>> = MutableLiveData()
    val channelId : LiveData<List<String>> get() = _channelId

    //채널 데이터 전송을 위한 LiveData
    private val _channelList : MutableLiveData<List<Channel>> = MutableLiveData()
    val channelList : LiveData<List<Channel>> get() = _channelList

    private val popularResultList = ArrayList<Video>()
    private val categoryVideoResultUpperList = ArrayList<Video>()
    private val categoryResultList = ArrayList<Category>()
    private val channelIdArray = ArrayList<String>()
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
                    if(viewLocation == "upper") {
                        categoryVideoResultUpperList.clear()
                        channelIdArray.clear()
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
                            channelIdArray.add(i.snippet.channelId)
                        }
                        _categoryUpperVideoList.value = categoryVideoResultUpperList
                        _channelId.value = channelIdArray
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
                    for (i in response.body()?.items!!) {
                        if (i.snippet.assignable && i.id != "27" && i.id !="19") {
                            categoryResultList.add(
                                Category(
                                    i.id,
                                    i.snippet.title
                                )
                            )
                        }
                    }
                    _categoryListUpper.value = categoryResultList
                }

                override fun onFailure(call: Call<CategoryModel>, t: Throwable) {

                }

            })
    }

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
                    channelResultList.clear()
                    for(i in response.body()?.items!!) {
                        channelResultList.add(
                            Channel(i.snippet.thumbnails.high.url, i.snippet.channelTitle)
                        )
                    }
                    _channelList.value = channelResultList
                }

                override fun onFailure(call: Call<SearchVideoModel>, t: Throwable) {

                }

            })
    }


}
