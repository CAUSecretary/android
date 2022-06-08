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
import com.example.causecretary.ui.api.ApiService
import com.example.causecretary.ui.api.RetrofitApi
import com.example.causecretary.ui.data.EventDetailResponse
import com.example.causecretary.ui.data.EventUserListResponse
import com.example.causecretary.ui.utils.Logger
import com.example.causecretary.ui.utils.PrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
        val userIdx= PrefManager(this).getLoginData()?.userIdx
        getDetail(userIdx)

    }

    private fun getDetail(userIdx: Int?) {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiService.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val eventService = retrofit.create(RetrofitApi::class.java)
        eventService.getEventUser(userIdx!!).enqueue(object : Callback<EventUserListResponse> {
            override fun onResponse(
                call: Call<EventUserListResponse>,
                response: Response<EventUserListResponse>
            ) {
                Logger.e("doori", response.toString())
                response.body()?.apply {
                    var userList = this.result as MutableList
                    val adapter = EventAdminAdapter(userList)
                    binding.rcEvent.adapter=adapter
                    binding.rcEvent.layoutManager=LinearLayoutManager(this@EventAdminActivity)

                    adapter.setItemClickListener(object : EventAdminAdapter.OnItemClickListener {
                        override fun onClick(v: View, position: Int) {
                            val eventDetail = adapter.getList(position)
                            Intent(this@EventAdminActivity,EventModifyActivity::class.java).run {
                                putExtra("eventIdx",eventDetail.eventIdx)
                                startActivity(this)
                            }
                        }
                    })
                }

            }

            override fun onFailure(call: Call<EventUserListResponse>, t: Throwable) {
                Logger.e("doori", t.toString())
            }

        })
    }

    private fun initData() {
        //TODO("Not yet implemented")
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.ib_back ->{
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        initData()
        initView()
    }
}