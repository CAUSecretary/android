package com.example.causecretary.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.example.causecretary.ui.utils.UiUtils
import com.example.causecretary.R
import com.example.causecretary.databinding.ActivityLoginBinding
import com.example.causecretary.ui.forgot.ForgotIdActivity
import com.example.causecretary.ui.register.AuthPhoneActivity
import kotlin.system.exitProcess

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    //뒤로가기 두번 누를때 꺼지게
    private var mBackBtnPresses: Boolean = false

    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)

        initData()
        initView()
    }

    private fun initView() {
        binding.clickListener = this@LoginActivity

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
                Intent(this@LoginActivity,AuthPhoneActivity::class.java).run {
                    startActivity(this)
                }
            }
            R.id.cb_auto_login,R.id.tv_auto_login -> {
                Toast.makeText(this,"auto_login",Toast.LENGTH_SHORT).show()
            }
            R.id.btn_login -> {
                Toast.makeText(this,"login",Toast.LENGTH_SHORT).show()
            }
            R.id.tv_forgot_id -> {
                Intent(this@LoginActivity,ForgotIdActivity::class.java).run {
                    startActivity(this)
                }
            }
            R.id.tv_forgot_pwd -> {
                Toast.makeText(this,"forgot_pwd",Toast.LENGTH_SHORT).show()
            }
        }
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
}