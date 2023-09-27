package com.example.android_teammaniacs_project.retrofit

import com.example.android_teammaniacs_project.constants.Constants
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val apiService: RetrofitInterface
        get() = instance.create(RetrofitInterface::class.java)

    private val instance: Retrofit
        private get() {
            val gson = GsonBuilder().setLenient().create()

            return Retrofit.Builder()
                .baseUrl(Constants.GOOGLE_BASE_URL)  // 기본 URL 설정
                .addConverterFactory(GsonConverterFactory.create(gson))  // JSON 파싱을 위한 컨버터 추가
                .build()
        }
}