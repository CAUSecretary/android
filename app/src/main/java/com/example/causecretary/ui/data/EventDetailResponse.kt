package com.example.causecretary.ui.data

data class EventDetailResponse(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: EventDetailResult
)