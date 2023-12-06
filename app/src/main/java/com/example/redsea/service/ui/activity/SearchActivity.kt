package com.example.redsea.service.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.redsea.R
import com.example.redsea.service.ui.fragments.ViewWellFragment


class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val btn = findViewById<AppCompatButton>(R.id.backSearchBtn)
        val searchBtn=findViewById<AppCompatButton>(R.id.searchBtnAc)
        val searchview=findViewById<SearchView>(R.id.searchInputText)
        btn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


    }



}