package com.example.causecretary.ui.data.dto

import com.google.gson.annotations.SerializedName
import retrofit2.http.Field

data class RegisterRequestData(
    @SerializedName("userIdx") val userIdx: String,
    @SerializedName("name") val name: String,
    @SerializedName("phone")val phone: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("univ") val univ:String,
    @SerializedName("department") val department: String,

    @SerializedName("belong")  val belong: String,
    @SerializedName("certifyImg")  val certifyImg: String,
    @SerializedName("certified") val certified: String

)