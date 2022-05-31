package com.example.causecretary.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.causecretary.ui.data.AdminResponse

class MainViewModel:ViewModel() {
    var liveData: MutableLiveData<AdminResponse> = MutableLiveData<AdminResponse>()
    var dimmed:Boolean = true

    fun dimmedDown(){
        dimmed = false
    }
}