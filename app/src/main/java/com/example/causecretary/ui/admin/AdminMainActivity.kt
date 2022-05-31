package com.example.causecretary.ui.admin

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.example.causecretary.R
import com.example.causecretary.adapter.AdminAdapter
import com.example.causecretary.databinding.ActivityAdminMainBinding
import com.example.causecretary.databinding.AdminDialogBinding
import com.example.causecretary.ui.MainActivity
import com.example.causecretary.ui.SplashActivity
import com.example.causecretary.ui.api.ApiService
import com.example.causecretary.ui.api.RetrofitApi
import com.example.causecretary.ui.data.AdminResponse
import com.example.causecretary.ui.data.Uncertified
import com.example.causecretary.ui.data.dto.AdminRequestData
import com.example.causecretary.ui.dialog.CustomDialog
import com.example.causecretary.ui.utils.Logger
import com.example.causecretary.ui.utils.UiUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream

class AdminMainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityAdminMainBinding
    lateinit var adminList: MutableList<Uncertified>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this@AdminMainActivity, R.layout.activity_admin_main)

        initData()
        initView()
    }

    private fun initData() {
        // TODO("Not yet implemented")
        val adminResponse = intent.getParcelableArrayListExtra<Uncertified>("adminResponse")
        if (adminResponse != null) {
            adminList= adminResponse
        }
        admintest1()
    }

    private fun admintest1() {
     /*   val uncertified1 = Uncertified("asd","asd","asd",1)
        val uncertified2 = Uncertified("asd","asd","asd",1)
        val testList: MutableList<Uncertified> = mutableListOf(uncertified1,uncertified2)*/
        val adapter = AdminAdapter(adminList)
        binding.rcAdmin.adapter=adapter

        adapter.setItemClickListener(object :AdminAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                val base64 = adapter.getList(position).certifyImg
                DataBindingUtil.inflate<AdminDialogBinding>(LayoutInflater.from(this@AdminMainActivity), R.layout.admin_dialog, null, false).apply {
                    this.ivAdmin.setImageBitmap(stringToBitmap(base64))
                    this.adminDialog = UiUtils.showCustomDialog(
                        this@AdminMainActivity,
                        root,
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT,
                        object : CustomDialog.DialogClickListener {
                            override fun onClose() {
                                // nothing
                            }

                            override fun onConfirm() {
                                //TODO 서버에 통신
                               adapter.deleteList(position)
                            }
                        })
                }
            }

        })
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

//    private fun admintest() {
//        val retrofit = Retrofit.Builder()
//            .baseUrl(ApiService.DOMAIN)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        val admin = AdminRequestData("k1@cau.ac.kr","1")
//
//        val registerService = retrofit.create(RetrofitApi::class.java)
//        registerService.adminlogin(admin).enqueue(object : Callback<AdminResponse> {
//            override fun onResponse(
//                call: Call<AdminResponse>,
//                response: Response<AdminResponse>
//            ) {
//                 val adminResponse = response.body() as AdminResponse
//                Logger.e("doori", response.toString())
//                adminList = adminResponse.result!!.uncertified
//                var adapter = AdminAdapter(adminList as MutableList<Uncertified>)
//                binding.rcAdmin.adapter=adapter
//                // Logger.e("doori", registerResponse.toString())
//            }
//
//            override fun onFailure(call: Call<AdminResponse>, t: Throwable) {
//                Logger.e("doori", t.toString())
//            }
//
//        })
//    }

    private fun stringToBitmap(base64: String?): Bitmap {
        val encodeByte = Base64.decode(base64, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
    }
}
