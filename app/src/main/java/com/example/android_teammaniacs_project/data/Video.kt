package com.example.android_teammaniacs_project.data

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Video(
    val image: String,
    val title: String,
    val channelId: String,
    val date : String,
    val channelName : String,
    val description : String

) : Parcelable
