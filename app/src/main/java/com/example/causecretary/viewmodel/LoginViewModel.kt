package com.example.causecretary.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.causecretary.ui.data.AdminResponse
import com.example.causecretary.ui.data.RegisterResponse

class LoginViewModel:ViewModel() {
    var liveData: MutableLiveData<AdminResponse> = MutableLiveData<AdminResponse>()
    val mIsLoading: ObservableField<Boolean> = ObservableField<Boolean>()

    init {
        hideLoading()
    }

    fun showLoading() {
        mIsLoading.set(true)
    }

    fun hideLoading() {
        mIsLoading.set(false)
    }

}