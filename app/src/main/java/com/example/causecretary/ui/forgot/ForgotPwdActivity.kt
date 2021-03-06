package com.example.causecretary.ui.forgot

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.example.causecretary.R
import com.example.causecretary.databinding.ActivityForgotPwdBinding
import com.example.causecretary.ui.LoginActivity
import com.example.causecretary.ui.api.ApiService
import com.example.causecretary.ui.api.RetrofitApi
import com.example.causecretary.ui.data.ForgotResponse
import com.example.causecretary.ui.utils.Logger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ForgotPwdActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityForgotPwdBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_forgot_pwd)

        initData()
        initView()
    }

    private fun initView() {
        binding.clickListener=this@ForgotPwdActivity
    }

    private fun initData() {
        //TODO("Not yet implemented")
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.ib_back -> {
                finish()
            }
            R.id.btn_send -> {
                getPwd()
            }
        }
    }

    private fun getPwd() {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiService.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val registerService = retrofit.create(RetrofitApi::class.java)
        registerService.forgotPwd(binding.etEmail.text.toString()).enqueue(object : Callback<ForgotResponse> {
            override fun onResponse(
                call: Call<ForgotResponse>,
                response: Response<ForgotResponse>
            ) {

                Logger.e("doori", response.toString())
                // Logger.e("doori", registerResponse.toString())
                if(response.body()==null){
                    val builder = AlertDialog.Builder(this@ForgotPwdActivity)
                        .setTitle("????????? ?????? ??????")
                        .setMessage("???????????? ?????? ${response.code()}")
                        .setPositiveButton("??????",
                            DialogInterface.OnClickListener { dialog, which ->
                                Toast.makeText(this@ForgotPwdActivity, "??????", Toast.LENGTH_SHORT)
                                    .show()
                            })
                        .setNegativeButton("??????",
                            DialogInterface.OnClickListener { dialog, which ->
                                Toast.makeText(this@ForgotPwdActivity, "??????", Toast.LENGTH_SHORT)
                                    .show()
                            })
                    builder.show()
                }
                response.body()?.apply {
                    if(this.code==1000){
                        val builder = AlertDialog.Builder(this@ForgotPwdActivity)
                            .setTitle("????????????")
                            .setMessage("???????????? ??????????????? ${this.result} ?????????.")
                            .setPositiveButton("??????",
                                DialogInterface.OnClickListener { dialog, which ->
                                    Intent(this@ForgotPwdActivity,LoginActivity::class.java).run {
                                        startActivity(this)
                                    }
                                    finishAffinity()
                                })
                            .setNegativeButton("??????",
                                DialogInterface.OnClickListener { dialog, which ->
                                    Toast.makeText(this@ForgotPwdActivity, "??????", Toast.LENGTH_SHORT)
                                        .show()
                                })
                        builder.show()
                    }else{
                        val builder = AlertDialog.Builder(this@ForgotPwdActivity)
                            .setTitle("???????????? ?????? ??????")
                            .setMessage(this.message)
                            .setPositiveButton("??????",
                                DialogInterface.OnClickListener { dialog, which ->
                                    Toast.makeText(this@ForgotPwdActivity, "??????", Toast.LENGTH_SHORT)
                                        .show()
                                })
                            .setNegativeButton("??????",
                                DialogInterface.OnClickListener { dialog, which ->
                                    Toast.makeText(this@ForgotPwdActivity, "??????", Toast.LENGTH_SHORT)
                                        .show()
                                })
                        builder.show()
                    }
                }
            }

            override fun onFailure(call: Call<ForgotResponse>, t: Throwable) {
                Logger.e("doori", t.toString())
                val builder = AlertDialog.Builder(this@ForgotPwdActivity)
                    .setTitle("??????????????? ??????")
                    .setMessage("???????????? ?????? ${t.message}")
                    .setPositiveButton("??????",
                        DialogInterface.OnClickListener { dialog, which ->
                            Toast.makeText(this@ForgotPwdActivity, "??????", Toast.LENGTH_SHORT)
                                .show()
                        })
                    .setNegativeButton("??????",
                        DialogInterface.OnClickListener { dialog, which ->
                            Toast.makeText(this@ForgotPwdActivity, "??????", Toast.LENGTH_SHORT)
                                .show()
                        })
                builder.show()
            }

        })
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return true
    }
}