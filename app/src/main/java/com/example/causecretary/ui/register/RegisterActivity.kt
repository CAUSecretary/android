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
import com.example.causecretary.ui.utils.GmailSender
import com.example.causecretary.ui.utils.UiUtils


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
                val gmailSender = GmailSender()
                gmailSender.sendEmail("dooooreeee@naver.com")
                UiUtils.showSnackBar(binding.root,"메일보냄")
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
            R.id.cl_dept -> {

                UiUtils.showSnackBar(binding.root,"아직 구현안함")
            }
            R.id.cl_club -> {
                UiUtils.showSnackBar(binding.root,"아직 구현안함")
            }
            R.id.btn_club_auth -> {
                UiUtils.showSnackBar(binding.root,"아직 구현안함")
            }

        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken,0)
        return true
    }
}