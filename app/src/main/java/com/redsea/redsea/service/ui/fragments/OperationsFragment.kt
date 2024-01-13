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
                (activity as? TitleInterface)
                    ?.onTextChange("Operations","Operations")
                userWellsoperations()}
            "wellSurveys" -> {
                (activity as? TitleInterface)?.onTextChange("Well Survey","Well Survey")
                userWellssurvey()}

            "test" -> {
                (activity as? TitleInterface)?.onTextChange("Test","Test")
                userWellstest()
            }
            "trouble" -> {
                (activity as? TitleInterface)?.onTextChange("Trouble Shooting","Trouble Shooting")
                userWelltrouble()
            }


        }


        binding.searchBtn.setOnClickListener {
            val transaction: FragmentTransaction? = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragmentContainer, ViewWellFragment())
//            transaction?.addToBackStack(null)
            transaction?.commit()
        }

        binding.addWellBtn.setOnClickListener {
            val transaction: FragmentTransaction? = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragmentContainer, AddWellFragment())
//                transaction?.addToBackStack(null)
            transaction?.commit()


        }
        binding.viewWellBtn.setOnClickListener {
            val transaction: FragmentTransaction? = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragmentContainer, ViewWellFragment())
//            transaction?.addToBackStack(null)
            transaction?.commit()
        }
        binding.openRequestBtn.setOnClickListener {
            val transaction: FragmentTransaction? = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragmentContainer, OPenRequestFragment())
//            transaction?.addToBackStack(null)
            transaction?.commit()
        }


    }




    private fun userWellsoperations() {
        val transiaction:FragmentTransaction?=fragmentManager?.beginTransaction()
        binding.draftProgress.visibility = View.VISIBLE
        com.redsea.redsea.network.retrofit.RetrofitClient.instance.userWells("Bearer ${UserID.userAccessToken}")
            .enqueue(object : Callback<com.redsea.redsea.network.Response.UserWells.UserWells> {
                override fun onResponse(
                    call: Call<com.redsea.redsea.network.Response.UserWells.UserWells>,
                    response: Response<com.redsea.redsea.network.Response.UserWells.UserWells>
                ) {
                    if (response.isSuccessful) {

                        val userWellsResponse = response.body()
                        Log.d("SAVEDDRAFT", userWellsResponse.toString())
                        if (userWellsResponse != null) {
                            draftsAdapter = DraftsAdapter(transiaction,userWellsResponse)
                            binding.draftsRecyclerView.adapter = draftsAdapter
                        }
                    }
                    binding.draftProgress.visibility = View.GONE
                }


                override fun onFailure(call: Call<com.redsea.redsea.network.Response.UserWells.UserWells>, t: Throwable) {
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
            .enqueue(object : Callback<com.redsea.redsea.network.Response.UserWells.UserWells> {
                override fun onResponse(
                    call: Call<com.redsea.redsea.network.Response.UserWells.UserWells>,
                    response: Response<com.redsea.redsea.network.Response.UserWells.UserWells>
                ) {
                    if (response.isSuccessful) {

                        val userWellsResponse = response.body()
                        Log.d("SAVEDDRAFT", userWellsResponse.toString())
                        if (userWellsResponse != null) {
                            draftsAdapter = DraftsAdapter(transiaction,userWellsResponse)
                            binding.draftsRecyclerView.adapter = draftsAdapter
                        }
                    }
                    binding.draftProgress.visibility = View.GONE
                }


                override fun onFailure(call: Call<com.redsea.redsea.network.Response.UserWells.UserWells>, t: Throwable) {
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
            .enqueue(object : Callback<com.redsea.redsea.network.Response.UserWells.UserWells> {
                override fun onResponse(
                    call: Call<com.redsea.redsea.network.Response.UserWells.UserWells>,
                    response: Response<com.redsea.redsea.network.Response.UserWells.UserWells>
                ) {
                    if (response.isSuccessful) {

                        val userWellsResponse = response.body()
                        Log.d("SAVEDDRAFT", userWellsResponse.toString())
                        if (userWellsResponse != null) {
                            draftsAdapter = DraftsAdapter(transiaction,userWellsResponse)
                            binding.draftsRecyclerView.adapter = draftsAdapter
                        }
                    }
                    binding.draftProgress.visibility = View.GONE
                }


                override fun onFailure(call: Call<com.redsea.redsea.network.Response.UserWells.UserWells>, t: Throwable) {
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
            .enqueue(object : Callback<com.redsea.redsea.network.Response.UserWells.UserWells> {
                override fun onResponse(
                    call: Call<com.redsea.redsea.network.Response.UserWells.UserWells>,
                    response: Response<com.redsea.redsea.network.Response.UserWells.UserWells>
                ) {
                    if (response.isSuccessful) {

                        val userWellsResponse = response.body()
                        Log.d("SAVEDDRAFT", userWellsResponse.toString())
                        if (userWellsResponse != null) {
                            draftsAdapter = DraftsAdapter(transiaction,userWellsResponse)
                            binding.draftsRecyclerView.adapter = draftsAdapter
                        }
                    }
                    binding.draftProgress.visibility = View.GONE
                }


                override fun onFailure(call: Call<com.redsea.redsea.network.Response.UserWells.UserWells>, t: Throwable) {
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



}
