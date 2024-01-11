package com.redsea.redsea.service.ui.activity
import android.content.Context
import android.content.SharedPreferences
import com.redsea.redsea.network.Response.WellOptions.StructureDescription

class mysharedpref(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun saveDataType(dataType: String) {
        editor.putString("dataType", dataType).apply()
    }

    fun getDataType(): String? {
        return sharedPreferences.getString("dataType", null)
    }


}