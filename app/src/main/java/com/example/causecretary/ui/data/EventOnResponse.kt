package com.example.causecretary.ui.data

data class EventOnResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: List<EventOnResult>
)