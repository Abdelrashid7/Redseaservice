package com.redsea.redsea.service.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.redsea.redsea.R
import com.redsea.redsea.databinding.ActivityMainBinding
import com.redsea.redsea.service.ui.BottomNavigationInterface
import com.redsea.redsea.service.ui.TitleInterface
import com.redsea.redsea.service.ui.fragments.MainCategoryFragment
import com.redsea.redsea.service.ui.fragments.OperationsFragment
import com.redsea.redsea.service.ui.fragments.ProfileFragment
import com.redsea.redsea.service.ui.fragments.ViewWellFragment

class MainActivity : AppCompatActivity() , TitleInterface, BottomNavigationInterface {

    private var prevText : String = "Home"
    private var middleText: String = ""
    private lateinit var binding: ActivityMainBinding
    val mainCategoryFragment = MainCategoryFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //handle on back pressed
        onBackPressedDispatcher.addCallback(this){
            initFragment(mainCategoryFragment)
        }

        binding =ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


            binding.bottomNavigation.add(MeowBottomNavigation.Model(0,R.mipmap.ic_home))
            binding.bottomNavigation.add(MeowBottomNavigation.Model(1,R.mipmap.ic_clock))
            binding.bottomNavigation.add(MeowBottomNavigation.Model(2,R.mipmap.ic_graph))
            binding.bottomNavigation.add(MeowBottomNavigation.Model(3,R.mipmap.ic_profile))
            binding.bottomNavigation.show(0)

        binding.bottomNavigation.setOnClickMenuListener {item->
            when(item.id)
            {
                0 -> initFragment(MainCategoryFragment())
                1-> initFragment(OperationsFragment())
                2-> initFragment(ViewWellFragment())
                3-> initFragment(ProfileFragment())
            }
        }

        initFragment(MainCategoryFragment())

    }



    override fun onTextChange(identifier: String, newText: String) {
        when(identifier)
        {
            "Operations" -> {
                binding.pageTV.text = newText
                middleText = prevText
            }
            "Add new Report"->{
                binding.pageTV.text = newText
                middleText = prevText
            }
            "View"->{
                binding.pageTV.text = newText
                middleText = prevText
            }
            "Home"->
            {
                binding.pageTV.text = newText
                middleText = prevText
            }
            "Profile"->
            {
                binding.pageTV.text = newText
                middleText = prevText
            }
            "Open Request"->
            {
                binding.pageTV.text=newText
                middleText = prevText
            }
            "Well Pdf"->
            {
                binding.pageTV.text=newText
                middleText=prevText
            }
            "Well Details"->
            {
                binding.pageTV.text=newText
                middleText=prevText
            }
            "Edit Request"->
            {
                binding.pageTV.text=newText
                middleText=prevText
            }
            "Well Survey"->{
                binding.pageTV.text=newText
                middleText=prevText
            }
            "Test"->{
                binding.pageTV.text=newText
                middleText=prevText
            }
            "Trouble Shooting"->{
                binding.pageTV.text=newText
                middleText=prevText
            }

        }
    }




    private fun initFragment(fragment : Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.fragmentContainer.id,fragment)
//        transaction.addToBackStack(null)
        transaction.commit()

    }

    override fun onBottomNavigationListener(identifier: String) {
        when(identifier)
        {
            "Operations"->binding.bottomNavigation.show(1)
            "View"->binding.bottomNavigation.show(2)
        }
    }

}