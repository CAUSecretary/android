package com.example.causecretary.ui.utils
import android.content.Context
import android.content.SharedPreferences
import com.example.causecretary.ui.data.LoginData
import com.google.gson.Gson

class PrefManager (private val context: Context) {

    companion object {
        private const val PREF_FILENAME = ""
        private const val PREF_LOGIN_DATA: String = "login_data"

        private const val PREF_PERMISSION_FILENAME = "com.example.causecretary.premission"
        private const val PREF_PERMISSION_CONFIRM = "permission_confirm"
    }

    fun clearAll() {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_FILENAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

    /**
     * 로그인 토큰 저장
     *
     * @param loginId
     * @param token
     */
    fun setLoginData(userIdx: Int, jwt: String) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_FILENAME, Context.MODE_PRIVATE)

        Gson().apply {
            val loginData: LoginData = LoginData(userIdx,jwt)
            val jsonToString: String = toJson(loginData)
            prefs.edit().putString(PREF_LOGIN_DATA, jsonToString).apply()
        }
    }
    /**
     * 로그인 토큰 반환
     *
     * @return LoginData?
     */
    fun getLoginData(): LoginData {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_FILENAME, Context.MODE_PRIVATE)
        Gson().apply {
            val jsonToString: String = prefs.getString(PREF_LOGIN_DATA, "")?:""
            return fromJson(jsonToString, LoginData::class.java)
        }
    }

    /**
     * 로그인 토큰 삭제
     */
    fun removeLoginData() {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_FILENAME, Context.MODE_PRIVATE)
        prefs.edit().remove(PREF_LOGIN_DATA).apply()
    }

    fun setPermissionConfirm(isConfirm: Boolean) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_PERMISSION_FILENAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(PREF_PERMISSION_CONFIRM, isConfirm).apply()
    }

    fun isPermissionConfirm(): Boolean {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_PERMISSION_FILENAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(PREF_PERMISSION_CONFIRM, false)
    }
}