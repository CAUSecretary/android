package com.example.causecretary.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.causecretary.R
import com.example.causecretary.databinding.ActivityAuthPhoneBinding

class Auth_phone : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityAuthPhoneBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_auth_phone)


        initData()
        initView()
    }

    private fun initView() {
        binding.clickListener = this@Auth_phone
    }

    private fun initData() {
        //TODO("Not yet implemented")
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_login -> {
                Intent(this@Auth_phone,Register::class.java).run {
                    startActivity(this)
                }
            }
            R.id.ib_back -> {
                finish()
            }
        }
    }
}