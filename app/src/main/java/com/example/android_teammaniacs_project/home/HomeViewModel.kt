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

    private val _popularList: MutableLiveData<List<Video>> = MutableLiveData()
    val popularList: LiveData<List<Video>> get() = _popularList

    private val _categoryVideoList: MutableLiveData<List<Video>> = MutableLiveData()
    val categoryVideoList: LiveData<List<Video>> get() = _categoryVideoList

    private val _categoryList: MutableLiveData<List<CategoryItem>> = MutableLiveData()
    val categoryList: LiveData<List<CategoryItem>> get() = _categoryList



    private val popularResultList = ArrayList<Video>()
    private val categoryVideoResultList = ArrayList<Video>()

    private val idGenerate = AtomicLong(1L)

//    init {
//        //test
//        _list.value = arrayListOf<Video>().apply {
//            add(
//                Video(
//                    Uri.parse("https://search.pstatic.net/common/?src=http%3A%2F%2Fimgnews.naver.net%2Fimage%2F241%2F2022%2F07%2F01%2F0003218781_001_20220701115801485.jpg&type=sc960_832"),
//                    "STAYC",
//                    "https://search.pstatic.net/common/?src=http%3A%2F%2Fimgnews.naver.net%2Fimage%2F241%2F2022%2F07%2F01%2F0003218781_001_20220701115801485.jpg&type=sc960_832"
//                )
//            )
//            add(
//                Video(
//                    Uri.parse("https://search.pstatic.net/sunny/?src=https%3A%2F%2Fimg.theqoo.net%2Fimg%2FVFgJV.png&type=a340"),
//                    "NewJeans",
//                    "https://search.pstatic.net/sunny/?src=https%3A%2F%2Fimg.theqoo.net%2Fimg%2FVFgJV.png&type=a340"
//                )
//            )
//            add(
//                Video(
//                    Uri.parse("https://search.pstatic.net/common/?src=http%3A%2F%2Fimgnews.naver.net%2Fimage%2F5095%2F2022%2F07%2F09%2F0000974046_001_20220709084001248.jpg&type=sc960_832"),
//                    "aespa",
//                    "https://search.pstatic.net/common/?src=http%3A%2F%2Fimgnews.naver.net%2Fimage%2F5095%2F2022%2F07%2F09%2F0000974046_001_20220709084001248.jpg&type=sc960_832"
//                )
//            )
//        }
//    }

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
                        Log.d("popular", i.snippet.title)
                    }
                    _popularList.value = popularResultList
                }

                override fun onFailure(call: Call<PopularVideoModel>, t: Throwable) {

                }

            })
    }

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

    fun getCategory(key: String, part: String, regionCode: String) {
        apiService.getCategoryList(key, part, regionCode)
            ?.enqueue(object : Callback<CategoryModel> {
                override fun onResponse(
                    call: Call<CategoryModel>,
                    response: Response<CategoryModel>
                ) {
                    for (i in response.body()?.items!!) {
                        Log.d("category", i.snippet.title)
                    }
                    _categoryList.value = response.body()?.items
                }

                override fun onFailure(call: Call<CategoryModel>, t: Throwable) {

                }

            })
    }

}
