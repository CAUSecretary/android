package com.example.causecretary.ui.event

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.causecretary.R
import com.example.causecretary.adapter.EventAdapter
import com.example.causecretary.adapter.EventAdminAdapter
import com.example.causecretary.databinding.ActivityEventAdminBinding

class EventAdminActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityEventAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_event_admin)

        initData()
        initView()
    }

    private fun initView() {
        //TODO("Not yet implemented")
        binding.clickListener=this@EventAdminActivity

        val adapter = EventAdminAdapter()
        adapter.setList()
        binding.rcEvent.adapter=adapter
        binding.rcEvent.layoutManager=LinearLayoutManager(this)

        adapter.setItemClickListener(object : EventAdminAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val point = 12
                Intent(this@EventAdminActivity,EventActivity::class.java).run {
                    putExtra("eventRoute",point)
                    startActivity(this)
                }
            }
        })
    }

    private fun initData() {
        //TODO("Not yet implemented")
    }

    override fun onClick(view: View?) {

    }
}