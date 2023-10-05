package com.example.android_teammaniacs_project.retrofit

data class ChannelModel(
    val kind : String,
    val etag : String,
    val nextPageToken : String,
    val prevPageToken : String,
    val pageInfo : ChannelPageInfo,
    val items: List<ChannelItem>
)

data class ChannelPageInfo(
    val totalResults : Int,
    val resultsPerPage : Int
)

data class ChannelItem(
    val kind : String,
    val etag : String,
    val id : String,
    val snippet : ChannelSnippet
)

data class ChannelSnippet (
    val title: String,
    val description: String,
    val customUrl: String,
    val publishedAt: String,
    val thumbnails: ChannelThumbnails,
    val localized: ChannelLocalized,
    val country: String
)

data class ChannelLocalized (
    val title: String,
    val description: String
)

data class ChannelThumbnails (
    val default: ChannelDefault,
    val medium: ChannelDefault,
    val high: ChannelDefault
)

data class ChannelDefault (
    val url: String,
    val width: Long,
    val height: Long
)