package com.example.causecretary.ui.event

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.example.causecretary.R
import com.example.causecretary.databinding.ActivityEventRegisterBinding
import com.example.causecretary.ui.MainActivity
import com.example.causecretary.ui.api.ApiService
import com.example.causecretary.ui.api.RetrofitApi
import com.example.causecretary.ui.data.EventDeleteResponse
import com.example.causecretary.ui.data.InstarResponse
import com.example.causecretary.ui.data.dto.InstarRequestData
import com.example.causecretary.ui.utils.Logger
import com.example.causecretary.ui.utils.PrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
            R.id.btn_register->{
                eventCreate()
            }
        }
    }

    private fun eventCreate() {
        val userIdx = PrefManager(this).getLoginData()?.userIdx
        val pointIdx = getEndPointIdx(binding.etLocation.selectedItem.toString())
        val eventName = binding.etEventName.text.toString()
        val belong = binding.rbStu.text.toString()
        val instartcralwer = 0
        val instarUrl = binding.etInstaUrl.text.toString()
        val kakao = binding.etKakaoUrl.text.toString()
        val phone = binding.etPhone.text.toString()
        val period = binding.etPeriod.text.toString()
        val instarRequestData = InstarRequestData(belong,eventName,instarUrl,instartcralwer, kakao,1,period,phone,pointIdx,userIdx!!)

        Logger.e("doori",instarRequestData.toString())
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiService.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val eventService = retrofit.create(RetrofitApi::class.java)
        eventService.createEvent(instarRequestData).enqueue(object :
            Callback<InstarResponse> {
            override fun onResponse(
                call: Call<InstarResponse>,
                response: Response<InstarResponse>
            ) {
                if(response.body()==null){
                    val builder = AlertDialog.Builder(this@EventRegisterActivity)
                        .setTitle("이벤트생성 오류")
                        .setMessage("네트워크 오류 ${response.code()}")
                        .setPositiveButton("확인",
                            DialogInterface.OnClickListener { dialog, which ->
                                Toast.makeText(this@EventRegisterActivity, "확인", Toast.LENGTH_SHORT)
                                    .show()
                            })
                        .setNegativeButton("취소",
                            DialogInterface.OnClickListener { dialog, which ->
                                Toast.makeText(this@EventRegisterActivity, "취소", Toast.LENGTH_SHORT)
                                    .show()
                            })
                    builder.show()
                }
                response.body()?.apply {
                    Logger.e("doori", response.toString())
                    Logger.e("doori", this.toString())
                    if (this.code==1000){
                        Intent(this@EventRegisterActivity,MainActivity::class.java).run {
                            startActivity(this)
                        }
                    }else{
                        val builder = AlertDialog.Builder(this@EventRegisterActivity)
                            .setTitle("이벤트생성 오류")
                            .setMessage(this.message)
                            .setPositiveButton("확인",
                                DialogInterface.OnClickListener { dialog, which ->
                                    Toast.makeText(this@EventRegisterActivity, "확인", Toast.LENGTH_SHORT)
                                        .show()
                                })
                            .setNegativeButton("취소",
                                DialogInterface.OnClickListener { dialog, which ->
                                    Toast.makeText(this@EventRegisterActivity, "취소", Toast.LENGTH_SHORT)
                                        .show()
                                })
                        builder.show()
                    }
                }
            }
            override fun onFailure(call: Call<InstarResponse>, t: Throwable) {
                Logger.e("doori", t.toString())
                val builder = AlertDialog.Builder(this@EventRegisterActivity)
                    .setTitle("이벤트생성 오류")
                    .setMessage(t.message)
                    .setPositiveButton("확인",
                        DialogInterface.OnClickListener { dialog, which ->
                            Toast.makeText(this@EventRegisterActivity, "확인", Toast.LENGTH_SHORT)
                                .show()
                        })
                    .setNegativeButton("취소",
                        DialogInterface.OnClickListener { dialog, which ->
                            Toast.makeText(this@EventRegisterActivity, "취소", Toast.LENGTH_SHORT)
                                .show()
                        })
                builder.show()
            }

        })



    }

    private fun setSpinner() {
        val buildingList = resources.getStringArray(R.array.structure_name)
        val buildingAdapter = ArrayAdapter(
            this,
            com.naver.maps.map.R.layout.support_simple_spinner_dropdown_item, buildingList
        )
        binding.etLocation.adapter = buildingAdapter

    }

    private fun getEndPointIdx(endPoint: String?): Int {
        when (endPoint) {
            "208관 제2공학관" -> {
                return 2
            }
            "207관 제1공학관 봅스터홀" -> {
                return 3
            }
            "204관 중앙도서관" -> {
                return 4
            }
            "203관 서라벌홀" -> {
                return 5
            }
            "테니스장 농구장" -> {
                return 6
            }
            "201관 본관" -> {
                return 7
            }
            "303관 법학관" -> {
                return 8
            }
            "302관 대학원" -> {
                return 9
            }
            "310관 100주년 기념관" -> {
                return 10
            }
            "운동장" -> {
                return 11
            }
            "107관 학생회관" -> {
                return 12
            }
            "101관 영신관" -> {
                return 13
            }
            "정문" -> {
                return 14
            }
        }
        return 1
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return true
    }
}