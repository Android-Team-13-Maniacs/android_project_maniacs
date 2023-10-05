package com.example.android_teammaniacs_project.utils

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {
    fun convertDateFormat(inputDateStr: String): String? {
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)

            // 입력된 문자열을 Date 객체로 파싱
            val date: Date = inputFormat.parse(inputDateStr)!!

            // 원하는 형식으로 변환한 문자열 반환
            return outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            return null
        }
    }
}