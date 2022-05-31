package com.example.causecretary.ui

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.causecretary.R
import com.example.causecretary.databinding.ActivityMainBinding
import com.example.causecretary.ui.api.ApiService
import com.example.causecretary.ui.api.RetrofitApi
import com.example.causecretary.ui.utils.Logger
import com.example.causecretary.naviAr.ArActivity
import com.example.causecretary.ui.data.EventOffResponse
import com.example.causecretary.ui.data.EventOnResponse
import com.example.causecretary.ui.data.dto.AdminRequestData
import com.example.causecretary.ui.event.EventRegisterActivity
import com.example.causecretary.ui.utils.UiUtils
import com.example.causecretary.viewmodel.MainViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.MultipartPathOverlay
import com.naver.maps.map.overlay.PathOverlay
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
    lateinit var binding: ActivityMainBinding
    private lateinit var naverMap: NaverMap
    private lateinit var mapView: MapView
    var multipartPathOverlay = MultipartPathOverlay()
    lateinit var routingService: RetrofitApi
    private var viewModel: MainViewModel? = null

    lateinit var eventOffData:EventOffResponse
    lateinit var eventOnData:EventOnResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel?.liveData?.observe(this,this)

        mapView = findViewById(R.id.map)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        initData()
        initView()
    }

    override fun onMapReady(@NonNull naverMap: NaverMap) {
        // 카메라 초기 위치 설정
        // 카메라 초기 위치 설정
        Logger.d("Navi", "onMapReady start")
        this.naverMap = naverMap
        val initialPosition = LatLng(37.50335, 126.9574114)
        val cameraUpdate = CameraUpdate.scrollTo(initialPosition)
        naverMap.moveCamera(cameraUpdate)

        // 백엔드 통신  retrofit 설정
        //baseURl http://~~~:8000/
        // ~~~ 부분에 ipconfig ipv4 주소 넣어야 함
        var retrofit = Retrofit.Builder()
            .baseUrl(ApiService.NAVIDOMAIN)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        routingService = retrofit.create(RetrofitApi::class.java)

    }

    private fun initView() {
        binding.clickListener = this@MainActivity

        drawable()

        getOffList()
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
                //viewModel?.liveData?.postValue(registerResponse)
                Logger.e("doori",response.toString())
                Logger.e("doori",eventOffData.toString())

                getOnList()

            }

            override fun onFailure(call: Call<EventOffResponse>, t: Throwable) {
                Logger.e("doori",t.toString())
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
                Logger.e("doori",response.toString())
                Logger.e("doori",eventOnData.toString())


            }

            override fun onFailure(call: Call<EventOnResponse>, t: Throwable) {
                Logger.e("doori",t.toString())
            }

        })
    }

    private fun drawable() {
        binding.dlMain.apply {
            tv_login.setOnClickListener {
                Intent(this@MainActivity, LoginActivity::class.java).run {
                    startActivity(this)
                }
            }
            tv_event.setOnClickListener {
                Intent(this@MainActivity,EventRegisterActivity::class.java).run {
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
                hideKeyboard()
                binding.dlMain.openDrawer(GravityCompat.START)
            }
            R.id.btn_soon -> {
                hideKeyboard()
                binding.apply {
                    if (btnSoon.isSelected) {
                        btnSoon.isSelected = false
                    } else {
                        btnSoon.isSelected = true
                        btnClub.isSelected = false
                        btnEtc.isSelected = false
                        btnStruct.isSelected = false
                        btnStu.isSelected = false

                        Logger.d("Navi", "ArBtn click")
                        Intent(this@MainActivity, ArActivity::class.java).run {
                            startActivity(this)
                        }
                    }
                }
            }
            R.id.btn_club -> {
                hideKeyboard()
                binding.apply {
                    if (btnClub.isSelected) {
                        btnClub.isSelected = false
                    } else {
                        btnSoon.isSelected = false
                        btnClub.isSelected = true
                        btnEtc.isSelected = false
                        btnStruct.isSelected = false
                        btnStu.isSelected = false
                    }
                }
            }
            R.id.btn_etc -> {
                hideKeyboard()
                binding.apply {
                    if (btnEtc.isSelected) {
                        btnEtc.isSelected = false
                    } else {
                        btnSoon.isSelected = false
                        btnClub.isSelected = false
                        btnEtc.isSelected = true
                        btnStruct.isSelected = false
                        btnStu.isSelected = false
                    }
                }
            }
            R.id.btn_struct -> {
                hideKeyboard()
                binding.apply {
                    if (btnStruct.isSelected) {
                        btnStruct.isSelected = false
                    } else {
                        btnSoon.isSelected = false
                        btnClub.isSelected = false
                        btnEtc.isSelected = false
                        btnStruct.isSelected = true
                        btnStu.isSelected = false
                    }
                }
            }
            R.id.btn_stu -> {
                hideKeyboard()
                binding.apply {
                    if (btnStu.isSelected) {
                        btnStu.isSelected = false
                    } else {
                        btnSoon.isSelected = false
                        btnClub.isSelected = false
                        btnEtc.isSelected = false
                        btnStruct.isSelected = false
                        btnStu.isSelected = true
                    }
                }
            }
            R.id.btn_search -> {
                hideKeyboard()
                var endNode = findViewById<EditText>(R.id.et_search).text.toString()
                var curLat: Double
                var curLon: Double
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    return
                } else {
                    val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    val curLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    curLon = curLocation?.longitude!!
                    curLat = curLocation.latitude
                }

                Logger.d("Navi", "curLon: $curLon curLat: $curLat")
                Logger.d("Navi", "endNode: $endNode")


                routingService.searchRoute_weigh(endNode, curLat.toString(), curLon.toString())
                    .enqueue(object : Callback<String> {
                        override fun onFailure(call: Call<String>, t: Throwable) {
                            //실패할 경우
                            Log.d("DEBUG", t.message.toString())
                            var dialog = AlertDialog.Builder(this@MainActivity)
                            dialog.setTitle("에러")
                            dialog.setMessage("통신에 실패했습니다.")
                            dialog.show()
                        }

                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            //정상응답이 올경우
                            var result: String
                            result = response.body().toString()
                            println(result)

                            var searchResult: JSONObject
                            searchResult = JSONObject(result)

                            parseJSON(searchResult)
                        }
                    })
            }

            R.id.cl_sv -> {
                hideKeyboard()
            }
        }
    }

    fun hideKeyboard() {
        Toast.makeText(this, "눌럿나", Toast.LENGTH_SHORT).show()
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    fun parseJSON(json: JSONObject) {
        var nodes = json.getJSONArray("nodes")
        var edges = json.getJSONArray("edges")


        for (i in 0 until nodes.length()) {
            val node = nodes.getJSONObject(i)
            val id = node.getString("id")
            val lat = node.getDouble("lat")
            val lon = node.getDouble("lon")
            val name = node.getString("name")
            Log.d(ContentValues.TAG, "id($i): $id")
            Log.d(ContentValues.TAG, "lat($i): $lat")
            Log.d(ContentValues.TAG, "lon($i): $lon")
            Log.d(ContentValues.TAG, "name($i): $name")
        }

        var pathOverlay = PathOverlay()
        multipartPathOverlay.map = null
        var paths: MutableList<MutableList<LatLng>> = ArrayList()
//        val seqList: MutableList<MutableList<LatLng>> = ArrayList() // alternatively: = mutableListOf()
//        seqList.add(mutableListOf<Int>(1, 2, 3))

        for (i in 0 until edges.length()) {
            val edge = edges.getJSONObject(i)
            val path = edge.getString("path")
            val start = edge.getInt("start")
            val end = edge.getInt("end")
            val type = edge.getString("type")
            val weigh = edge.getInt("weigh")
            val distance = edge.getInt("distance")

            val path_coords_list = (path.substring(1, path.lastIndex)).split("|")
            Log.d(ContentValues.TAG, "path($i): $start => $end : $path_coords_list")

            var path_container: MutableList<LatLng> = mutableListOf(LatLng(0.1, 0.1))
            for (path_coords in path_coords_list) {
                val coords = path_coords.split(" ")
                val coord_lon = coords[0].toDouble()
                val coord_lat = coords[1].toDouble()
                //구한 경로를 하나씩 path_container에 추가해줌
                path_container.add(LatLng(coord_lat, coord_lon))
            }
            path_container = (path_container.drop(1) as MutableList<LatLng>?)!!
            paths.add(path_container)
        }

        for (p in paths) {
            println(p.toString())
        }

        multipartPathOverlay.coordParts = paths
        multipartPathOverlay.colorParts = listOf(
            MultipartPathOverlay.ColorPart(
                Color.GREEN, Color.WHITE, Color.DKGRAY, Color.LTGRAY
            )
        )
        //더미원소 드랍후 path.coords에 path들을 넣어줌.
//        pathOverlay.coords = path_container
//        pathOverlay.color = Color.GREEN
//        pathOverlay.map = naverMap
        multipartPathOverlay.map = naverMap
    }

    override fun onChanged(t: EventOnResponse?) {
        Logger.e("doori","onChanged = ${t.toString()}")

    }
}