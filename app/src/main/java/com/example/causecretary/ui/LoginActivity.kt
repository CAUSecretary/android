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
import androidx.databinding.DataBindingUtil
import com.example.causecretary.R
import com.example.causecretary.databinding.ActivityLoginBinding
import com.example.causecretary.ui.register.Auth_phone

class LoginActivity : AppCompatActivity(), View.OnClickListener {
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
                Intent(this@LoginActivity,Auth_phone::class.java).run {
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
                Toast.makeText(this,"forgot_id",Toast.LENGTH_SHORT).show()
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
}