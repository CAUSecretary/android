package com.example.causecretary.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.causecretary.ui.utils.UiUtils
import com.example.causecretary.R
import com.example.causecretary.adapter.AdminAdapter
import com.example.causecretary.databinding.ActivityLoginBinding
import com.example.causecretary.ui.admin.AdminMainActivity
import com.example.causecretary.ui.api.ApiService
import com.example.causecretary.ui.api.RetrofitApi
import com.example.causecretary.ui.data.AdminResponse
import com.example.causecretary.ui.data.AdminResult
import com.example.causecretary.ui.data.RegisterResponse
import com.example.causecretary.ui.data.Uncertified
import com.example.causecretary.ui.data.dto.AdminRequestData
import com.example.causecretary.ui.forgot.ForgotIdActivity
import com.example.causecretary.ui.forgot.ForgotPwdActivity
import com.example.causecretary.ui.register.RegisterActivity
import com.example.causecretary.ui.utils.Logger
import com.example.causecretary.ui.utils.PrefManager
import com.example.causecretary.viewmodel.LoginViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import kotlin.system.exitProcess

class LoginActivity : AppCompatActivity(), View.OnClickListener, Observer<AdminResponse> {

    //lateinit var viewModel: LoginViewModel
    private var viewModel: LoginViewModel? = null
    //뒤로가기 두번 누를때 꺼지게
    private var mBackBtnPresses: Boolean = false
    //var registerResponse = RegisterResponse(null,null,null,null)

    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        viewModel?.liveData?.observe(this,this)
        initData()
        initView()
    }

    private fun initView() {

        binding.clickListener = this@LoginActivity

        //TODO test

        //viewModel.liveData.postValue()


        binding.etEmail.requestFocus()
        Handler(Looper.getMainLooper()).postDelayed({
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(binding.etEmail,InputMethodManager.SHOW_IMPLICIT)
        },300)
    }

    private fun initData() {
        //TODO("Not yet implemented")
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.tv_register -> {
               /* Intent(this@LoginActivity,AuthPhoneActivity::class.java).run {
                    startActivity(this)
                }*/
                Intent(this@LoginActivity,RegisterActivity::class.java).run {
                    startActivity(this)
                }

            }
            R.id.cb_auto_login,R.id.tv_auto_login -> {
                Toast.makeText(this,"auto_login",Toast.LENGTH_SHORT).show()
            }
            R.id.btn_login -> {
                Logger.e("doori",binding.etEmail.text.toString())
                if(binding.etEmail.text.toString() == "k1@cau.ac.kr"){
                    Logger.e("doori","if문 adminLogin")
                    adminLogin()
                }else{
                    login()
                }
                /*Intent(this@LoginActivity,MainActivity::class.java).run {
                    startActivity(this)
                }*/

            }
            R.id.tv_forgot_id -> {
                Intent(this@LoginActivity,ForgotIdActivity::class.java).run {
                    startActivity(this)
                }
            }
            R.id.tv_forgot_pwd -> {
                Intent(this@LoginActivity,ForgotPwdActivity::class.java).run {
                    startActivity(this)
                }
            }
        }
    }

    private fun adminLogin() {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiService.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val registerService = retrofit.create(RetrofitApi::class.java)
        val admin = AdminRequestData("k1@cau.ac.kr","1")
        registerService.adminlogin(admin).enqueue(object : Callback<AdminResponse> {
            override fun onResponse(
                call: Call<AdminResponse>,
                response: Response<AdminResponse>
            ) {
                val adminResponse = response.body() as AdminResponse
                viewModel?.liveData?.postValue(adminResponse)
                Logger.e("doori", response.toString())
                // Logger.e("doori", registerResponse.toString())
            }

            override fun onFailure(call: Call<AdminResponse>, t: Throwable) {
                Logger.e("doori", t.toString())
            }

        })
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken,0)
        return true
    }

    override fun onBackPressed() {
        binding.run {
            if(mBackBtnPresses){
                mBackBtnPresses = false
                exitApp()
            } else {
                UiUtils.showSnackBar(root,"'뒤로' 버튼을 한번 더 누르시면 종료됩니다.")
                mBackBtnPresses = true
                Handler(Looper.getMainLooper()).postDelayed({mBackBtnPresses = false},2500)
            }
        }
    }

    private fun exitApp() {
        ActivityCompat.finishAffinity(this)
        System.runFinalization()
        exitProcess(0)
    }

    override fun onResume() {
        super.onResume()
        binding.apply {
            etEmail.text=null
            etPwd.text=null
        }
    }

    private fun login(){
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiService.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val registerService = retrofit.create(RetrofitApi::class.java)
        val user = AdminRequestData("111@cau.ac.kr","111")
        registerService.login(user).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                val registerResponse = response.body() as RegisterResponse
                //viewModel?.liveData?.postValue(registerResponse)
                Logger.e("doori",response.toString())
                Logger.e("doori",registerResponse.toString())
                Intent(this@LoginActivity,MainActivity::class.java).run {
                    startActivity(this)
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Logger.e("doori",t.toString())
            }

        })
    }

    override fun onChanged(t: AdminResponse?) {
        Logger.e("doori","onChanged = ${t.toString()}")
        t?.result?.apply {
            PrefManager(this@LoginActivity).setLoginData(this.userIdx,this.jwt)
        }
        Intent(this@LoginActivity,AdminMainActivity::class.java).run {
            startActivity(this)
        }
    }
}


