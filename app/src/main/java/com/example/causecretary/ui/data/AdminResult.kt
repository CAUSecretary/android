package com.example.causecretary.ui.data

data class AdminResult(
    val jwt: String,
    val uncertified: List<Uncertified>,
    val userIdx: Int
)