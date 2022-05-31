package com.example.causecretary.ui.api


import com.example.causecretary.ui.data.AdminResponse
import com.example.causecretary.ui.data.RegisterResponse
import com.example.causecretary.ui.data.dto.AdminRequestData
import com.example.causecretary.ui.data.dto.RegisterRequestData
import com.squareup.okhttp.RequestBody
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

class ApiService {
    companion object{
        val DOMAIN = "http://10.210.61.15:9000/"
        val NAVIDOMAIN = "http://10.210.61.167:8080/"
    }
}

interface RetrofitApi{

    @Multipart
    @POST("test")
    fun postUsersMulti(@Part file: MultipartBody.Part): Call<String>

    @POST("auth/authlogin")
    fun adminlogin(@Body admin:AdminRequestData): Call<AdminResponse>


    @POST("users/register")
    fun postUsers(@Body body: RegisterRequestData): Call<RegisterResponse>


    @POST("auth/userlogin")
    fun login(@Body admin:AdminRequestData): Call<RegisterResponse>

  
   // @GET("users/register")
    //fun getImageList(): Call<AdminResponse>

    @FormUrlEncoded
    @POST("/search/weigh")
    fun searchRoute_weigh(
        @Field("endPoint") endPoint:String,
        @Field("startLat") curLat:String,
        @Field("startLon") curLon:String
    ) : Call<String>

    @FormUrlEncoded
    @POST("/search/distance")
    fun searchRoute_distance(
        @Field("endPoint") endPoint:String,
        @Field("startLat") curLat:String,
        @Field("startLon") curLon:String
    ) : Call<String>



}