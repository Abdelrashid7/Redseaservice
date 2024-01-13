package com.redsea.redsea.service.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.redsea.redsea.R
import com.redsea.redsea.databinding.FragmentMainCategoryBinding
import com.redsea.redsea.service.ui.activity.MainActivity
import com.redsea.redsea.service.ui.BottomNavigationInterface
import com.redsea.redsea.service.ui.TitleInterface
import com.redsea.redsea.service.ui.activity.mysharedpref

class MainCategoryFragment : Fragment(){
    lateinit var binding: FragmentMainCategoryBinding
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
        (activity as? TitleInterface)?.onTextChange("Home", getString(R.string.home_toolbar))
        (activity as? BottomNavigationInterface)?.onBottomNavigationListener("Home")

        binding.operationsBtn.setOnClickListener {

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
        transaction?.addToBackStack(null)
        transaction?.commit()
    }

}



