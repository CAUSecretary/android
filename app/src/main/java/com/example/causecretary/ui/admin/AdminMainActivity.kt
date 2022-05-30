package com.example.causecretary.ui.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.causecretary.R
import com.example.causecretary.adapter.AdminAdapter
import com.example.causecretary.databinding.ActivityAdminMainBinding
import com.example.causecretary.ui.LoginActivity
import com.example.causecretary.ui.MainActivity
import com.example.causecretary.ui.api.ApiService
import com.example.causecretary.ui.api.RetrofitApi
import com.example.causecretary.ui.data.AdminResponse
import com.example.causecretary.ui.data.Uncertified
import com.example.causecretary.ui.data.dto.AdminRequestData
import com.example.causecretary.ui.utils.Logger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AdminMainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityAdminMainBinding
    lateinit var adminList: List<Uncertified>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this@AdminMainActivity, R.layout.activity_admin_main)

        initData()
        initView()
    }

    private fun initData() {
        // TODO("Not yet implemented")
        admintest()
    }

    private fun initView() {
        binding.clickListener = this@AdminMainActivity
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ib_close -> {
                //TODO 여기 로그아웃해주는거 추가해야해!
                Intent(this@AdminMainActivity,MainActivity::class.java).run {
                    startActivity(this)
                }
            }
        }
    }

    private fun getImage() {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiService.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val adminService = retrofit.create(RetrofitApi::class.java)

        /*adminService.getImageList().enqueue(object :Callback<AdminResponse>{
            override fun onResponse(call: Call<AdminResponse>, response: Response<AdminResponse>) {
                Logger.e("doori",response.toString())
            }

            override fun onFailure(call: Call<AdminResponse>, t: Throwable) {
                Logger.e("doori",t.toString())
            }

        })*/
    }

    private fun admintest() {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiService.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val admin = AdminRequestData("k1@cau.ac.kr","1")

        val registerService = retrofit.create(RetrofitApi::class.java)
        registerService.adminlogin(admin).enqueue(object : Callback<AdminResponse> {
            override fun onResponse(
                call: Call<AdminResponse>,
                response: Response<AdminResponse>
            ) {
                 val adminResponse = response.body() as AdminResponse
                Logger.e("doori", response.toString())
                adminList = adminResponse.result.uncertified
                var adapter = AdminAdapter(adminList)
                binding.rcAdmin.adapter=adapter
                binding.rcAdmin.layoutManager=LinearLayoutManager(this@AdminMainActivity)
                // Logger.e("doori", registerResponse.toString())
            }

            override fun onFailure(call: Call<AdminResponse>, t: Throwable) {
                Logger.e("doori", t.toString())
            }

        })
    }
}
