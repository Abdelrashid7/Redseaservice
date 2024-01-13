package com.redsea.redsea.service.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.redsea.redsea.R
import com.redsea.redsea.databinding.FragmentOperationsBinding
import com.redsea.redsea.network.Response.UserWells.UserWells
import com.redsea.redsea.service.ui.BottomNavigationInterface
import com.redsea.redsea.service.ui.TitleInterface
import com.redsea.redsea.service.ui.UserID
import com.redsea.redsea.service.ui.activity.mysharedpref
import com.redsea.redsea.service.ui.adapters.adapters.DraftsAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OperationsFragment : Fragment() {


    lateinit var binding: FragmentOperationsBinding
    lateinit var draftsAdapter: DraftsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOperationsBinding.inflate(layoutInflater)
        var linearLayoutManager = LinearLayoutManager(context)
        binding.draftsRecyclerView.layoutManager = linearLayoutManager
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dattype= mysharedpref(requireContext()).getDataType()
        when (dattype) {
            "operations" -> {
                (activity as? TitleInterface)?.onTextChange("Operations","Operations")
                (activity as? BottomNavigationInterface)?.onBottomNavigationListener("Operations")
                userWellsoperations()

            }
            "wellSurveys" -> {
                (activity as? TitleInterface)?.onTextChange("Well Survey","Well Survey")
                (activity as? BottomNavigationInterface)?.onBottomNavigationListener("Operations")

                userWellssurvey()
            }

            "test" -> {
                (activity as? TitleInterface)?.onTextChange("Test","Equipments")
                (activity as? BottomNavigationInterface)?.onBottomNavigationListener("Operations")
                userWellstest()
            }
            "trouble" -> {
                (activity as? TitleInterface)?.onTextChange("Trouble Shooting","Trouble Shooting")
                (activity as? BottomNavigationInterface)?.onBottomNavigationListener("Operations")
                userWelltrouble()
            }


        }


        binding.searchBtn.setOnClickListener {
            initFragment(ViewWellFragment())
        }

        binding.addWellBtn.setOnClickListener {
            initFragment(AddWellFragment())
        }
        binding.viewWellBtn.setOnClickListener {
            initFragment(ViewWellFragment())

        }
        binding.openRequestBtn.setOnClickListener {
            initFragment(OPenRequestFragment())

        }


    }




    private fun userWellsoperations() {
        val transiaction:FragmentTransaction?=fragmentManager?.beginTransaction()
        binding.draftProgress.visibility = View.VISIBLE
        com.redsea.redsea.network.retrofit.RetrofitClient.instance.userWells("Bearer ${UserID.userAccessToken}")
            .enqueue(object : Callback<UserWells> {
                override fun onResponse(
                    call: Call<UserWells>,
                    response: Response<UserWells>
                ) {
                    if (response.isSuccessful) {
                        val userWellsResponse = response.body()
                        if (userWellsResponse?.size!!>=2){
                            val latesttworesponse= userWellsResponse?.subList(userWellsResponse.size.minus(2),
                                userWellsResponse.size)
                            draftsAdapter = DraftsAdapter(transiaction,latesttworesponse!!)
                            binding.draftsRecyclerView.adapter = draftsAdapter

                        }
                        else{
                            draftsAdapter = DraftsAdapter(transiaction,userWellsResponse)
                            binding.draftsRecyclerView.adapter = draftsAdapter

                        }
                    }
                    binding.draftProgress.visibility = View.GONE
                }


                override fun onFailure(call: Call<UserWells>, t: Throwable) {
                    Toast.makeText(
                        context,
                        "Failed to fetch data: ${t.message}",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    Log.d("SAVED DRAFTS FAILED DATA", t.message.toString())
                    binding.draftProgress.visibility = View.GONE
                }

            })

    }
    private fun userWellssurvey(){
        val transiaction:FragmentTransaction?=fragmentManager?.beginTransaction()
        binding.draftProgress.visibility = View.VISIBLE
        com.redsea.redsea.network.retrofit.RetrofitClient.instance.userWellssurvey("Bearer ${UserID.userAccessToken}")
            .enqueue(object : Callback<UserWells> {
                override fun onResponse(
                    call: Call<UserWells>,
                    response: Response<UserWells>
                ) {
                    if (response.isSuccessful) {
                        val userWellsResponse = response.body()
                        if (userWellsResponse?.size!!>=2){
                            val latesttworesponse= userWellsResponse?.subList(userWellsResponse.size.minus(2),
                                userWellsResponse.size)
                            draftsAdapter = DraftsAdapter(transiaction,latesttworesponse!!)
                            binding.draftsRecyclerView.adapter = draftsAdapter

                        }
                        else{
                            draftsAdapter = DraftsAdapter(transiaction,userWellsResponse)
                            binding.draftsRecyclerView.adapter = draftsAdapter

                        }
                    }
                    binding.draftProgress.visibility = View.GONE
                }


                override fun onFailure(call: Call<UserWells>, t: Throwable) {
                    Toast.makeText(
                        context,
                        "Failed to fetch data: ${t.message}",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    Log.d("SAVED DRAFTS FAILED DATA", t.message.toString())
                    binding.draftProgress.visibility = View.GONE
                }

            })

    }
    private fun userWellstest(){
        val transiaction:FragmentTransaction?=fragmentManager?.beginTransaction()
        binding.draftProgress.visibility = View.VISIBLE
        com.redsea.redsea.network.retrofit.RetrofitClient.instance.userWellstest("Bearer ${UserID.userAccessToken}")
            .enqueue(object : Callback<UserWells> {
                override fun onResponse(
                    call: Call<UserWells>,
                    response: Response<UserWells>
                ) {
                    if (response.isSuccessful) {
                        val userWellsResponse = response.body()
                        if (userWellsResponse?.size!!>=2){
                            val latesttworesponse= userWellsResponse?.subList(userWellsResponse.size.minus(2),
                                userWellsResponse.size)
                            draftsAdapter = DraftsAdapter(transiaction,latesttworesponse!!)
                            binding.draftsRecyclerView.adapter = draftsAdapter

                        }
                        else{
                            draftsAdapter = DraftsAdapter(transiaction,userWellsResponse)
                            binding.draftsRecyclerView.adapter = draftsAdapter

                        }
                    }
                    binding.draftProgress.visibility = View.GONE
                }


                override fun onFailure(call: Call<UserWells>, t: Throwable) {
                    Toast.makeText(
                        context,
                        "Failed to fetch data: ${t.message}",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    Log.d("SAVED DRAFTS FAILED DATA", t.message.toString())
                    binding.draftProgress.visibility = View.GONE
                }

            })
    }
    private fun userWelltrouble(){
        val transiaction:FragmentTransaction?=fragmentManager?.beginTransaction()
        binding.draftProgress.visibility = View.VISIBLE
        com.redsea.redsea.network.retrofit.RetrofitClient.instance.userWellstrouble("Bearer ${UserID.userAccessToken}")
            .enqueue(object : Callback<UserWells> {
                override fun onResponse(
                    call: Call<UserWells>,
                    response: Response<UserWells>
                ) {
                    if (response.isSuccessful) {
                        val userWellsResponse = response.body()
                        if (userWellsResponse?.size!!>=2){
                            val latesttworesponse= userWellsResponse?.subList(userWellsResponse.size.minus(2),
                                userWellsResponse.size  )
                            draftsAdapter = DraftsAdapter(transiaction,latesttworesponse!!)
                            binding.draftsRecyclerView.adapter = draftsAdapter

                        }
                        else{
                            draftsAdapter = DraftsAdapter(transiaction,userWellsResponse)
                            binding.draftsRecyclerView.adapter = draftsAdapter

                        }
                    }
                    binding.draftProgress.visibility = View.GONE
                }


                override fun onFailure(call: Call<UserWells>, t: Throwable) {
                    Toast.makeText(
                        context,
                        "Failed to fetch data: ${t.message}",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    Log.d("SAVED DRAFTS FAILED DATA", t.message.toString())
                    binding.draftProgress.visibility = View.GONE
                }

            })
    }
    private fun initFragment(fragment : Fragment) {
        val transaction = fragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragmentContainer,fragment)
        transaction?.addToBackStack(null)
        transaction?.commit()
    }



}

