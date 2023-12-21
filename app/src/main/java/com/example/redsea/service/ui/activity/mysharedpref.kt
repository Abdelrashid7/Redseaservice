package com.example.redsea.service.ui.activity

import android.content.Context
import android.content.SharedPreferences

class mysharedpref(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)

    fun saveDataType(dataType: String) {
        sharedPreferences.edit().putString("dataType", dataType).apply()
    }

    fun getDataType(): String? {
        return sharedPreferences.getString("dataType", null)
    }
}