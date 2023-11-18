package com.example.redsea.service.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.redsea.R
import com.example.redsea.databinding.FragmentSoonBinding
import com.example.redsea.service.ui.TitleInterface

class SoonFragment : Fragment() {
    lateinit var binding: FragmentSoonBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSoonBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? TitleInterface)?.onTextChange("Soon", "Soon")




    }
}