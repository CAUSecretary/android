package com.example.causecretary.ui.api

import com.example.causecretary.ui.data.RegisterResponse
import com.example.causecretary.ui.data.dto.RegisterRequestData
import com.example.causecretary.ui.data.dto.UsersItem
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

class ApiService {
    companion object{
        val DOMAIN = "http://192.168.138.85:9000/"
    }
}

interface RetrofitApi{

    //이거 다 코쳐야함
    @GET("posts/1")
    fun getUsers(): Call<UsersItem>

    @POST("users/register")
    fun postUsers(@Body body: RegisterRequestData): Call<RegisterResponse>

}