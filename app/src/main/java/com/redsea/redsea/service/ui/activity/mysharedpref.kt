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
    fun savelogin(email:String,password:String,remember:Boolean){
        editor.putString("email",email)
        editor.putString("password",password)
        editor.putBoolean("remember me",remember)
        editor.apply()

    }


    fun removelogin(email: String,password: String){
        editor.remove("email")
        editor.remove("password")
        editor.remove("remember me")
        editor.apply()

    }



}