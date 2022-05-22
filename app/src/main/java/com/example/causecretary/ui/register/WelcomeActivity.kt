package com.example.causecretary.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.causecretary.R
import com.example.causecretary.databinding.ActivityWelcomeBinding
import com.example.causecretary.ui.LoginActivity

class WelcomeActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_welcome)

        initData()
        initView()
    }

    private fun initView() {
        binding.clickListener = this@WelcomeActivity
    }

    private fun initData() {
        //TODO("Not yet implemented")
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_go_login -> {
                Intent(this@WelcomeActivity,LoginActivity::class.java).run {
                    startActivity(this)
                }
                finishAffinity()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Intent(this@WelcomeActivity,LoginActivity::class.java).run {
            startActivity(this)
        }
        finishAffinity()
    }
}