package com.example.redsea.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.redsea.R
import com.example.redsea.databinding.FragmentMainCategoryBinding
import com.example.redsea.service.ui.BottomNavigationInterface
import com.example.redsea.service.ui.TitleInterface
import com.example.redsea.ui.activity.MainActivity

class MainCategoryFragment : Fragment(){
    lateinit var binding: FragmentMainCategoryBinding
    private var bottomNavigationListener : BottomNavigationInterface? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainCategoryBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainActivity = MainActivity()
        (activity as? TitleInterface)?.onTextChange("Home", getString(R.string.home_toolbar))


        binding.operationsBtn.setOnClickListener {
            bottomNavigationListener?.onBottomNavigationListener("operations")
            val transaction : FragmentTransaction? = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragmentContainer, OperationsFragment())
            transaction?.addToBackStack(null)
            transaction?.commit()
        }
    }


}