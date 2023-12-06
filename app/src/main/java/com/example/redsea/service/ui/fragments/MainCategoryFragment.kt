package com.example.redsea.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.redsea.R
import com.example.redsea.databinding.FragmentMainCategoryBinding
import com.example.redsea.service.ui.BottomNavigationInterface
import com.example.redsea.service.ui.TitleInterface
import com.example.redsea.service.ui.activity.mysharedpref
import com.example.redsea.ui.activity.MainActivity

class MainCategoryFragment : Fragment(){
    lateinit var binding: FragmentMainCategoryBinding
    private var bottomNavigationListener : BottomNavigationInterface? = null
    private lateinit var mysharedpref: mysharedpref
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
        mysharedpref= mysharedpref(requireContext())
        val mainActivity = MainActivity()
        (activity as? TitleInterface)?.onTextChange("Home", getString(R.string.home_toolbar))
        binding.operationsBtn.setOnClickListener {
            bottomNavigationListener?.onBottomNavigationListener("operations")
            mysharedpref.saveDataType("operations")
            navigateToOperationsFragment()
        }
        binding.wellSurveysBtn.setOnClickListener {
            mysharedpref.saveDataType("wellSurveys")
            navigateToOperationsFragment()
        }
        binding.testBtn.setOnClickListener {
            mysharedpref.saveDataType("test")
            navigateToOperationsFragment()
        }
        binding.troubleShootingBtn.setOnClickListener {
            mysharedpref.saveDataType("trouble")
            navigateToOperationsFragment()
        }


    }

    private fun navigateToOperationsFragment() {
        val transaction: FragmentTransaction? = fragmentManager?.beginTransaction()
        val operationsFragment = OperationsFragment()
        transaction?.replace(R.id.fragmentContainer, operationsFragment)
        transaction?.commit()
    }

}



