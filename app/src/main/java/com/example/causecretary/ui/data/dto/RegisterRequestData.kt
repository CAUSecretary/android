package com.example.causecretary.ui.data.dto

import com.google.gson.annotations.SerializedName
import retrofit2.http.Field

data class RegisterRequestData(
    @SerializedName("userIdx") var userIdx: String,
    @SerializedName("name") var name: String,
    @SerializedName("phone")var phone: String,
    @SerializedName("email") var email: String,
    @SerializedName("password") var password: String,
    @SerializedName("univ") var univ:String,
    @SerializedName("department") var department: String,

    @SerializedName("belong")  var belong: String,
    @SerializedName("certifyImg")  var certifyImg: String,
    @SerializedName("certified") var certified: String

)