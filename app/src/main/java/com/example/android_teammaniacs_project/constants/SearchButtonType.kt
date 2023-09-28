package com.example.android_teammaniacs_project.constants

// Search 정렬 기준 버튼을 구분하기 위한 Enum class
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