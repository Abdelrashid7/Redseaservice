package com.redsea.redsea.service.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.redsea.redsea.R
import com.redsea.redsea.databinding.FragmentProfileBinding
import com.redsea.redsea.service.ui.BottomNavigationInterface
import com.redsea.redsea.service.ui.activity.LoginActivity
import com.redsea.redsea.service.ui.TitleInterface

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
        (activity as? BottomNavigationInterface)?.onBottomNavigationListener("Profile")

        binding.logOutBtn.setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()

        }




    }




}