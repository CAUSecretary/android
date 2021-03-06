package com.example.causecretary.ui

//import com.example.causecretary.naviAr.ARActivity

import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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
import com.example.causecretary.ui.event.EventAdminActivity
import com.example.causecretary.ui.event.EventRegisterActivity
import com.example.causecretary.ui.utils.Logger
import com.example.causecretary.ui.utils.PrefManager
import com.example.causecretary.ui.utils.UiUtils
import com.example.causecretary.viewmodel.MainViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.*
import com.naver.maps.map.util.FusedLocationSource
/*import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.activity_route.view.*
import kotlinx.android.synthetic.main.main_navi.view.*
import org.json.JSONObject*/
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity(), View.OnClickListener, OnMapReadyCallback,
    Observer<EventOnResponse> {
    var distance:Float = 0.0f

    //???????????? ?????? ????????? ?????????
    private var mBackBtnPresses: Boolean = false

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
        // ????????? ?????? ?????? ??????
        // ????????? ?????? ?????? ??????
        Logger.d("Navi", "onMapReady start")
        this.naverMap = naverMap
        val initialPosition = LatLng(37.50335, 126.9574114)
        //val cameraUpdate = CameraUpdate.scrollTo(initialPosition)
        //naverMap.moveCamera(cameraUpdate)
        val cameraPosition = CameraPosition(initialPosition, 16.0)
        naverMap.cameraPosition = cameraPosition
        naverMap.locationSource = locationSource

        //??????????????????
        naverMap.apply {
            uiSettings.isLocationButtonEnabled = true
            //locationTrackingMode=LocationTrackingMode.Follow
        }
    }

    private fun initView() {
        binding.clickListener = this@MainActivity
        setShowDimmed(true)
        getOffList()
        setSpinner()
        settingDraw()
        val loginData = PrefManager(this@MainActivity).getLoginData()
        Logger.e("doori","${loginData.toString()}")
        //????????? ????????? ????????? draw??? ?????????
        //settingDraw()
    }

    private fun setSpinner() {
        hideKeyboard()
        val buildingList = resources.getStringArray(R.array.structure_name)
        val buildingAdapter = ArrayAdapter(
            this,
            com.naver.maps.map.R.layout.support_simple_spinner_dropdown_item, buildingList
        )
        binding.spSearch.adapter = buildingAdapter

    }

    private fun settingDraw() {
        val loginData = PrefManager(this@MainActivity).getLoginData()
        binding.apply {
            if (loginData == null) {
                tvEvent.visibility= GONE
                tvEventPlz.visibility=GONE
                tvAdmin.visibility=GONE
                ibLogout.visibility=GONE
            } else {
                ibLogout.visibility= VISIBLE
                tvLogin.visibility=GONE
                tvAdmin.visibility= VISIBLE
                Logger.e("doori",loginData.toString())
                if(loginData.certified=="F"){
                    tvEvent.visibility=GONE
                }else{
                    tvEventPlz.visibility=GONE
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


    private fun initData() {
        //TODO("Not yet implemented")

    }


    override fun onClick(view: View?) {
        //TODO("Not yet implemented")
        when (view?.id) {
            R.id.iv_menu -> {
                //???????????????
                val translateLeft = AnimationUtils.loadAnimation(applicationContext,R.anim.translate_left)
                binding.flNavi.startAnimation(translateLeft)
                binding.flNavi.visibility= VISIBLE

            }
            R.id.ib_logout->{
                PrefManager(this).removeLoginData()
                finish()
                Intent(this@MainActivity,MainActivity::class.java).run {
                    startActivity(this)
                    finishAffinity()
                }
            }
            R.id.ib_close->{
                //???????????????
                val translateLeft = AnimationUtils.loadAnimation(applicationContext,R.anim.translate_left_gone)
                binding.flNavi.startAnimation(translateLeft)
                binding.flNavi.visibility= GONE
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
                    putExtra("endPoint",binding.spSearch.selectedItem.toString())
                    startActivity(this)
                    finishAffinity()
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
            R.id.tv_login ->{
                Intent(this@MainActivity, LoginActivity::class.java).run {
                    startActivity(this)
                }
            }
            R.id.tv_event->{
                Intent(this@MainActivity,EventRegisterActivity::class.java).run {
                    startActivity(this)
                }
            }
            R.id.tv_event_plz->{
                val builder = AlertDialog.Builder(this@MainActivity)
                    .setTitle("?????? ?????? ???")
                    .setMessage("????????? ????????? ?????? ??????????????? ?????? ????????????.\n??????????????? 20~30??? ?????????.")
                    .setPositiveButton("??????",
                        DialogInterface.OnClickListener { dialog, which ->
                            Toast.makeText(this@MainActivity, "??????", Toast.LENGTH_SHORT)
                                .show()
                        })
                    .setNegativeButton("??????",
                        DialogInterface.OnClickListener { dialog, which ->
                            Toast.makeText(this@MainActivity, "??????", Toast.LENGTH_SHORT)
                                .show()
                        })
                builder.show()
            }
            R.id.tv_admin->{
                Intent(this@MainActivity,EventAdminActivity::class.java).run {
                    startActivity(this)
                    finishAffinity()
                }
            }
        }
    }

    fun hideKeyboard() {
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
            if (!locationSource.isActivated) { // ?????? ?????????
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun setSoonMarkerSet() {
        val eventList = eventOffData.result
        for (e in eventList) {
            if (e.belong == "??????") {
                val marker = Marker()
                markerSoonList.add(marker)
                setMark(marker, e.latitude, e.longitude, R.drawable.ic_location_soon, e.eventName,e.pointIdx)
            }
        }
    }

    fun setStuMarkerSet() {
        val eventList = eventOffData.result
        for (e in eventList) {
            if (e.belong == "?????????") {
                val marker = Marker()
                markerStuList.add(marker)
                setMark(marker, e.latitude, e.longitude, R.drawable.ic_location_red, e.eventName,e.pointIdx)
            }
        }
    }

    fun setClubMarkerSet() {
        val eventList = eventOffData.result
        for (e in eventList) {
            if (e.belong == "?????????") {
                val marker = Marker()
                markerClubList.add(marker)
                setMark(marker, e.latitude, e.longitude, R.drawable.ic_location_club, e.eventName,e.pointIdx)
            }
        }
    }

    fun setStructureMarkerSet() {
        val eventList = eventOffData.result
        for (e in eventList) {
            if (e.belong == "??????") {
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
            if (e.belong == "??????") {
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
        //????????? ??????
        marker.isIconPerspectiveEnabled = true
        //????????? ??????
        marker.icon = OverlayImage.fromResource(resourceID)
        //????????? ?????????
        marker.alpha = 0.8f
        //?????? ??????
        marker.position = LatLng(lat, lng)
        //?????? ????????????
        marker.zIndex = 15
        //?????? ??????
        marker.map = naverMap
        //?????? ???????????????
        marker.captionTextSize = 13f
        //?????? ?????????
        marker.captionText = eventName
        //????????????
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


    //????????? ????????? ???????????? ?????? ?????????
    //?????? ????????? ?????? ???????????? ??????
    private fun eventBuildingList(pointIdx: Int) {
        val eventBuildList= mutableListOf<EventOffResult>()
        Logger.e("doori",pointIdx.toString())

        for(e in eventOffData.result){
            if (e.pointIdx.equals(pointIdx)){
                eventBuildList.add(e)
                Logger.e("doori","Point =${pointIdx.toString()},  PointIdx = ${e.pointIdx}  location = ${e.location}")
            }

        }

        var adapter = EventAdapter(eventBuildList)
        //???????????????
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


    override fun onResume() {
        super.onResume()
        initView()
        initData()
    }

    override fun onBackPressed() {
        binding.run {
            if (mBackBtnPresses) {
                mBackBtnPresses = false
                exitApp()
            } else {
                UiUtils.showSnackBar(root, "'??????' ????????? ?????? ??? ???????????? ???????????????.")
                mBackBtnPresses = true
                Handler(Looper.getMainLooper()).postDelayed({ mBackBtnPresses = false }, 2500)
            }
        }
    }

    private fun exitApp() {
        ActivityCompat.finishAffinity(this)
        System.runFinalization()
        exitProcess(0)
    }


}