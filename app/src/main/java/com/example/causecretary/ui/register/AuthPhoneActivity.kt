package com.example.causecretary.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.causecretary.R
import com.example.causecretary.databinding.ActivityAuthPhoneBinding
import com.example.causecretary.ui.utils.Logger
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class AuthPhoneActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityAuthPhoneBinding
    lateinit var auth: FirebaseAuth
    var verificationId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_auth_phone)




        initData()
        initView()
    }

    private fun initView() {
        binding.clickListener = this@AuthPhoneActivity
    }

    private fun initData() {
        //TODO("Not yet implemented")
        auth= FirebaseAuth.getInstance()
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_auth_phone -> {
                Intent(this@AuthPhoneActivity,AuthPhone2Activity::class.java).run {
                    startActivity(this)
                }
                /*val callbacks=object :PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                    override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                        TODO("Not yet implemented")
                    }

                    override fun onVerificationFailed(p0: FirebaseException) {
                        TODO("Not yet implemented")
                    }

                    override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                        this@AuthPhoneActivity.verificationId=verificationId
                        Logger.e("doori",this@AuthPhoneActivity.verificationId)
                    }

                }

                val optionsCompat =  PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber("+821067745131")
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(this)
                    .setCallbacks(callbacks)
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(optionsCompat)
                auth.setLanguageCode("kr")*/
            }
            R.id.ib_back -> {
                finish()
            }
        }
    }
}