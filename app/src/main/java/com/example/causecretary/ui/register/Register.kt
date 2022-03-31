package com.example.causecretary.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.causecretary.R
import com.example.causecretary.databinding.ActivityRegisterBinding
import com.example.causecretary.ui.LoginActivity

class Register : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register)

        initData()
        initView()
    }

    private fun initView() {
        binding.clickListener=this@Register
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
                Intent(this@Register,Welcome::class.java).run {
                    startActivity(this)
                }
            }
            R.id.ib_close -> {
                Intent(this@Register,LoginActivity::class.java).run {
                    startActivity(this)
                }
                finishAffinity()
            }

        }
    }
}