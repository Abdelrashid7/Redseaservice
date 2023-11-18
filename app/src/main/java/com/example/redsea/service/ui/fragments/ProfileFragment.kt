package com.example.redsea.service.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.redsea.R
import com.example.redsea.databinding.FragmentOperationsBinding
import com.example.redsea.databinding.FragmentProfileBinding
import com.example.redsea.service.ui.TitleInterface
import com.example.redsea.service.ui.activity.SearchActivity
import com.example.redsea.ui.activity.LoginActivity

class ProfileFragment : Fragment(){

    lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? TitleInterface)?.onTextChange("Profile", getString(R.string.profile_toolbar))

        binding.logOutBtn.setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }



    }


}