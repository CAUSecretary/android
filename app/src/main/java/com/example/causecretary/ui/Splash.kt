package com.example.causecretary.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.causecretary.R
import com.example.causecretary.databinding.ActivitySplashBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Splash : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        initData()
        initView()

    }

    private fun initView() {
        CoroutineScope(Dispatchers.Default).launch {
            delay(1000)
            Intent(this@Splash, MainActivity::class.java).run {
                startActivity(this)
            }
        }
    }

    private fun initData() {
       // TODO("Not yet implemented")
    }
}