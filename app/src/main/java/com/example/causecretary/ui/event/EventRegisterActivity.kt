package com.example.causecretary.ui.event

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.causecretary.R
import com.example.causecretary.databinding.ActivityEventRegisterBinding

class EventRegisterActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityEventRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_event_register)


        initData()
        initView()
    }

    private fun initView() {
        binding.clickListener=this@EventRegisterActivity
    }

    private fun initData() {
        //TODO("Not yet implemented")
    }

    override fun onClick(view: View?) {
        when(view?.id){

        }
    }
}