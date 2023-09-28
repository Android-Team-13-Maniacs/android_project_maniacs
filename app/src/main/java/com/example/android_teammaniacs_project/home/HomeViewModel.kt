package com.example.android_teammaniacs_project.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android_teammaniacs_project.data.Category
import com.example.android_teammaniacs_project.data.Video
import com.example.android_teammaniacs_project.retrofit.CategoryItem
import com.example.android_teammaniacs_project.retrofit.CategoryModel
import com.example.android_teammaniacs_project.retrofit.PopularVideoModel
import com.example.android_teammaniacs_project.retrofit.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.atomic.AtomicLong

class HomeViewModel(private val apiService: RetrofitInterface) : ViewModel() {

    private val _list: MutableLiveData<List<Video>> = MutableLiveData()
    val list: LiveData<List<Video>> get() = _list

    //인기 영상을 위한 LiveData 선언
    private val _popularList: MutableLiveData<List<Video>> = MutableLiveData()
    val popularList: LiveData<List<Video>> get() = _popularList

    //카테고리별 영상을 위한 LiveData 선언
    private val _categoryVideoList: MutableLiveData<List<Video>> = MutableLiveData()
    val categoryVideoList: LiveData<List<Video>> get() = _categoryVideoList

    //카테고리 목록을 받기 위한 LiveData 선언
    private val _categoryList: MutableLiveData<List<Category>> = MutableLiveData()
    val categoryList: LiveData<List<Category>> get() = _categoryList

    private val popularResultList = ArrayList<Video>()
    private val categoryVideoResultList = ArrayList<Video>()
    private val categoryResultList = ArrayList<Category>()

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
                        popularResultList.add(
                            Video(
                                i.snippet.thumbnails.high.url,
                                i.snippet.title,
                                i.snippet.channelId
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
        videoCategoryId: String
    ) {
        apiService.getCategoryVideoList(key, part, chart, maxResults, videoCategoryId)
            ?.enqueue(object : Callback<PopularVideoModel> {
                override fun onResponse(
                    call: Call<PopularVideoModel>,
                    response: Response<PopularVideoModel>
                ) {
                    categoryVideoResultList.clear()
                    for (i in response.body()?.items!!) {
                        categoryVideoResultList.add(
                            Video(
                                i.snippet.thumbnails.high.url,
                                i.snippet.title,
                                i.snippet.channelId
                            )
                        )
                    }
                    _categoryVideoList.value = categoryVideoResultList
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
                            Log.d("category", i.snippet.title+i.id)
                            categoryResultList.add(
                                Category(
                                    i.id,
                                    i.snippet.title
                                )
                            )
                        }
                    }
                    _categoryList.value = categoryResultList
                }

                override fun onFailure(call: Call<CategoryModel>, t: Throwable) {

                }

            })
    }

}
