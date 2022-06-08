package com.example.causecretary.ui.data

data class EventDeleteResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: String
)