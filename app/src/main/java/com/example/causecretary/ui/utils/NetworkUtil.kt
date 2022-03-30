package com.example.causecretary.ui.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.example.causecretary.ui.MainActivity

class NetworkUtil {
    companion object {
        private var mNetworkType = NetworkType.NONE
        private var mIsUseWifi = false
        private var mIsUseData = false
        private lateinit var wifiNetworkCallback: ConnectivityManager.NetworkCallback
        private lateinit var dataNetworkCallback: ConnectivityManager.NetworkCallback

        fun checkConnectionType(activity: Activity) {
            unregisterNetworkCallback(activity)

            val connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val wifiNetworkRequest = NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build()
            wifiNetworkCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    Logger.e("sigu", "wifi onAvailable = $network")
                    mIsUseWifi = true
                    mNetworkType = NetworkType.WIFI
                    if (activity is MainActivity) {
//                        activity.changeNetworkWifi()
                    }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    Logger.e("sigu", "wifi onLost = $network")
                    mIsUseWifi = false
                    if (!mIsUseWifi && !mIsUseData) {
                        mNetworkType = NetworkType.NONE
                        if (activity is MainActivity) {
//                            activity.changeNetworkNone()
                        }
                    }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    Logger.e("sigu", "wifi onUnavailable")
                    mIsUseWifi = false
                    if (!mIsUseWifi && !mIsUseData) {
                        mNetworkType = NetworkType.NONE
                        if (activity is MainActivity) {
//                            activity.changeNetworkNone()
                        }
                    }
                }
            }
            val dataNetworkRequest = NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build()
            dataNetworkCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    Logger.e("sigu", "data onAvailable = $network")
                    mIsUseData = true
                    mNetworkType = NetworkType.DATA
                    if (activity is MainActivity) {
//                        activity.changeNetworkData()
                    }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    Logger.e("sigu", "data onLost = $network")
                    mIsUseData = false
                    if (!mIsUseWifi && !mIsUseData) {
                        mNetworkType = NetworkType.NONE
                        if (activity is MainActivity) {
//                            activity.changeNetworkNone()
                        }
                    }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    Logger.e("sigu", "data onUnavailable")
                    mIsUseData = false
                    if (!mIsUseWifi && !mIsUseData) {
                        mNetworkType = NetworkType.NONE
                        if (activity is MainActivity) {
//                            activity.changeNetworkNone()
                        }
                    }
                }
            }

            connectivityManager.registerNetworkCallback(wifiNetworkRequest, wifiNetworkCallback)
            connectivityManager.registerNetworkCallback(dataNetworkRequest, dataNetworkCallback)
        }

        fun getNetworkType(): NetworkType {
            return mNetworkType
        }

        fun isUseWifi(): Boolean {
            return mIsUseWifi
        }

        fun unregisterNetworkCallback(activity: Activity) {
            val connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            try {
                connectivityManager.unregisterNetworkCallback(wifiNetworkCallback)
            } catch (e: Exception) {
//                Logger.e("sigu", "connectivityManager wifiCallback_app = ${e.message}")
            }
            try {
                connectivityManager.unregisterNetworkCallback(dataNetworkCallback)
            } catch (e: Exception) {
//                Logger.e("sigu", "connectivityManager dataCallback_app = ${e.message}")
            }
        }
    }

    enum class NetworkType(val type : String) {
        WIFI("wifi"), DATA("data"), NONE("none")
    }
}