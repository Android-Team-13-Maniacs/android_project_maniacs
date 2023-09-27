package com.example.android_teammaniacs_project.retrofit

//인기 동영상, 카테고리 별 동영상 API DTO (리턴 값이 같아 같이 사용)
data class PopularVideoModel(
    val kind: String,
    val etag: String,
    val items: List<Item>,
    val nextPageToken: String,
    val pageInfo: PageInfo
)

data class PageInfo(
    val totalResults: Int,
    val resultsPerPage: Int
)

data class Item(
    val kind: String,
    val etag: String,
    val id: String,
    val snippet: Snippet,
)

data class Snippet(
    val publishedAt: String,
    val channelId: String,
    val title: String,
    val description: String,
    val thumbnails: Thumbnail,
    val channelTitle: String,
    val categoryId: String,
    val liveBroadcastContent: String,
    val localized: Localized
)

data class Localized(
    val title: String,
    val description: String,
)

data class Thumbnail(
    val default: Default,
    val medium: Default,
    val high: Default,
    val standard: Default,
    val maxres: Default
)

data class Default(
    val url: String,
    val width: Int,
    val height: Int
)
