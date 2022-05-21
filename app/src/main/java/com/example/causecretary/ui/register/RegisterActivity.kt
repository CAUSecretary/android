package com.example.causecretary.ui.register

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.example.causecretary.R
import com.example.causecretary.databinding.ActivityRegisterBinding
import com.example.causecretary.ui.LoginActivity
import com.example.causecretary.ui.api.ApiService
import com.example.causecretary.ui.api.ApiService.Companion.DOMAIN
import com.example.causecretary.ui.api.RetrofitApi
import com.example.causecretary.ui.data.RegisterResponse
import com.example.causecretary.ui.data.dto.RegisterRequestData
import com.example.causecretary.ui.utils.GmailSender
import com.example.causecretary.ui.utils.Logger
import com.example.causecretary.ui.utils.UiUtils
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.SimpleDateFormat

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var galleryResult: ActivityResultLauncher<Intent>

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

        galleryResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ activityResult ->
            if(activityResult.resultCode== RESULT_OK){
                activityResult.data?.data.let {
                    val bitmap: Bitmap? = loadBitmap(it)
                    val base64 = bitmapToString(bitmap)
                    Logger.e("doori",base64)
                    binding.tvAuthHint.setTextColor(Color.BLACK)
                    binding.tvAuthHint.text=base64

                }
            }else{
                UiUtils.showSnackBar(binding.root,"갤러리 오류")
            }
        }
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
                register()
                /*Intent(this@RegisterActivity,WelcomeActivity::class.java).run {
                    startActivity(this)
                }*/
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
                openGallery()

            }

        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken,0)
        return true
    }

    private fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK)

        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        intent.type = "image/*"
        galleryResult.launch(intent)

    }

    private fun newFileName(): String {
        Log.d("doori","newFileName")
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
        val filename =sdf.format(System.currentTimeMillis())
        return "$filename.jpg"
    }

    fun loadBitmap(photoUri: Uri?): Bitmap?{
        Log.d("doori","loadBitmap")
        var image: Bitmap? = null
        try{
            image =if(Build.VERSION.SDK_INT>27){
                val source : ImageDecoder.Source =
                    ImageDecoder.createSource(binding.root.context.contentResolver,photoUri!!)
                ImageDecoder.decodeBitmap(source)
            }else{
                MediaStore.Images.Media.getBitmap(binding.root.context.contentResolver,photoUri)
            }
        } catch (e: IOException){
            e.printStackTrace()
        }
        return image
    }
    private fun createImageUri(filename: String, mimeType: String): Uri?{
        Log.d("doori","createImageUri")
        var values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME,filename)
        values.put(MediaStore.Images.Media.MIME_TYPE,mimeType)
        return binding.root.context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values)
    }

    private fun bitmapToString(bitmap: Bitmap?):String{
        val bytearrayOutputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG,100,bytearrayOutputStream)
        val byteArray=bytearrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray,Base64.DEFAULT)
    }

    private fun stringToBitmap(base64: String?):Bitmap{
        val encodeByte = Base64.decode(base64,Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(encodeByte,0,encodeByte.size)
    }

    private fun register(){
        val registerRequestData=RegisterRequestData("1211","asd","aa","doooreee@naver.com","aa","a","a","a","a","a")


        val retrofit = Retrofit.Builder()
            .baseUrl(DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val registerService = retrofit.create(RetrofitApi::class.java)
        registerService.postUsers(registerRequestData).enqueue(object :Callback<RegisterResponse>{
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                val registerResponse = response.body() as RegisterResponse
                Logger.e("doori",response.toString())
                Logger.e("doori",registerResponse.toString())
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Logger.e("doori",t.toString())
            }

        })
    }
}