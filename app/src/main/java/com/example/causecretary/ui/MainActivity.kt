package com.example.causecretary.ui

//import com.example.causecretary.naviAr.ARActivity

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.causecretary.R
import com.example.causecretary.adapter.AdminAdapter
import com.example.causecretary.adapter.EventAdapter
import com.example.causecretary.databinding.ActivityMainBinding
import com.example.causecretary.ui.api.ApiService
import com.example.causecretary.ui.api.RetrofitApi
import com.example.causecretary.ui.data.*
import com.example.causecretary.ui.data.Consts.Companion.LOCATION_PERMISSION_REQUEST_CODE
import com.example.causecretary.ui.event.EventActivity
import com.example.causecretary.ui.event.EventRegisterActivity
import com.example.causecretary.ui.utils.Logger
import com.example.causecretary.ui.utils.PrefManager
import com.example.causecretary.ui.utils.UiUtils
import com.example.causecretary.viewmodel.MainViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.*
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.activity_route.view.*
import kotlinx.android.synthetic.main.main_navi.view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


class MainActivity : AppCompatActivity(), View.OnClickListener, OnMapReadyCallback,
    Observer<EventOnResponse> {
    var distance:Float = 0.0f
    lateinit var binding: ActivityMainBinding
    private lateinit var naverMap: NaverMap
    private lateinit var mapView: MapView
    private lateinit var locationSource: FusedLocationSource
    private var viewModel: MainViewModel? = null

    lateinit var eventOffData: EventOffResponse
    lateinit var eventOnData: EventOnResponse
    var markerSoonList = mutableListOf<Marker>()
    var markerStuList = mutableListOf<Marker>()
    var markerClubList = mutableListOf<Marker>()
    var markerStructureList = mutableListOf<Marker>()
    var markerEtcList = mutableListOf<Marker>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel?.liveData?.observe(this, this)

        mapView = findViewById(R.id.map)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        locationSource =
            FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        initData()
        initView()
    }

    override fun onMapReady(@NonNull naverMap: NaverMap) {
        // 카메라 초기 위치 설정
        // 카메라 초기 위치 설정
        Logger.d("Navi", "onMapReady start")
        this.naverMap = naverMap
        val initialPosition = LatLng(37.50335, 126.9574114)
        //val cameraUpdate = CameraUpdate.scrollTo(initialPosition)
        //naverMap.moveCamera(cameraUpdate)
        val cameraPosition = CameraPosition(initialPosition, 16.0)
        naverMap.cameraPosition = cameraPosition
        naverMap.locationSource = locationSource

        //현재위치버튼
        naverMap.apply {
            uiSettings.isLocationButtonEnabled = true
            //locationTrackingMode=LocationTrackingMode.Follow
        }
    }

    private fun initView() {
        binding.clickListener = this@MainActivity
        setShowDimmed(true)
        drawable()
        getOffList()

        //로그인 정보가 있으면 draw를 다르게
       //settingDraw()
    }

    private fun settingDraw() {
        val loginData = PrefManager(this@MainActivity).getLoginData()
        binding.dlMain.apply {
            if (loginData == null) {
                tv_event.visibility = GONE
                //tv_event_plz.visibility = GONE
                tv_like.visibility = GONE
            } else {
                Logger.e("doori",loginData.toString())
                tv_login.visibility= GONE
                if(loginData.certified=="F"){
                    tv_event.visibility= GONE
                }else{
                  //  tv_event_plz.visibility=GONE
                }
            }
        }
    }

    private fun getOffList() {

        val retrofit = Retrofit.Builder()
            .baseUrl(ApiService.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val registerService = retrofit.create(RetrofitApi::class.java)
        registerService.getEventOff().enqueue(object : Callback<EventOffResponse> {
            override fun onResponse(
                call: Call<EventOffResponse>,
                response: Response<EventOffResponse>
            ) {
                eventOffData = response.body() as EventOffResponse
                val e= eventOffData.result
                for(a in e){
                    Logger.e("doori","PointIdx = ${a.pointIdx.toString()}   Location = ${a.location}")
                }
                //viewModel?.liveData?.postValue(registerResponse)
                Logger.e("doori", response.toString())
                Logger.e("doori", eventOffData.toString())

                getOnList()

            }

            override fun onFailure(call: Call<EventOffResponse>, t: Throwable) {
                Logger.e("doori", t.toString())
            }

        })
    }

    private fun getOnList() {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiService.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val registerService = retrofit.create(RetrofitApi::class.java)
        registerService.getEventOn().enqueue(object : Callback<EventOnResponse> {
            override fun onResponse(
                call: Call<EventOnResponse>,
                response: Response<EventOnResponse>
            ) {

                eventOnData = response.body() as EventOnResponse
                viewModel?.liveData?.postValue(eventOnData)
                //viewModel?.liveData?.postValue(registerResponse)
                Logger.e("doori", response.toString())
                Logger.e("doori", eventOnData.toString())

                setAllMarker()
            }

            override fun onFailure(call: Call<EventOnResponse>, t: Throwable) {
                Logger.e("doori", t.toString())
            }

        })
    }


    private fun drawable() {
        settingDraw()
        binding.dlMain.apply {
            tv_login.setOnClickListener {
                Intent(this@MainActivity, LoginActivity::class.java).run {
                    startActivity(this)
                }
            }
            tv_event.setOnClickListener {
                Intent(this@MainActivity, EventRegisterActivity::class.java).run {
                    startActivity(this)
                }
            }
            tv_hotline.setOnClickListener {
                UiUtils.showSnackBar(binding.root, "hotline")
            }
            tv_like.setOnClickListener {
                UiUtils.showSnackBar(binding.root, "like")
            }
            tv_setting.setOnClickListener {
                UiUtils.showSnackBar(binding.root, "setting")
            }
            tv_noti.setOnClickListener {
                UiUtils.showSnackBar(binding.root, "notice")
            }
        }
    }

    private fun initData() {
        //TODO("Not yet implemented")

    }


    override fun onClick(view: View?) {
        //TODO("Not yet implemented")
        when (view?.id) {
            R.id.iv_menu -> {
                //hideKeyboard()
                binding.dlMain.openDrawer(GravityCompat.START)
                //binding.flEvent.visibility = VISIBLE
            }
            R.id.btn_soon -> {
                hideKeyboard()
                binding.apply {
                    if (btnSoon.isSelected) {
                        setAllMarker()
                        btnSoon.isSelected = false
                    } else {
                        btnSoon.isSelected = true
                        btnClub.isSelected = false
                        btnEtc.isSelected = false
                        btnOnline.isSelected = false
                        btnStruct.isSelected = false
                        btnStu.isSelected = false
                        deletAllMarker()
                        setSoonMarkerSet()

                        Logger.d("Navi", "ArBtn click")
                        /*Intent(this@MainActivity, ARActivity::class.java).run {
                            startActivity(this)
                        }*/
                    }
                }

            }
            R.id.btn_club -> {
                hideKeyboard()
                binding.apply {
                    if (btnClub.isSelected) {
                        setAllMarker()
                        btnClub.isSelected = false
                    } else {
                        btnSoon.isSelected = false
                        btnClub.isSelected = true
                        btnEtc.isSelected = false
                        btnOnline.isSelected = false
                        btnStruct.isSelected = false
                        btnStu.isSelected = false
                        deletAllMarker()
                        setClubMarkerSet()
                    }
                }

            }
            R.id.btn_etc -> {
                hideKeyboard()
                binding.apply {
                    if (btnEtc.isSelected) {
                        setAllMarker()
                        btnEtc.isSelected = false
                    } else {
                        btnSoon.isSelected = false
                        btnClub.isSelected = false
                        btnEtc.isSelected = true
                        btnOnline.isSelected = false
                        btnStruct.isSelected = false
                        btnStu.isSelected = false
                        deletAllMarker()
                        setEtcMarkerSet()
                    }
                }

            }
            R.id.btn_struct -> {
                hideKeyboard()
                binding.apply {
                    if (btnStruct.isSelected) {
                        setAllMarker()
                        btnStruct.isSelected = false
                    } else {
                        btnSoon.isSelected = false
                        btnClub.isSelected = false
                        btnEtc.isSelected = false
                        btnOnline.isSelected = false
                        btnStruct.isSelected = true
                        btnStu.isSelected = false
                        deletAllMarker()
                        setStructureMarkerSet()
                    }
                }

            }
            R.id.btn_online -> {
                hideKeyboard()
                binding.apply {
                    if (btnOnline.isSelected) {
                        setAllMarker()
                        btnOnline.isSelected = false
                    } else {
                        btnSoon.isSelected = false
                        btnClub.isSelected = false
                        btnEtc.isSelected = false
                        btnOnline.isSelected = true
                        btnStruct.isSelected = false
                        btnStu.isSelected = false
                    }
                }
            }
            R.id.btn_stu -> {
                hideKeyboard()
                binding.apply {
                    if (btnStu.isSelected) {
                        setAllMarker()
                        btnStu.isSelected = false
                    } else {
                        btnSoon.isSelected = false
                        btnClub.isSelected = false
                        btnEtc.isSelected = false
                        btnOnline.isSelected = false
                        btnStruct.isSelected = false
                        btnStu.isSelected = true
                        deletAllMarker()
                        setStuMarkerSet()
                    }
                }

            }
            R.id.btn_search -> {
                hideKeyboard()
                Intent(this@MainActivity, RouteActivity::class.java).run {
                    startActivity(this)
                }
            }

            R.id.cl_sv -> {
                hideKeyboard()
            }
            R.id.ib_down->{
                val translateUp = AnimationUtils.loadAnimation(applicationContext,R.anim.translate_down)
                binding.flEvent.startAnimation(translateUp)
                binding.flEvent.visibility= GONE
            }
        }
    }

    fun hideKeyboard() {
        Toast.makeText(this, "눌럿나", Toast.LENGTH_SHORT).show()
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    override fun onChanged(t: EventOnResponse?) {
        Logger.e("doori", "onChanged = ${t.toString()}")

    }

    private fun setShowDimmed(isLoading: Boolean) {
        viewModel?.apply {
            if (isLoading) {
                showLoading()
            } else {
                hideLoading()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions,
                grantResults
            )
        ) {
            if (!locationSource.isActivated) { // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun setSoonMarkerSet() {
        val eventList = eventOffData.result
        for (e in eventList) {
            if (e.belong == "예정") {
                val marker = Marker()
                markerSoonList.add(marker)
                setMark(marker, e.latitude, e.longitude, R.drawable.ic_location_soon, e.eventName,e.pointIdx)
            }
        }
    }

    fun setStuMarkerSet() {
        val eventList = eventOffData.result
        for (e in eventList) {
            if (e.belong == "학생회") {
                val marker = Marker()
                markerStuList.add(marker)
                setMark(marker, e.latitude, e.longitude, R.drawable.ic_location_red, e.eventName,e.pointIdx)
            }
        }
    }

    fun setClubMarkerSet() {
        val eventList = eventOffData.result
        for (e in eventList) {
            if (e.belong == "동아리") {
                val marker = Marker()
                markerClubList.add(marker)
                setMark(marker, e.latitude, e.longitude, R.drawable.ic_location_club, e.eventName,e.pointIdx)
            }
        }
    }

    fun setStructureMarkerSet() {
        val eventList = eventOffData.result
        for (e in eventList) {
            if (e.belong == "건물") {
                val marker = Marker()
                markerStructureList.add(marker)
                setMark(
                    marker,
                    e.latitude,
                    e.longitude,
                    R.drawable.ic_location_structure,
                    e.eventName,e.pointIdx
                )
            }
        }
    }

    fun setEtcMarkerSet() {
        val eventList = eventOffData.result
        for (e in eventList) {
            if (e.belong == "기타") {
                val marker = Marker()
                markerEtcList.add(marker)
                setMark(marker, e.latitude, e.longitude, R.drawable.ic_location_etc, e.eventName,e.pointIdx)
            }
        }
    }

    fun setAllMarker() {
        setClubMarkerSet()
        setEtcMarkerSet()
        setSoonMarkerSet()
        setStuMarkerSet()
        setStructureMarkerSet()
        setShowDimmed(false)
    }

    fun deletAllMarker() {
        markerStuList.apply {
            for (e in markerStuList) {
                e.map = null
            }
            removeAll(this)
        }
        markerClubList.apply {
            for (e in markerClubList) {
                e.map = null
            }
            removeAll(this)
        }
        markerEtcList.apply {
            for (e in markerEtcList) {
                e.map = null
            }
            removeAll(this)
        }
        markerSoonList.apply {
            for (e in markerSoonList) {
                e.map = null
            }
            removeAll(this)
        }
        markerStructureList.apply {
            for (e in markerStructureList) {
                e.map = null
            }
            removeAll(this)
        }
    }

    private fun setMark(
        marker: Marker,
        lat: Double,
        lng: Double,
        resourceID: Int,
        eventName: String,pointIdx: Int
    ) {
        //원근감 표시
        marker.isIconPerspectiveEnabled = true
        //아이콘 지정
        marker.icon = OverlayImage.fromResource(resourceID)
        //마커의 투명도
        marker.alpha = 0.8f
        //마커 위치
        marker.position = LatLng(lat, lng)
        //마커 우선순위
        marker.zIndex = 15
        //마커 표시
        marker.map = naverMap
        //마커 텍스트크기
        marker.captionTextSize = 13f
        //마커 텍스트
        marker.captionText = eventName
        //마커크기
        marker.width = 100
        marker.height = 100


        val list = eventOffData.result
        marker.onClickListener = Overlay.OnClickListener {
            for (e in list) {
                if (e.pointIdx == pointIdx) {
                    eventBuildingList(e.pointIdx)
                    break
                }
            }
            false
        }
    }


    //겹치면 동시에 돼가지고 엄청 많아짐
    //같은 건물에 있는 이벤트들 표시
    private fun eventBuildingList(pointIdx: Int) {
        val eventBuildList= mutableListOf<EventOffResult>()
        Logger.e("doori",pointIdx.toString())

        var c=0
        for(e in eventOffData.result){
            if (e.pointIdx.equals(pointIdx)){
                eventBuildList.add(e)
                c++
                Logger.e("doori","Point =${pointIdx.toString()},  PointIdx = ${e.pointIdx}  location = ${e.location}")
            }

        }
        Logger.e("doori",eventBuildList.toString())
        Logger.e("doori",c.toString())
        var adapter = EventAdapter(eventBuildList)
        //애니메이션
        val translateUp = AnimationUtils.loadAnimation(applicationContext,R.anim.translate_up)
        binding.rcEvent.adapter = adapter
        binding.rcEvent.layoutManager=LinearLayoutManager(this)
        binding.flEvent.visibility= VISIBLE
        binding.flEvent.startAnimation(translateUp)

        adapter.setItemClickListener(object : EventAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val eventOff=adapter.getList(position)
                Intent(this@MainActivity,EventActivity::class.java).run {
                    putExtra("eventIdx",eventOff.eventIdx)
                    startActivity(this)
                }
            }
        })
    }
}