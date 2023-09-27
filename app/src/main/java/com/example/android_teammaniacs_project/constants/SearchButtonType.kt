package com.example.android_teammaniacs_project.constants

enum class SearchButtonType {
    DATE, RATING, TITLE, COUNT;

    companion object {
        fun from(name: String?): SearchButtonType? {
            return SearchButtonType.values().find {
                it.name.uppercase() == name?.uppercase()
            }
        }
    }
}