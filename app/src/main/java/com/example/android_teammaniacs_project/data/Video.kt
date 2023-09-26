package com.example.android_teammaniacs_project.data

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Video(
    val image: Uri?,
    val title: String,
    val sourceUri: String?,
) : Parcelable
