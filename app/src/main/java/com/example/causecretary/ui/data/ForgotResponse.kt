package com.example.causecretary.ui.data

data class ForgotResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: String
)