package com.example.causecretary.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.causecretary.R
import com.example.causecretary.databinding.ActivitySplashBinding
import com.example.causecretary.databinding.PermissionDialogBinding
import com.example.causecretary.ui.dialog.CustomDialog
import com.example.causecretary.ui.utils.Logger
import com.example.causecretary.ui.utils.PrefManager
import com.example.causecretary.ui.utils.UiUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CAMERA_PERMISSION = 7777
        const val REQUEST_FINE_LOCATION_PERMISSION = 9999
        const val REQUEST_GALLERY_PERMISSION = 5555

    }

    lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        initData()
        initView()

    }

    private fun initView() {
        //최초 앱 실행 시 permission창이 뜨고 permission체크했으면 다음부터는 그냥 넘어가도록
        PrefManager(this@SplashActivity).apply {
            if (isPermissionConfirm()) {
                checkPermission(REQUEST_GALLERY_PERMISSION)
            } else {
                DataBindingUtil.inflate<PermissionDialogBinding>(LayoutInflater.from(this@SplashActivity), R.layout.permission_dialog, null, false).apply {
                    this.permissionDialog = UiUtils.showCustomDialog(
                        this@SplashActivity,
                        root,
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT,
                        object : CustomDialog.DialogClickListener {
                            override fun onClose() {
                                // nothing
                            }

                            override fun onConfirm() {
                                setPermissionConfirm(true)
                                checkPermission(REQUEST_FINE_LOCATION_PERMISSION)
                            }
                        })
                }
            }
        }
    }

    private fun initData() {
       // TODO("Not yet implemented")
    }


    /**
     *  권한
     **/
    private fun checkPermission(requestCode: Int) {
        if (requestCode==REQUEST_GALLERY_PERMISSION){
            if (ContextCompat.checkSelfPermission(this@SplashActivity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_GALLERY_PERMISSION)
            } else {
                checkPermission(REQUEST_FINE_LOCATION_PERMISSION)
            }
        }
        else if(requestCode==REQUEST_FINE_LOCATION_PERMISSION){
            if (ContextCompat.checkSelfPermission(this@SplashActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_FINE_LOCATION_PERMISSION)
            } else {
                checkPermission(REQUEST_CAMERA_PERMISSION)
            }

        }
        else if(requestCode== REQUEST_CAMERA_PERMISSION){
            if (ContextCompat.checkSelfPermission(this@SplashActivity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
            } else {
                goLoginpage()
                //ViewModel 설정
                /*getBinding()?.apply {
                    mSplashViewModel = ViewModelProvider(this@SplashActivity, SplashViewModel.Factory()).get(SplashViewModel::class.java).apply {
                        splashViewModel = this
                        getCurrentVersion().observe(this@SplashActivity, this@SplashActivity)
                    }
                }
                requestVersionOk()*/
            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_FINE_LOCATION_PERMISSION->{
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Logger.e("doori", "granted")
                    checkPermission(REQUEST_CAMERA_PERMISSION)
                    //requestVersionOk()
                } else {
                    Logger.e("doori", "not granted")
                    checkPermission(REQUEST_CAMERA_PERMISSION)
                    //requestVersionOk()
                }

            }
            REQUEST_CAMERA_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Logger.e("doori", "granted")
                    goLoginpage()
                    //requestVersionOk()
                } else {
                    Logger.e("doori", "not granted")
                    goLoginpage()
                    //requestVersionOk()
                }
            }
            REQUEST_GALLERY_PERMISSION ->{
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Logger.e("doori", "granted")
                    checkPermission(REQUEST_FINE_LOCATION_PERMISSION)
                    //requestVersionOk()
                } else {
                    Logger.e("doori", "not granted")
                    checkPermission(REQUEST_FINE_LOCATION_PERMISSION)
                    //requestVersionOk()
                }
            }
        }
    }

    private fun goLoginpage(){
        //프로그래스바 1초동안
        CoroutineScope(Dispatchers.Default).launch {
            delay(1000)
            Intent(this@SplashActivity, MainActivity::class.java).run {
                startActivity(this)
            }
        }
    }
}