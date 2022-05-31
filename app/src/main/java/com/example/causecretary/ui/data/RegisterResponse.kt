package com.example.causecretary.ui.data

import com.example.causecretary.ui.data.dto.Result
import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: Result
)
