package com.example.android_teammaniacs_project.retrofit


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitInterface {

    //인기 동영상
    @GET("youtube/v3/videos")
    fun getVideoList(
        @Query("key") key: String,
        @Query("part") part: String,
        @Query("chart") chart: String,
        @Query("maxResults") maxResults: Int
    ): Call<PopularVideoModel>

    //검색
    @GET("youtube/v3/search")
    fun getSearchList(
        @Query("key") key: String,
        @Query("part") part: String,
        @Query("maxResults") maxResults: Int,
        @Query("order") order: String,
        @Query("q") q: String?,
        @Query("type") type: String,
        @Query("pageToken") pageToken : String?
    ): Call<SearchVideoModel>

    //카테고리 별 영상
    @GET("youtube/v3/videos")
    fun getCategoryVideoList(
        @Query("key") key: String,
        @Query("part") part: String,
        @Query("chart") chart: String,
        @Query("maxResults") maxResults: Int,
        @Query("videoCategoryId") videoCategoryId: String
    ): Call<PopularVideoModel>

    //카테고리 리스트
    @GET("youtube/v3/videoCategories")
    fun getCategoryList(
        @Query("key") key: String,
        @Query("part") part: String,
        @Query("regionCode") regionCode: String
    ): Call<CategoryModel>
}