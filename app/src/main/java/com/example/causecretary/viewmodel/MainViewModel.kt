package com.example.causecretary.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.causecretary.ui.data.AdminResponse
import com.example.causecretary.ui.data.EventOnResponse

class MainViewModel:ViewModel() {
    var liveData: MutableLiveData<EventOnResponse> = MutableLiveData<EventOnResponse>()
    var dimmed:Boolean = true

    fun dimmedDown(){
        dimmed = false
    }
}