package com.example.causecretary.ui.data

data class AdminResponse(
    val code: Int?,
    val isSuccess: Boolean?,
    val message: String?,
    val result: AdminResult?
)