package com.example.causecretary.ui.forgot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.causecretary.R
import com.example.causecretary.databinding.ActivityForgotIdBinding

class ForgotIdActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityForgotIdBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_forgot_id)

        initData()
        initView()
    }

    private fun initView() {
        binding.clickListener=this@ForgotIdActivity
    }

    private fun initData() {
     //   TODO("Not yet implemented")
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.ib_back -> {
                finish()
            }
            R.id.btn_auth -> {
                Intent(this@ForgotIdActivity,ForgotId2Activity::class.java).run {
                    startActivity(this)
                }
            }
        }
    }
}