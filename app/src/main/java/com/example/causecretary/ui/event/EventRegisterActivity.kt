package com.example.causecretary.ui.event

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.example.causecretary.R
import com.example.causecretary.databinding.ActivityEventRegisterBinding
import com.example.causecretary.ui.MainActivity

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
        setSpinner()
    }

    private fun initData() {
        //TODO("Not yet implemented")
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_create->{
                Intent(this@EventRegisterActivity,MainActivity::class.java).run {
                    startActivity(this)
                }
            }
        }
    }

    private fun setSpinner() {
        val buildingList = resources.getStringArray(R.array.structure_name)
        val buildingAdapter = ArrayAdapter(
            this,
            com.naver.maps.map.R.layout.support_simple_spinner_dropdown_item, buildingList
        )
        binding.etLocation.adapter = buildingAdapter

    }
}