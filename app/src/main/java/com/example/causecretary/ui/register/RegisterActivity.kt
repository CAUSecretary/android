package com.example.causecretary.ui.register

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import com.example.causecretary.R
import com.example.causecretary.databinding.ActivityRegisterBinding
import com.example.causecretary.ui.LoginActivity

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register)

        initData()
        initView()
    }

    private fun initView() {
        binding.clickListener=this@RegisterActivity
    }

    private fun initData() {
        //TODO("Not yet implemented")
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_auth_email -> {
                //TODO
            }
            R.id.btn_auth -> {
                //TODO
            }
            R.id.btn_register -> {
                Intent(this@RegisterActivity,WelcomeActivity::class.java).run {
                    startActivity(this)
                }
            }
            R.id.ib_close -> {
                Intent(this@RegisterActivity,LoginActivity::class.java).run {
                    startActivity(this)
                }
                finishAffinity()
            }

        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken,0)
        return true
    }
}