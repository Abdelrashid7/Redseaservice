package com.example.redsea.service.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import com.example.redsea.R
import com.example.redsea.network.api.api
import com.example.redsea.ui.fragments.OperationsFragment

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val btn = findViewById<AppCompatButton>(R.id.backSearchBtn)
        btn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }
}