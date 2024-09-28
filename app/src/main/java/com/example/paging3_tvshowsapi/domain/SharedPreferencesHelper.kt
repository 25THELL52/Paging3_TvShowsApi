package com.example.paging3_tvshowsapi.domain

import android.content.Context

class SharedPreferencesHelper(private val context: Context) {

    companion object{
        private const val MY_PREF_KEY = "MY_PREF"
    }

    fun saveIntData(key: String,data: Int) {
        val sharedPreferences = context.getSharedPreferences(MY_PREF_KEY, Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt(key,data).apply()
    }

    fun getIntData(key: String): Int {
        val sharedPreferences = context.getSharedPreferences(MY_PREF_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(key,-1)
    }

    fun clearPreferences() {
        val sharedPreferences = context.getSharedPreferences(MY_PREF_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.edit().clear().apply()

    }}