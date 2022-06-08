package com.example.causecretary.ui.data

data class InstarResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: InstarResult
)