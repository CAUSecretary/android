package com.example.causecretary.ui.api

import com.example.causecretary.ui.data.AdminResponse
import com.example.causecretary.ui.data.RegisterResponse
import com.example.causecretary.ui.data.dto.RegisterRequestData
import com.squareup.okhttp.RequestBody
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

class ApiService {
    companion object{
        val DOMAIN = "http://192.168.138.85:9000/"
    }
}

interface RetrofitApi{

    @Multipart
    @POST("test")
    fun postUsersMulti(@Part file: MultipartBody.Part): Call<String>


    @POST("users/register")
    fun postUsers(@Body body: RegisterRequestData): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("auth/login")
    fun login(@Field ("email")email:String, @Field("password") password: String): Call<RegisterResponse>

    @GET("users/register")
    fun getImageList(): Call<AdminResponse>


}