package com.example.causecretary.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import com.example.causecretary.R
import com.example.causecretary.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        initData()
        initView()
    }

    private fun initView() {
        binding.clickListener=this@MainActivity
    }

    private fun initData() {
        //TODO("Not yet implemented")
    }

    override fun onClick(view: View?) {
        //TODO("Not yet implemented")
        when(view?.id){
            R.id.iv_menu -> {
                hideKeyboard()
                binding.dlMain.openDrawer(GravityCompat.START)
            }
            R.id.btn_soon->{
                hideKeyboard()
                binding.apply {
                    if(btnSoon.isSelected){
                        btnSoon.isSelected=false
                    }else {
                        btnSoon.isSelected = true
                        btnClub.isSelected = false
                        btnEtc.isSelected = false
                        btnStruct.isSelected = false
                        btnStu.isSelected = false
                    }
                }
            }
            R.id.btn_club->{
                hideKeyboard()
                binding.apply {
                    if(btnClub.isSelected){
                        btnClub.isSelected=false
                    }else {
                        btnSoon.isSelected = false
                        btnClub.isSelected = true
                        btnEtc.isSelected = false
                        btnStruct.isSelected = false
                        btnStu.isSelected = false
                    }
                }
            }
            R.id.btn_etc->{
                hideKeyboard()
                binding.apply {
                    if(btnEtc.isSelected){
                        btnEtc.isSelected=false
                    }else {
                        btnSoon.isSelected = false
                        btnClub.isSelected = false
                        btnEtc.isSelected = true
                        btnStruct.isSelected = false
                        btnStu.isSelected = false
                    }
                }
            }
            R.id.btn_struct->{
                hideKeyboard()
                binding.apply {
                    if(btnStruct.isSelected){
                        btnStruct.isSelected=false
                    }else {
                        btnSoon.isSelected = false
                        btnClub.isSelected = false
                        btnEtc.isSelected = false
                        btnStruct.isSelected = true
                        btnStu.isSelected = false
                    }
                }
            }
            R.id.btn_stu->{
                hideKeyboard()
                binding.apply {
                    if(btnStu.isSelected){
                        btnStu.isSelected=false
                    }else {
                        btnSoon.isSelected = false
                        btnClub.isSelected = false
                        btnEtc.isSelected = false
                        btnStruct.isSelected = false
                        btnStu.isSelected = true
                    }
                }
            }
            R.id.cl_search->{
                hideKeyboard()
            }
            R.id.cl_sv->{
                hideKeyboard()
            }
        }
    }

    fun hideKeyboard() {
        Toast.makeText(this,"눌럿나",Toast.LENGTH_SHORT).show()
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken,0)
    }
}