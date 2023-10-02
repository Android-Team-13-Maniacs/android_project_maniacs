package com.example.android_teammaniacs_project.constants
enum class MyPageType {
    EDIT, SAVE;

    //name을 가지고 enum class를 가져옴
    companion object {
        fun from(name: String?): MyPageType? {
            return MyPageType.values().find {
                it.name.uppercase() == name?.uppercase()
            }
        }
    }
}

fun test(){
    MyPageType.EDIT.name// 이름으로 가져오기
    MyPageType.EDIT.ordinal// 0번째 자리로 가져오기
}
