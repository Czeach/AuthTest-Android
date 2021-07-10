package com.appstyx.authtest.utils

import android.content.Context
import android.content.SharedPreferences
import com.appstyx.authtest.R

class SessionManager(context: Context) {

    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
    private val editor = prefs.edit()

    companion object {
        const val USER_TOKEN = "user_token"
        const val IS_LOGIN = "IsLoggedIn"
    }

    fun saveAuthToken(token: String) {
        editor.putString(USER_TOKEN, token)
        editor.putBoolean(IS_LOGIN, true)
        editor.apply()
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun clearAuthToken() {
        editor
            .remove(USER_TOKEN)
            .remove(IS_LOGIN)
            .clear()
            .apply()
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(IS_LOGIN, false)
    }
}