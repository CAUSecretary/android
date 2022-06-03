package com.example.causecretary.ui.register

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import com.example.causecretary.R
import com.example.causecretary.databinding.ActivityAuthPhone2Binding
import com.example.causecretary.ui.utils.Logger
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class AuthPhone2Activity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityAuthPhone2Binding
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
            DataBindingUtil.setContentView(this@AuthPhone2Activity, R.layout.activity_auth_phone2)

        initData()
        initView()
    }

    private fun initView() {
        binding.clickListener = this@AuthPhone2Activity
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
                        TODO("Not yet implemented")
                    }

                    override fun onVerificationFailed(p0: FirebaseException) {
                        TODO("Not yet implemented")
                    }

                    override fun onCodeSent(
                        verificationId: String,
                        token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        this@AuthPhone2Activity.verificationId = verificationId
                    }
                }

                val optionsCompat = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber("+821064912552")
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(this)
                    .setCallbacks(callbacks)
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(optionsCompat)
                auth.setLanguageCode("kr")

            }
            R.id.btn_auth -> {
                Logger.e("doori", binding.etAuthNumber.text.toString())
                val credential = PhoneAuthProvider.getCredential(
                    verificationId,
                    binding.etAuthNumber.text.toString()
                )
                signInWithPhoneAuthCredential(credential)
            }
            R.id.btn_next -> {
                Intent(this@AuthPhone2Activity, RegisterActivity::class.java).run {
                    this.putExtra("phoneNumber","01064912552")
                    startActivity(this)
                }
            }
            R.id.ib_back -> {
                finish()
            }
        }
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