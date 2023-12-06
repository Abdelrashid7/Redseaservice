package com.example.redsea.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.redsea.R
import com.example.redsea.service.ui.TitleInterface
import com.example.redsea.databinding.FragmentOperationsBinding
import com.example.redsea.network.Response.UserWells.UserWells
import com.example.redsea.network.retrofit.RetrofitClient
import com.example.redsea.service.ui.UserID
import com.example.redsea.service.ui.activity.mysharedpref
import com.example.redsea.service.ui.adapters.adapters.DraftsAdapter
import com.example.redsea.service.ui.fragments.AddWellFragment
import com.example.redsea.service.ui.fragments.OPenRequestFragment
import com.example.redsea.service.ui.fragments.ViewWellFragment
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
        val dattype=mysharedpref(requireContext()).getDataType()
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
        binding.draftProgress.visibility = View.VISIBLE
        RetrofitClient.instance.userWells("Bearer ${UserID.userAccessToken}")
            .enqueue(object : Callback<UserWells> {
                override fun onResponse(
                    call: Call<UserWells>,
                    response: Response<UserWells>
                ) {
                    if (response.isSuccessful) {

                        val userWellsResponse = response.body()
                        Log.d("SAVEDDRAFT", userWellsResponse.toString())
                        if (userWellsResponse != null) {
                            val latestTwoResponses = userWellsResponse.subList(
                                userWellsResponse.size - 2,
                                userWellsResponse.size
                            )
                            draftsAdapter = DraftsAdapter(latestTwoResponses)
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
        Toast.makeText(context, "well survey data", Toast.LENGTH_SHORT).show()
    }
    private fun userWellstest(){
        Toast.makeText(context, "test data", Toast.LENGTH_SHORT).show()
    }
    private fun userWelltrouble(){
        Toast.makeText(context, "trouble data", Toast.LENGTH_SHORT).show()
    }



}
