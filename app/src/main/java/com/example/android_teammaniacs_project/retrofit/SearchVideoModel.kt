package com.example.android_teammaniacs_project.retrofit

data class SearchVideoModel(
    val kind : String,
    val etag : String,
    val nextPageToken : String,
    val regionCode : String,
    val pageInfo : SearchPageInfo,
    val items : List<SearchItem>
)

data class SearchItem(
    val kind: String,
    val etag: String,
    val id: SearchID,
    val snippet: SearchSnippet
)

data class SearchID(
    val kind: String,
    val videoId: String
)

data class SearchSnippet(
    val publishedAt: String,
    val channelId: String,
    val title: String,
    val description: String,
    val thumbnails: SearchThumbnails,
    val channelTitle: String,
    val liveBroadcastContent: String,
    val publishTime: String
)

data class SearchThumbnails(
    val default: SearchDefault,
    val medium: SearchDefault,
    val high: SearchDefault
)

data class SearchDefault (
    val url: String,
    val width: Long,
    val height: Long
)

data class SearchPageInfo (
    val totalResults: Long,
    val resultsPerPage: Long
)
