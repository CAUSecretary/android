package com.example.causecretary.ui

import android.Manifest
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.example.causecretary.R
import com.example.causecretary.databinding.ActivityRouteBinding
import com.example.causecretary.naviAr.ARActivity
import com.example.causecretary.naviAr.latLng
import com.example.causecretary.ui.api.ApiService
import com.example.causecretary.ui.api.RetrofitApi
import com.example.causecretary.ui.data.Consts
import com.example.causecretary.ui.utils.Logger
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.MultipartPathOverlay
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.util.FusedLocationSource
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class RouteActivity : AppCompatActivity(), View.OnClickListener, OnMapReadyCallback {
    lateinit var binding: ActivityRouteBinding
    private lateinit var naverMap: NaverMap
    private lateinit var mapView: MapView
    var multipartPathOverlay = MultipartPathOverlay()
    lateinit var routingService: RetrofitApi
    private lateinit var locationSource: FusedLocationSource
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var endPoint: String
    private var currentLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_route)
        mapView = findViewById(R.id.map)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        locationSource =
            FusedLocationSource(this, Consts.LOCATION_PERMISSION_REQUEST_CODE)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        initData()
        initView()
    }

    override fun onMapReady(naverMap: NaverMap) {
        // ????????? ?????? ?????? ??????
        // ????????? ?????? ?????? ??????
        Logger.d("Navi", "onMapReady start")
        this.naverMap = naverMap

        naverMap.locationSource = locationSource

        getCurrentLocation {
            var latLng: LatLng = LatLng(it.latitude, it.longitude)
            val cameraUpdate = CameraUpdate.scrollTo(latLng)
            naverMap.moveCamera(cameraUpdate)

        }

        //??????????????????
        naverMap.apply {
            uiSettings.isLocationButtonEnabled = true
            //locationTrackingMode=LocationTrackingMode.Follow
        }
        // ????????? ??????  retrofit ??????
        //baseURl http://~~~:8000/
        // ~~~ ????????? ipconfig ipv4 ?????? ????????? ???
        var retrofit = Retrofit.Builder()
            .baseUrl(ApiService.NAVIDOMAIN)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        routingService = retrofit.create(RetrofitApi::class.java)
    }

    private fun initView() {
        binding.goAr.isEnabled = false
        binding.clickListener = this@RouteActivity
        //setSpinner()

        /**
         * ???????????? ??? ???????????? ??????????????? ???????????? ????????? ??????????????????, ???????????? ?????? ?????? ??????????????? ???????
         * ?????? ???????????? ???????????? ????????? ????????? ????????? ??????????????????.
         */
    }

    private fun initData() {
        Logger.d("doori", intent.getStringExtra("eventRoute").toString())
        //?????? ???????????? ?????????
        intent.getStringExtra("endPoint")?.run {
            endPoint = getEndPointIdx(this)
            binding.etEnd.text=endPoint
            Logger.e("doori", "end Name = $this , ?????? = ${endPoint.toString()}")
        }


        //?????? ??????????????? ??????????????????
        intent.getStringExtra("eventRoute")?.run {
            endPoint=getEndPointIdx(this)
            binding.etEnd.text=endPoint
            Logger.e("doori", "end Name = $this , ?????? = ${endPoint.toString()}")
        }

    }
    private fun getCurrentLocation(onSuccess: (Location) -> Unit) {
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
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            currentLocation = location
            onSuccess(location)
        }.addOnFailureListener {
            Log.e(TAG, "Could not get location")
        }
    }

    private fun getEndPointIdx(endPoint: String?): String {
        when (endPoint) {
            "208??? ???2?????????" , "2"-> {
                return "208???"
            }
            "207??? ???1????????? ????????????", "3" -> {
                return "207???"
            }
            "204??? ???????????????", "4" -> {
                return "204???"
            }
            "203??? ????????????", "5" -> {
                return "203???"
            }
            "???????????? ?????????", "6" -> {
                return "????????????"
            }
            "201??? ??????", "7" -> {
                return "201???"
            }
            "303??? ?????????", "8" -> {
                return "303???"
            }
            "302??? ?????????", "9" -> {
                return "302???"
            }
            "310??? 100?????? ?????????", "10" -> {
                return "310???"
            }
            "?????????", "11" -> {
                return "?????????"
            }
            "107??? ????????????", "12" -> {
                return "107???"
            }
            "101??? ?????????", "13" -> {
                return "101???"
            }
            "??????", "14" -> {
                return "??????"
            }
        }
        return "??????"
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ib_switch -> {
                //????????? ????????? ?????????
            }
            R.id.ib_close -> {
                Intent(this@RouteActivity,MainActivity::class.java).run {
                    startActivity(this)
                    finishAffinity()
                }
            }
            R.id.btn_route -> {
                binding.goAr.isEnabled = true

                //????????? ?????? ??????


                var curLon: Double = currentLocation!!.longitude
                var curLat: Double = currentLocation!!.latitude

                Logger.d("Navi", "curLon: $curLon curLat: $curLat")
                Logger.d("Navi", "endNode: $endPoint")


                routingService.searchRoute_weigh(endPoint, curLat.toString(), curLon.toString())
                    .enqueue(object : Callback<String> {
                        override fun onFailure(call: Call<String>, t: Throwable) {
                            //????????? ??????
                            Log.d("DEBUG", t.message.toString())
                            var dialog = AlertDialog.Builder(this@RouteActivity)
                            dialog.setTitle("??????")
                            dialog.setMessage("????????? ??????????????????.")
                            dialog.show()
                        }

                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            //??????????????? ?????????
                            var result: String
                            result = response.body().toString()
                            println(result)

                            var searchResult: JSONObject
                            searchResult = JSONObject(result)

                            parseJSON(searchResult)
                        }
                    })
            }
            R.id.go_ar -> {
                //TODO AR???????????? ??????

                val intent = Intent(this@RouteActivity, ARActivity::class.java)

                intent.putExtra("endNode", endPoint)

                Toast.makeText(this, "ar", Toast.LENGTH_SHORT).show()
                Intent(this@RouteActivity, ARActivity::class.java).run {
                    startActivity(intent)
                }
            }
        }
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
                //?????? ????????? ????????? path_container??? ????????????
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
        //???????????? ????????? path.coords??? path?????? ?????????.
//        pathOverlay.coords = path_container
//        pathOverlay.color = Color.GREEN
//        pathOverlay.map = naverMap
        multipartPathOverlay.map = naverMap
    }
    /*
    private fun setSpinner() {
        hideKeyboard()
        /**
         * res - values - strings?????? string array strucrure_name??? ???????????? ???????????????.
         */
        val buildingList = resources.getStringArray(R.array.structure_name)
        val buildingAdapter = ArrayAdapter(
            this,
            com.naver.maps.map.R.layout.support_simple_spinner_dropdown_item, buildingList
        )
        binding.spEnd.adapter = buildingAdapter


        binding.spEnd.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Logger.e("doori",binding.spEnd.getItemAtPosition(position).toString())

                /**
                 * ??????????????? ????????? ???????????? ?????? ????????? ???????????? ????????? ?????? ????????????????????????.
                 * val startPoint = binding.etStart.text.toString()
                 */
                when (binding.spEnd.getItemAtPosition(position)) {
                    "??????1" -> {

                    }
                    "??????2" -> {

                    }
                    "??????3" -> {

                    }
                    "??????4" -> {

                    }
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                //TODO("Not yet implemented")
            }

        }
    }
    */


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return true
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

    override fun onBackPressed() {
        super.onBackPressed()
        Intent(this@RouteActivity,MainActivity::class.java).run {
            startActivity(this)
            finishAffinity()
        }
    }

}