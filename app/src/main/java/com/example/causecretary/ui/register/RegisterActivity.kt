package com.example.causecretary.ui.register

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MotionEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import com.example.causecretary.R
import com.example.causecretary.databinding.ActivityRegisterBinding
import com.example.causecretary.ui.LoginActivity
import com.example.causecretary.ui.utils.GmailSender
import com.example.causecretary.ui.utils.Logger
import com.example.causecretary.ui.utils.UiUtils
import java.lang.Boolean.FALSE

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityRegisterBinding
    private val emailtimer=object : CountDownTimer(300000,1000){
        override fun onTick(millisUntilFinished: Long) {
            Logger.e("doori","타이머 들어갓다")
            var min = millisUntilFinished/60000
            var second = (millisUntilFinished/1000)%60
            //형식 문자로 데이터 담기 실시
            var timer = "%02d:%02d".format(min, second)
            binding.tvEmailTimer.text = timer
        }

        override fun onFinish() {
            binding.apply {
                tvAuthEmail.setTextColor(Color.RED)
                tvAuthEmail.text = "인증번호를 다시 발송해주세요."
                tvEmailTimer.text = null
            }
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register)

        initData()
        initView()
    }

    private fun initView() {
        binding.clickListener=this@RegisterActivity
    }

    private fun initData() {
        //TODO("Not yet implemented")
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_auth_email -> {
                //TODO 이메일 입력안했을때 분기처리 해줘야함
                val email = binding.etEmail.text.toString()
                val gmailSender = GmailSender()
                gmailSender.sendEmail(email)
                UiUtils.showSnackBar(binding.root,"메일보냄")

                binding.tvAuthEmail.setTextColor(Color.RED)
                binding.tvAuthEmail.text = "인증번호를 발송했습니다."
                Logger.e("doori","타이머 전")

                emailtimer.start()


            }
            R.id.btn_auth -> {
                binding.apply {
                    if(etAuthNumber.text.toString()=="0"){
                        emailtimer.cancel()
                        tvEmailTimer.visibility=GONE
                        etAuthNumber.visibility=GONE
                        tvAuthNumber.visibility= GONE
                        tvAuthEmail.visibility=GONE
                        btnAuth.visibility=GONE
                        btnAuthEmail.setTextColor(Color.GREEN)
                        btnAuthEmail.text = "인증완료"
                        btnAuthEmail.isEnabled=false
                        etEmail.isEnabled=false

                        tvWarning.visibility=GONE

                    }
                    else{
                        binding.tvWarning.visibility= VISIBLE
                    }
                }
            }
            R.id.btn_register -> {
                Intent(this@RegisterActivity,WelcomeActivity::class.java).run {
                    startActivity(this)
                }
            }
            R.id.ib_close -> {
                Intent(this@RegisterActivity,LoginActivity::class.java).run {
                    startActivity(this)
                }
                finishAffinity()
            }
            R.id.cl_dept -> {
                UiUtils.showSnackBar(binding.root,"아직 구현안함")
            }
            R.id.cl_club -> {
                UiUtils.showSnackBar(binding.root,"아직 구현안함")
            }
            R.id.btn_club_auth -> {
                UiUtils.showSnackBar(binding.root,"아직 구현안함")
            }

        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken,0)
        return true
    }
}