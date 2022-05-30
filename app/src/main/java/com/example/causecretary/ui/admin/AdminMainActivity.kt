package com.example.causecretary.ui.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.causecretary.R
import com.example.causecretary.databinding.ActivityAdminMainBinding
import com.example.causecretary.ui.api.ApiService
import com.example.causecretary.ui.api.RetrofitApi
import com.example.causecretary.ui.data.AdminResponse
import com.example.causecretary.ui.utils.Logger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AdminMainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityAdminMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this@AdminMainActivity, R.layout.activity_admin_main)

        initData()
        initView()
    }

    private fun initData() {
        // TODO("Not yet implemented")
    }

    private fun initView() {
        binding.clickListener = this@AdminMainActivity
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_next -> {
                getImage()
            }
        }
    }

    private fun getImage() {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiService.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val adminService = retrofit.create(RetrofitApi::class.java)

        adminService.getImageList().enqueue(object :Callback<AdminResponse>{
            override fun onResponse(call: Call<AdminResponse>, response: Response<AdminResponse>) {
                Logger.e("doori",response.toString())
            }

            override fun onFailure(call: Call<AdminResponse>, t: Throwable) {
                Logger.e("doori",t.toString())
            }

        })
    }
}