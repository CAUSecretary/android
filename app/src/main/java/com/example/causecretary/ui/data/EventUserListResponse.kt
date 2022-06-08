package com.example.causecretary.ui.data

data class EventUserListResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: List<EventUserListResult>
)