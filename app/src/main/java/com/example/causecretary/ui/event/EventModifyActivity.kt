package com.example.causecretary.ui.event

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.causecretary.R
import com.example.causecretary.adapter.EventAdminAdapter
import com.example.causecretary.adapter.EventDetailAdapter
import com.example.causecretary.databinding.ActivityEventBinding
import com.example.causecretary.databinding.ActivityEventModifyBinding
import com.example.causecretary.ui.RouteActivity
import com.example.causecretary.ui.api.ApiService
import com.example.causecretary.ui.api.RetrofitApi
import com.example.causecretary.ui.data.EventDeleteResponse
import com.example.causecretary.ui.data.EventDetailResponse
import com.example.causecretary.ui.data.EventUserListResponse
import com.example.causecretary.ui.utils.Logger
import com.example.causecretary.ui.utils.PrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EventModifyActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityEventModifyBinding
    lateinit var eventDetailResponse: EventDetailResponse
    var eventIdx:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_event_modify)

        initData()
        initView()
    }

    private fun initView() {
        binding.clickListener=this@EventModifyActivity
    }

    private fun initData() {
        eventIdx = intent.getIntExtra("eventIdx",0)
        Logger.e("doori",eventIdx.toString())

        getEventDetail(eventIdx)


    }

    private fun getEventDetail(eventIdx: Int) {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiService.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val registerService = retrofit.create(RetrofitApi::class.java)
        registerService.getEventDetail(eventIdx).enqueue(object : Callback<EventDetailResponse> {
            override fun onResponse(
                call: Call<EventDetailResponse>,
                response: Response<EventDetailResponse>
            ) {
                Logger.e("doori", response.toString())
                response.body()?.apply {
                    eventDetailResponse = this as EventDetailResponse
                    setView(eventDetailResponse)
                }

            }

            override fun onFailure(call: Call<EventDetailResponse>, t: Throwable) {
                Logger.e("doori", t.toString())
            }

        })
    }

    private fun setView(eventDetailResponse: EventDetailResponse) {
        val event = eventDetailResponse.result

        val adapter = EventDetailAdapter(event.imgs as MutableList<String>)

        binding.apply {
            rcEvent.adapter=adapter
            rcEvent.layoutManager = LinearLayoutManager(this@EventModifyActivity,
                RecyclerView.HORIZONTAL,false)

            tvTitle.text = event.eventName
            tvKakaoInput.text = event.kakaoChatUrl
            tvPhoneInput.text=event.phone
            tvEventLocationInput.text=event.location
            tvPeriodInput.text=event.period
            tvContent.text=event.contents
        }

    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.ib_back ->{
                finish()
            }
            R.id.btn_delete ->{
                val retrofit = Retrofit.Builder()
                    .baseUrl(ApiService.DOMAIN)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val userIdx = PrefManager(this).getLoginData()?.userIdx
                val eventService = retrofit.create(RetrofitApi::class.java)
                eventService.deleteEvent(userIdx!!,eventIdx).enqueue(object : Callback<EventDeleteResponse> {
                    override fun onResponse(
                        call: Call<EventDeleteResponse>,
                        response: Response<EventDeleteResponse>
                    ) {
                        response.body()?.apply {
                            Logger.e("doori", response.toString())
                            Logger.e("doori", this.toString())
                            finish()
                        }
                    }
                    override fun onFailure(call: Call<EventDeleteResponse>, t: Throwable) {
                        Logger.e("doori", t.toString())
                    }

                })

            }
        }
    }
}