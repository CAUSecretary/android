package com.example.causecretary.ui.forgot

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.example.causecretary.R
import com.example.causecretary.databinding.ActivityAuthPhone2Binding
import com.example.causecretary.databinding.ActivityForgotId2Binding
import com.example.causecretary.ui.LoginActivity
import com.example.causecretary.ui.api.ApiService
import com.example.causecretary.ui.api.RetrofitApi
import com.example.causecretary.ui.data.AdminResponse
import com.example.causecretary.ui.data.ForgotResponse
import com.example.causecretary.ui.data.dto.LoginRequestData
import com.example.causecretary.ui.register.RegisterActivity
import com.example.causecretary.ui.utils.Logger
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ForgotId2Activity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityForgotId2Binding
    lateinit var auth: FirebaseAuth
    var verificationId = ""

    private val timer = object : CountDownTimer(60000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            var min = millisUntilFinished / 60000
            var second = (millisUntilFinished / 1000) % 60
            //형식 문자로 데이터 담기 실시
            var timer = "%02d:%02d".format(min, second)
            binding.tvTimer.text = timer
        }

        override fun onFinish() {
            binding.apply {
                Logger.e("doori",btnNext.isEnabled.toString())
                tvTimer.text = null
                tvWarning.text = "시간초과.다시 인증요청 해주세요."
                tvWarning.setTextColor(getColor(R.color.red))


            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this@ForgotId2Activity, R.layout.activity_forgot_id2)

        initData()
        initView()
    }

    private fun initView() {
        binding.clickListener = this@ForgotId2Activity
    }

    private fun initData() {
        auth = FirebaseAuth.getInstance()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_auth_phone -> {
                timer.start()
                binding.btnAuthPhone.text = "다시받기"

                val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                        //TODO("Not yet implemented")
                    }

                    override fun onVerificationFailed(p0: FirebaseException) {
                        //TODO("Not yet implemented")
                    }

                    override fun onCodeSent(
                        verificationId: String,
                        token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        this@ForgotId2Activity.verificationId = verificationId
                    }
                }
                var phoneNumber = binding.etPhone.text.substring(1,11)
                Logger.e("doori","+82$phoneNumber")
                val optionsCompat = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber("+82$phoneNumber")
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(this)
                    .setCallbacks(callbacks)
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(optionsCompat)
                auth.setLanguageCode("kr")

            }
            R.id.btn_auth -> {
                Logger.e("doori", binding.etAuthNumber.text.toString())
                /* binding.apply {
                     tvWarning.setTextColor(getColor(R.color.color_039400))
                     tvWarning.text = "인증완료"
                     btnNext.isEnabled = true

                     //inEnable만 해줘도될까?
                     btnAuthPhone.isEnabled=false
                     tvTimer.text=null
                     timer.cancel()
                 }*/
                val credential = PhoneAuthProvider.getCredential(
                    verificationId,
                    binding.etAuthNumber.text.toString()
                )
                signInWithPhoneAuthCredential(credential)
            }
            R.id.btn_next -> {
                getEmail()

            }
            R.id.ib_back -> {
                finish()
            }
        }
    }

    private fun getEmail() {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiService.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val registerService = retrofit.create(RetrofitApi::class.java)
        val admin = LoginRequestData("k1@cau.ac.kr", "1")
        registerService.forgotEmail(binding.etPhone.text.toString()).enqueue(object : Callback<ForgotResponse> {
            override fun onResponse(
                call: Call<ForgotResponse>,
                response: Response<ForgotResponse>
            ) {
                Logger.e("doori", response.toString())
                // Logger.e("doori", registerResponse.toString())
                if(response.body()==null){
                    val builder = AlertDialog.Builder(this@ForgotId2Activity)
                        .setTitle("이메일 찾기 오류")
                        .setMessage("네트워크 오류 ${response.code()}")
                        .setPositiveButton("확인",
                            DialogInterface.OnClickListener { dialog, which ->
                                Toast.makeText(this@ForgotId2Activity, "확인", Toast.LENGTH_SHORT)
                                    .show()
                            })
                        .setNegativeButton("취소",
                            DialogInterface.OnClickListener { dialog, which ->
                                Toast.makeText(this@ForgotId2Activity, "취소", Toast.LENGTH_SHORT)
                                    .show()
                            })
                    builder.show()
                }
                response.body()?.apply {
                    if(this.code==1000){
                        val builder = AlertDialog.Builder(this@ForgotId2Activity)
                            .setTitle("이메일 찾기")
                            .setMessage("회원님의 이메일은 ${this.result} 입니다.")
                            .setPositiveButton("확인",
                                DialogInterface.OnClickListener { dialog, which ->
                                    Toast.makeText(this@ForgotId2Activity, "확인", Toast.LENGTH_SHORT)
                                        .show()
                                })
                            .setNegativeButton("취소",
                                DialogInterface.OnClickListener { dialog, which ->
                                    Toast.makeText(this@ForgotId2Activity, "취소", Toast.LENGTH_SHORT)
                                        .show()
                                })
                        builder.show()
                    }else{
                        val builder = AlertDialog.Builder(this@ForgotId2Activity)
                            .setTitle("이메일 찾기 오류")
                            .setMessage(this.message)
                            .setPositiveButton("확인",
                                DialogInterface.OnClickListener { dialog, which ->
                                    Toast.makeText(this@ForgotId2Activity, "확인", Toast.LENGTH_SHORT)
                                        .show()
                                })
                            .setNegativeButton("취소",
                                DialogInterface.OnClickListener { dialog, which ->
                                    Toast.makeText(this@ForgotId2Activity, "취소", Toast.LENGTH_SHORT)
                                        .show()
                                })
                        builder.show()
                    }
                }

            }

            override fun onFailure(call: Call<ForgotResponse>, t: Throwable) {
                Logger.e("doori", t.toString())
                val builder = AlertDialog.Builder(this@ForgotId2Activity)
                    .setTitle("이메일 찾기 오류")
                    .setMessage("네트워크 오류")
                    .setPositiveButton("확인",
                        DialogInterface.OnClickListener { dialog, which ->
                            Toast.makeText(this@ForgotId2Activity, "확인", Toast.LENGTH_SHORT)
                                .show()
                        })
                    .setNegativeButton("취소",
                        DialogInterface.OnClickListener { dialog, which ->
                            Toast.makeText(this@ForgotId2Activity, "취소", Toast.LENGTH_SHORT)
                                .show()
                        })
                builder.show()
            }

        })
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        Logger.e("doori", "signInwithPhone")
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //인증성공
                    binding.apply {
                        tvWarning.setTextColor(getColor(R.color.color_039400))
                        tvWarning.text = "인증완료"
                        btnNext.isEnabled = true

                        //inEnable만 해줘도될까?
                        btnAuthPhone.isEnabled=false
                        tvTimer.text=null
                        timer.cancel()
                    }
                } else {
                    //인증실패
                    binding.apply {
                        tvWarning.setTextColor(getColor(R.color.red))
                        tvWarning.text = "인증번호가 틀립니다."
                    }
                }
            }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return true
    }
}