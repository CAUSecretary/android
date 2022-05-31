package com.example.causecretary.ui.data

data class EventOffResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: List<EventOffResult>
)