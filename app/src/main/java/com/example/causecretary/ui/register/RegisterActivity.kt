package com.example.causecretary.ui.register

import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
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
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.example.causecretary.R
import com.example.causecretary.databinding.ActivityRegisterBinding
import com.example.causecretary.ui.LoginActivity
import com.example.causecretary.ui.api.ApiService.Companion.DOMAIN
import com.example.causecretary.ui.api.RetrofitApi
import com.example.causecretary.ui.data.AdminResponse
import com.example.causecretary.ui.data.RegisterResponse
import com.example.causecretary.ui.data.dto.LoginRequestData
import com.example.causecretary.ui.data.dto.RegisterRequestData
import com.example.causecretary.ui.utils.GmailSender
import com.example.causecretary.ui.utils.Logger
import com.example.causecretary.ui.utils.PrefManager
import com.example.causecretary.ui.utils.UiUtils
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var galleryResult: ActivityResultLauncher<Intent>
    lateinit var binding: ActivityRegisterBinding

    lateinit var registerRequestData :RegisterRequestData
    var bitmapMultipartBody: MultipartBody.Part? = null

    var emailCheck: Boolean = false
    var nameCheck: Boolean = false
    var stuIdCheck: Boolean = false
    var univCheck: Boolean = false
    var deptCheck: Boolean = false
    var clubCheck: Boolean = false
    var invalidCheck: Boolean = false


    private val emailtimer = object : CountDownTimer(300000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            var min = millisUntilFinished / 60000
            var second = (millisUntilFinished / 1000) % 60
            //?????? ????????? ????????? ?????? ??????
            var timer = "%02d:%02d".format(min, second)
            binding.tvEmailTimer.text = timer
        }

        override fun onFinish() {
            binding.apply {
                tvAuthEmail.setTextColor(Color.RED)
                tvAuthEmail.text = "??????????????? ?????? ??????????????????."
                tvEmailTimer.text = null
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        initData()
        initView()
    }

    private fun initView() {
        binding.clickListener = this@RegisterActivity

        //spinner
        setSpinner()

        //textwatcher
        textWatcher()

        //admintest()

        galleryResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
                if (activityResult.resultCode == RESULT_OK) {
                    activityResult.data?.data.let {
                        val bitmap: Bitmap? = loadBitmap(it)
                      //  val bitmapRequestBody = bitmap?.let { BitmapRequestBody(it@bitmap) }
                      //  bitmapMultipartBody=
                      //      if (bitmapRequestBody == null) null
                       //     else MultipartBody.Part.createFormData("image", newFileName(), bitmapRequestBody)

                       // testmulti(bitmapMultipartBody)
                        val base64 = bitmapToString(bitmap)
                        binding.tvAuthHint.setTextColor(Color.BLACK)
                        base64.substring(1,10).apply {
                            binding.tvAuthHint.text=this
                        }
                        registerRequestData.certifyImg=base64
                        Logger.e("uri",it.toString())
                        if (binding.tvAuthHint.text.isNotEmpty()) {
                            invalidCheck = true
                        }
                        allCheck()
                    }
                } else {
                    UiUtils.showSnackBar(binding.root, "????????? ??????")
                }
            }
    }

    private fun testmulti(bitmapMultipartBody: MultipartBody.Part?) {
        Logger.e("doori",bitmapMultipartBody.toString())
        val retrofit = Retrofit.Builder()
            .baseUrl(DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val registerService = retrofit.create(RetrofitApi::class.java)
        if (bitmapMultipartBody != null) {
            registerService.postUsersMulti(bitmapMultipartBody).enqueue(object : Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Logger.e("doori",response.toString())
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Logger.e("doori",t.toString())
                }

            })
        }

    }

    inner class BitmapRequestBody(private val bitmap: Bitmap) : RequestBody() {
        override fun contentType(): MediaType = "image/jpeg".toMediaType()
        override fun writeTo(sink: BufferedSink) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 99, sink.outputStream())
        }
    }

    private fun initData() {
        registerRequestData=RegisterRequestData(1234,"asd","asd","dooo@naver.com","123k12j3","a","a","a","","")

        //Logger.e("doori",intent.getStringExtra("phoneNumber").toString())
        registerRequestData.phone = intent.getStringExtra("phoneNumber").toString()
    }

    override fun onClick(view: View?) {
        hideKeyboard()
        when (view?.id) {
            R.id.btn_auth_email -> {
                //TODO ????????? ?????????????????? ???????????? ????????????
                val email = "${binding.etEmail.text.toString()}@cau.ac.kr"
                Logger.e("doori",email)
                val gmailSender = GmailSender()
                gmailSender.sendEmail(email)

                binding.tvAuthEmail.setTextColor(Color.RED)
                binding.tvAuthEmail.text = "??????????????? ??????????????????."
                Logger.e("doori", "????????? ???")

                emailtimer.start()


            }
            R.id.btn_auth -> {
                binding.apply {
                    if (etAuthNumber.text.toString() == "48925") {
                        emailtimer.cancel()
                        tvEmailTimer.visibility = GONE
                        etAuthNumber.visibility = GONE
                        tvAuthNumber.visibility = GONE
                        tvAuthEmail.visibility = GONE
                        btnAuth.visibility = GONE
                        btnAuthEmail.setTextColor(getColor(R.color.color_039400))
                        btnAuthEmail.text = "????????????"
                        btnAuthEmail.isEnabled = false
                        etEmail.isEnabled = false
                        tvWarning.visibility = GONE

                        emailCheck = true
                        allCheck()

                        registerRequestData.email="${binding.etEmail.text.toString()}@cau.ac.kr"

                    } else {
                        binding.tvWarning.visibility = VISIBLE
                    }
                }
            }
            R.id.btn_register -> {
                registerRequestData.password=binding.etPwd.text.toString()
                Logger.e("doori",registerRequestData.toString())

                register()

            }
            R.id.ib_close -> {

                /**
                 * test???
                 */
                //test()

                Intent(this@RegisterActivity,LoginActivity::class.java).run {
                    startActivity(this)
                }
                finishAffinity()
            }
            R.id.btn_club_auth -> {
                openGallery()
            }

        }
    }

    private fun admintest() {
        val retrofit = Retrofit.Builder()
            .baseUrl(DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val admin = LoginRequestData("k1@cau.ac.kr","1")

        val registerService = retrofit.create(RetrofitApi::class.java)
        registerService.adminlogin(admin).enqueue(object : Callback<AdminResponse> {
            override fun onResponse(
                call: Call<AdminResponse>,
                response: Response<AdminResponse>
            ) {
                val adminResponse = response.body() as AdminResponse
                Logger.e("doori", response.toString())
                Logger.e("doori", adminResponse.result!!.uncertified.toString())
                val uncertified = adminResponse.result!!.uncertified
                uncertified.forEach {
                    val bitmap = stringToBitmap(it.certifyImg)
                    Logger.e("doori", "bitmap = ${bitmap.toString()}")
                    binding.ivTest.setImageBitmap(bitmap)
                }

            }

            override fun onFailure(call: Call<AdminResponse>, t: Throwable) {
                Logger.e("doori", t.toString())
            }

        })
    }

    fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocus = window.currentFocus
        if (currentFocus != null) {
            inputManager.hideSoftInputFromWindow(
                currentFocus.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        intent.type = "image/*"
        galleryResult.launch(intent)

    }

    private fun newFileName(): String {
        Log.d("doori", "newFileName")
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
        val filename = sdf.format(System.currentTimeMillis())
        return "$filename.jpg"
    }

    fun loadBitmap(photoUri: Uri?): Bitmap? {
        Log.d("doori", "loadBitmap")
        var image: Bitmap? = null
        try {
            image = if (Build.VERSION.SDK_INT > 27) {
                val source: ImageDecoder.Source =
                    ImageDecoder.createSource(binding.root.context.contentResolver, photoUri!!)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(binding.root.context.contentResolver, photoUri)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return image
    }

    private fun createImageUri(filename: String, mimeType: String): Uri? {
        Log.d("doori", "createImageUri")
        var values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)
        return binding.root.context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )
    }

    private fun bitmapToString(bitmap: Bitmap?): String {
        val bytearrayOutputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, bytearrayOutputStream)
        val byteArray = bytearrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT).replace("\n","")
    }

    private fun stringToBitmap(base64: String?): Bitmap {
        val encodeByte = Base64.decode(base64, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
    }

    private fun register() {
        val retrofit = Retrofit.Builder()
            .baseUrl(DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val registerService = retrofit.create(RetrofitApi::class.java)
        registerService.postUsers(registerRequestData).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {

                Logger.e("doori", response.toString())
                if(response.body()==null){
                    val builder = AlertDialog.Builder(this@RegisterActivity)
                        .setTitle("?????? ??????")
                        .setMessage("?????? ??? ?????? ??????????????????.")
                        .setPositiveButton("??????",
                            DialogInterface.OnClickListener { dialog, which ->
                                Toast.makeText(this@RegisterActivity, "??????", Toast.LENGTH_SHORT)
                                    .show()
                            })
                        .setNegativeButton("??????",
                            DialogInterface.OnClickListener { dialog, which ->
                                Toast.makeText(this@RegisterActivity, "??????", Toast.LENGTH_SHORT)
                                    .show()
                            })
                    builder.show()
                }

                response.body()?.apply {
                    val registerResponse = this as RegisterResponse
                    if (registerResponse.code==1000){
                        Logger.e("doori", response.toString())
                        Logger.e("doori", registerResponse.toString())
                        Intent(this@RegisterActivity, WelcomeActivity::class.java).run {
                            startActivity(this)
                        }
                    }else{
                        val builder = AlertDialog.Builder(this@RegisterActivity)
                            .setTitle("???????????? ??????")
                            .setMessage(registerResponse.message)
                            .setPositiveButton("??????",
                                DialogInterface.OnClickListener{ dialog, which ->
                                    Toast.makeText(this@RegisterActivity, "??????", Toast.LENGTH_SHORT).show()
                                })
                            .setNegativeButton("??????",
                                DialogInterface.OnClickListener { dialog, which ->
                                    Toast.makeText(this@RegisterActivity, "??????", Toast.LENGTH_SHORT).show()
                                })
                        builder.show()
                    }
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Logger.e("doori", t.toString())
            }

        })
    }

    private fun test() {
        val test = RegisterRequestData(23,"sdf","","aksda@nsadsd.com","1lasdy89ah","asd","asd","asd","asd","asd")
        val retrofit = Retrofit.Builder()
            .baseUrl(DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val registerService = retrofit.create(RetrofitApi::class.java)
        registerService.postUsers(test).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                val registerResponse = response.body() as RegisterResponse
                Logger.e("doori", response.toString())
                Logger.e("doori", registerResponse.toString())
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Logger.e("doori", t.toString())
            }

        })
    }


    private fun setSpinner() {
        val univList = resources.getStringArray(R.array.cau_univ)
        val univAdapter = ArrayAdapter(
            this,
            com.naver.maps.map.R.layout.support_simple_spinner_dropdown_item, univList
        )
        binding.spUniv.adapter = univAdapter

        initDept()

        val clubList = listOf("????????? ??????????????????.", "?????????", "?????????", "??????")
        val clubAdapter = ArrayAdapter(
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            clubList
        )
        binding.spClub.adapter = clubAdapter
        binding.spClub.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                registerRequestData.belong= binding.spClub.getItemAtPosition(position).toString()
                hideKeyboard()
                clubCheck = position != 0
                allCheck()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                //TODO("Not yet implemented")
            }

        }


        binding.spUniv.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                registerRequestData.univ= binding.spUniv.getItemAtPosition(position).toString()
                hideKeyboard()
                univCheck = if (position == 0) {
                    initDept()
                    false
                } else {
                    true
                }
                allCheck()
                when (binding.spUniv.getItemAtPosition(position)) {
                    "????????????" -> {
                        setDeptSpinner(R.array.human_dept)
                    }
                    "??????????????????" -> {
                        setDeptSpinner(R.array.social_dept)
                    }
                    "????????????" -> {
                        setDeptSpinner(R.array.edu_dept)
                    }
                    "??????????????????" -> {
                        setDeptSpinner(R.array.manage_dept)
                    }
                    "??????????????????" -> {
                        setDeptSpinner(R.array.natural_dept)
                    }
                    "????????????" -> {
                        setDeptSpinner(R.array.engineer_dept)
                    }
                    "??????ICT????????????" -> {
                        setDeptSpinner(R.array.ict_dept)
                    }
                    "?????????????????????" -> {
                        setDeptSpinner(R.array.soft_dept)
                    }
                    "??????????????????" -> {
                        setDeptSpinner(R.array.biotech_dept)
                    }
                    "????????????" -> {
                        setDeptSpinner(R.array.art_dept)
                    }
                    "????????????" -> {
                        setDeptSpinner(R.array.phy_dept)
                    }
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                //TODO("Not yet implemented")
            }

        }
    }

    fun setDeptSpinner(deptName: Int) {
        val deptList = resources.getStringArray(deptName)
        var adapter = ArrayAdapter(
            this@RegisterActivity,
            com.naver.maps.map.R.layout.support_simple_spinner_dropdown_item,
            deptList
        )
        binding.spDept.adapter = adapter

        binding.spDept.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                registerRequestData.department= binding.spDept.getItemAtPosition(position).toString()
                hideKeyboard()
                deptCheck = position != 0
                allCheck()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //TODO("Not yet implemented")
            }

        }
    }

    fun allCheck() {
        Logger.e(
            "doori",
            "emailcheck = $emailCheck , clubCheck = $clubCheck, deptCheck = $deptCheck, invalidCheck = $invalidCheck, nameCheck = $nameCheck, stdIdCheck = $stuIdCheck, univCheck = $univCheck"
        )

        binding.apply {
            if (etName.text.isNotEmpty() && etStdId.text.isNotEmpty()) {
                nameCheck = true
                stuIdCheck = true
                registerRequestData.name = etName.text.toString()
                registerRequestData.userIdx=etStdId.text.toString().toInt()
            } else {
                nameCheck = false
                stuIdCheck = false
            }
            if (emailCheck && clubCheck && deptCheck && invalidCheck && nameCheck && stuIdCheck && univCheck) {
                btnRegister.isEnabled = true
            }
        }
    }

    fun initDept() {
        val initdept = listOf("??????????????? ???????????????.", "??????????????? ?????? ??????????????????.")
        val deptAdapter = ArrayAdapter(
            this,
            com.naver.maps.map.R.layout.support_simple_spinner_dropdown_item,
            initdept
        )
        binding.spDept.adapter = deptAdapter
    }

    fun textWatcher(){
        val pwPattern = "^(?=.*[a-z])(?=.*\\d)[a-zA-Z\\d]*$" // ??????, ?????? 8 ~ 20??? ??????

        binding.etPwd.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
               // TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //TODO("Not yet implemented")
            }

            override fun afterTextChanged(s: Editable?) {
                val pwd= binding.etPwd.text.toString()
                Logger.e("doori",pwd)
                Logger.e("doori",Pattern.matches(pwPattern,pwd).toString())
                if(Pattern.matches(pwPattern,pwd)){
                    binding.tvPwdNum.setTextColor(getColor(R.color.color_039400))
                }else{
                    binding.tvPwdNum.setTextColor(getColor(R.color.color_ebebeb))
                }

                if (pwd.length>=8){
                    binding.tvPwdLength.setTextColor(getColor(R.color.color_039400))
                }else{
                    binding.tvPwdLength.setTextColor(getColor(R.color.color_ebebeb))
                }
            }

        })
    }

    override fun onBackPressed() {
        Intent(this@RegisterActivity, LoginActivity::class.java).run {
            startActivity(this)
        }
        finishAffinity()
    }
}