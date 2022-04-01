package com.example.causecretary.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.causecretary.R
import com.example.causecretary.databinding.ActivityAuthPhoneBinding

class AuthPhoneActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityAuthPhoneBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_auth_phone)


        initData()
        initView()
    }

    private fun initView() {
        binding.clickListener = this@AuthPhoneActivity
    }

    private fun initData() {
        //TODO("Not yet implemented")
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_auth_phone -> {
                Intent(this@AuthPhoneActivity,RegisterActivity::class.java).run {
                    startActivity(this)
                }
            }
            R.id.ib_back -> {
                finish()
            }
        }
    }
}