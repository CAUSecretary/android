package com.example.causecretary.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.example.causecretary.R
import com.example.causecretary.databinding.ActivityRouteBinding
import com.example.causecretary.ui.utils.Logger
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback

class RouteActivity : AppCompatActivity(), View.OnClickListener, OnMapReadyCallback {
    lateinit var binding: ActivityRouteBinding
    private lateinit var naverMap: NaverMap
    private lateinit var mapView: MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_route)
        mapView = findViewById(R.id.map)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)


        initData()
        initView()
    }

    override fun onMapReady(naverMap: NaverMap) {
        // 카메라 초기 위치 설정
        // 카메라 초기 위치 설정
        Logger.d("Navi", "onMapReady start")
        this.naverMap = naverMap
        val initialPosition = LatLng(37.50335, 126.9574114)
        val cameraUpdate = CameraUpdate.scrollTo(initialPosition)
        naverMap.moveCamera(cameraUpdate)
    }

    private fun initView() {
        binding.clickListener = this@RouteActivity
        //setSpinner()

        /**
         * 검색해서 이 화면으로 들어왔을때 검색했던 건물에 마커띄어주고, 도착지에 건물 주소 띄어주는거 가넝?
         * 그럼 출발지만 검색받고 길찾기 누르면 길찾기 되는걸로다가.
         */
    }

    private fun initData() {
        //TODO("Not yet implemented")
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ib_switch -> {
                //출발지 도착지 스우치
            }
            R.id.ib_close -> {
                // 출발지 지우기
            }
            R.id.btn_route ->{
                //길찾기 로직 수행
            }
        }
    }

    /*
    private fun setSpinner() {
        hideKeyboard()
        /**
         * res - values - strings에서 string array strucrure_name에 건물이름 넣어주세용.
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
                 * 스피너에서 건물을 클릭하면 실제 주소로 세팅해서 길찾기 로직 수행부탁드립니당.
                 * val startPoint = binding.etStart.text.toString()
                 */
                when (binding.spEnd.getItemAtPosition(position)) {
                    "건물1" -> {

                    }
                    "건물2" -> {

                    }
                    "건물3" -> {

                    }
                    "건물4" -> {

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
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken,0)
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

}