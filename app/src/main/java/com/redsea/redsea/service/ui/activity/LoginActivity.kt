package com.redsea.redsea.service.ui.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import com.redsea.redsea.R
import com.redsea.redsea.databinding.ActivityLoginBinding
import com.redsea.redsea.service.ui.UserID
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreference:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        var checked = false
        val intent = Intent(this, MainActivity::class.java)
        sharedPreference = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
        if (sharedPreference.getBoolean("remember me", false)) {
            binding.usernameInput.setText(sharedPreference.getString("email", ""))
            binding.passwordInput.setText(sharedPreference.getString("password", ""))
            binding.remembermeCheckBox.isChecked = true
        }

        binding.hidePwIcon.setOnClickListener {
            if (checked == false) {
                binding.hidePwIcon.setBackgroundResource(R.mipmap.ic_view_pw)
                binding.passwordInput.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                checked = true
            } else {
                binding.hidePwIcon.setBackgroundResource(R.mipmap.ic_hide_pw)
                binding.passwordInput.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                checked = false
            }
        }

        binding.logInBtn.setOnClickListener {
            val email = binding.usernameInput.text.toString()
            val password = binding.passwordInput.text.toString()
            Log.d("PASS", password)
                if (!password.isNullOrBlank() && !email.isNullOrBlank()) {
                    binding.logInProgress.visibility = View.VISIBLE
                    com.redsea.redsea.network.retrofit.RetrofitClient.instance.userLogin(email, password)
                        .enqueue(object :
                            Callback<com.redsea.redsea.network.Response.Login.LoginResponse> {
                            override fun onResponse(
                                call: Call<com.redsea.redsea.network.Response.Login.LoginResponse>,
                                logInResponse: Response<com.redsea.redsea.network.Response.Login.LoginResponse>
                            ) {
                                if(logInResponse.isSuccessful) {
                                    val loginResponse = logInResponse.body()

                                    if (loginResponse?.code == 100) {
                                        UserID.userId = loginResponse.user.id
                                        UserID.userAccessToken = loginResponse.token
                                        Log.d("CHECKUSER", UserID.userId.toString())
                                        Log.d("CHECKUSER", UserID.userAccessToken.toString())
                                        startActivity(intent)

                                        if (binding.remembermeCheckBox.isChecked){
                                            val shareprefeditor:Editor=sharedPreference.edit()
                                            shareprefeditor.putString("email",email)
                                            shareprefeditor.putString("password",password)
                                            shareprefeditor.putBoolean("remember me",true)
                                            shareprefeditor.apply()
                                        }
                                        else{
                                            val shareprefeditor:Editor=sharedPreference.edit()
                                            shareprefeditor.remove("email")
                                            shareprefeditor.remove("password")
                                            shareprefeditor.remove("remember me")
                                            shareprefeditor.apply()

                                        }

                                    } else {
                                        Toast.makeText(
                                            applicationContext,
                                            "Email or Password Incorrect",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                else
                                {
                                    Toast.makeText(applicationContext, "Email or Password are wrong", Toast.LENGTH_SHORT).show()
                                    Log.d("LoginResponse", "NO response")
                                }
                                binding.logInProgress.visibility = View.GONE
                            }

                            override fun onFailure(call: Call<com.redsea.redsea.network.Response.Login.LoginResponse>, t: Throwable) {
                                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG)
                                    .show()
                                Log.d("LoginCrash", t.message.toString())
                                binding.logInProgress.visibility = View.GONE
                            }

                        })
                } else {
                    Toast.makeText(applicationContext, "Email or Password is Empty", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }

    }



