package com.example.causecretary.ui.api

import com.example.causecretary.ui.data.dto.UsersItem
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

class ApiService {
    companion object{
        val DOMAIN = "여기는 우리 서버 도메인"
    }
}

interface RetrofitApi{

    //이거 다 코쳐야함
    @GET("posts/1")
    fun getUsers(): Call<UsersItem>

    @POST("posts")
    fun postUsers(@Body user: UsersItem): Call<UsersItem>
}