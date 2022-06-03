package com.example.causecretary.ui.event

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.causecretary.R
import com.example.causecretary.adapter.EventDetailAdapter
import com.example.causecretary.databinding.ActivityEventBinding
import com.example.causecretary.ui.RouteActivity
import com.example.causecretary.ui.api.ApiService
import com.example.causecretary.ui.api.RetrofitApi
import com.example.causecretary.ui.data.EventDetailResponse
import com.example.causecretary.ui.data.EventDetailResult
import com.example.causecretary.ui.utils.Logger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EventActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityEventBinding
    //lateinit var eventDetailResponse:EventDetailResponse
    var eventIdx:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_event)

        initData()
        initView()
    }

    private fun initView() {
        binding.clickListener=this@EventActivity
    }

    private fun initData() {
        //eventIdx = intent.getIntExtra("eventIdx",0)
        //Logger.e("doori",eventIdx.toString())

        //getEventDetail(eventIdx)

        val s3= "https://scontent-gmp1-1.cdninstagram.com/v/t51.2885-15/281840934_107492245250705_555173062285680192_n.jpg?stp=dst-jpg_e35&_nc_ht=scontent-gmp1-1.cdninstagram.com&_nc_cat=105&_nc_ohc=pwqhmY-R6goAX_IDOwV&edm=AABBvjUBAAAA&ccb=7-5&ig_cache_key=Mjg0Mjg4MjA5OTA2NjA1MjAzMw%3D%3D.2-ccb7-5&oh=00_AT8F25D49EJ99mE3FNiYdnSTWuB3lG6zOFxIr2tVSSgNjg&oe=629D3BDF&_nc_sid=83d603"
        val s = "https://scontent-gmp1-1.cdninstagram.com/v/t51.2885-15/281840934_107492245250705_555173062285680192_n.jpg?stp=dst-jpg_e35&_nc_ht=scontent-gmp1-1.cdninstagram.com&_nc_cat=105&_nc_ohc=pwqhmY-R6goAX_IDOwV&edm=AABBvjUBAAAA&ccb=7-5&ig_cache_key=Mjg0Mjg4MjA5OTA2NjA1MjAzMw%3D%3D.2-ccb7-5&oh=00_AT8F25D49EJ99mE3FNiYdnSTWuB3lG6zOFxIr2tVSSgNjg&oe=629D3BDF&_nc_sid=83d603"
        val s1="https://scontent-gmp1-1.cdninstagram.com/v/t51.2885-15/283139406_685626309206835_3807317693912064780_n.jpg?stp=dst-jpg_e35&_nc_ht=scontent-gmp1-1.cdninstagram.com&_nc_cat=103&_nc_ohc=tD5DoLhCp08AX9gVzep&edm=AABBvjUBAAAA&ccb=7-5&ig_cache_key=Mjg0Mjg4MjA5ODk4MjA1NDY2NA%3D%3D.2-ccb7-5&oh=00_AT_HTkjO3FPrMUknllF_8y2dXA12leZExhlYDClD0-9okA&oe=629D3BB5&_nc_sid=83d603"
        val s2="https://scontent-gmp1-1.cdninstagram.com/v/t51.2885-15/283395761_533144354942993_4301059969446481556_n.jpg?stp=dst-jpg_e35&_nc_ht=scontent-gmp1-1.cdninstagram.com&_nc_cat=105&_nc_ohc=JIuUgZ6V9ZgAX96TXMU&edm=AABBvjUBAAAA&ccb=7-5&ig_cache_key=Mjg0Mjg4MjA5ODk4MjA0NzMzNA%3D%3D.2-ccb7-5&oh=00_AT9SrxEtuor_V5Qk_9RjuF3C26cisYNCd7zilNuiRcTtVA&oe=629DD676&_nc_sid=83d603"
        //test
        val imgs = listOf<String>(s,s1,s2,s3)
        val result = EventDetailResult("안녕하세요","학생회","daskdasd","asdasdas","8월8일","ㅁㄴㅇㅁㄴㅇㅁㄴㅇㅁㄴㅇㅁㄴㅇㅁㄴㅇ",imgs)
        val eventDetailResponse = EventDetailResponse(true,121,"asd",result)
        setView(eventDetailResponse)

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
                //eventDetailResponse = response.body() as EventDetailResponse

                //viewModel?.liveData?.postValue(registerResponse)
                Logger.e("doori", response.toString())
                //Logger.e("doori", eventDetailResponse.toString())

                //setView(eventDetailResponse)
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
            rcEvent.layoutManager = LinearLayoutManager(this@EventActivity,RecyclerView.HORIZONTAL,false)

            tvTitle.text = event.eventName
            tvKakaoInput.text = event.kakaoChatUrl
            tvPhoneInput.text=event.phone
            tvEventLocationInput.text="위치는 왜 안주니?"
            tvPeriodInput.text=event.period
            tvContent.text=event.contents
        }

    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.ib_back ->{
                finish()
            }
            R.id.btn_route ->{
                Intent(this@EventActivity,RouteActivity::class.java).run {
                    //TODO 여기추가해야함
                    putExtra("eventRoute",9)
                    startActivity(this)
                }
            }
        }
    }
}