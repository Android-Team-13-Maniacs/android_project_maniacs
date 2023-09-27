package com.example.android_teammaniacs_project.retrofit

import com.google.gson.annotations.SerializedName

data class CategoryModel(
    val kind: String,
    val etag: String,
    val items: List<CategoryItem>
)

data class CategoryItem(
    val kind: CategoryKind,
    val etag: String,
    val id: String,
    val snippet: Snippet
)

enum class CategoryKind(val value: String) {
    @SerializedName("youtube#videoCategory")
    YoutubeVideoCategory("youtube#videoCategory");
}

data class CategorySnippet(
    val title: String,
    val assignable: Boolean,
    val channelID: ChannelID
)

enum class ChannelID(val value: String) {
    @SerializedName("UCBR8-60-B28hp2BmDPdntcQ")
    UCBR860B28Hp2BmDPdntcQ("UCBR8-60-B28hp2BmDPdntcQ");
}