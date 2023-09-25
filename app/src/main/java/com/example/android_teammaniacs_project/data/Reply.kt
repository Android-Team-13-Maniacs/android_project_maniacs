package com.example.android_teammaniacs_project.data

import android.net.Uri

data class Reply(
    val profileImage : Uri?,
    val id : String,
    val content : String,
    val dateTime : String
)
