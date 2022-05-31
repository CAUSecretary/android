package com.example.causecretary.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.causecretary.ui.data.AdminResponse
import com.example.causecretary.ui.data.RegisterResponse

class LoginViewModel:ViewModel() {
    var liveData: MutableLiveData<RegisterResponse> = MutableLiveData<RegisterResponse>()
}