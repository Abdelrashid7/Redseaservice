package com.redsea.redsea.service.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.redsea.redsea.R
import com.redsea.redsea.databinding.FragmentEditRequestBinding
import com.redsea.redsea.network.Response.MakeRequest.MakeRequestResponse
import com.redsea.redsea.network.ViewWellsResponse.ViewWellsItem
import com.redsea.redsea.network.retrofit.RetrofitClient
import com.redsea.redsea.service.ui.TitleInterface
import com.redsea.redsea.service.ui.UserID
import com.redsea.redsea.service.ui.activity.mysharedpref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditRequestFragment: Fragment() {

    var requestDone = false

    lateinit var binding: FragmentEditRequestBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditRequestBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? TitleInterface)?.onTextChange("Edit Request", "Edit Request")
        val wellItem = arguments?.getSerializable("wellItemRequest") as? com.redsea.redsea.network.ViewWellsResponse.ViewWellsItem
        Log.d("HELLOTEXT" , wellItem?.name.toString())
        binding.wellNameViewWellTV.text = wellItem?.name
        binding.dateInTV.text = wellItem?.from
        binding.dateOutTV.text = wellItem?.to
        binding.submitRequestBtn.setOnClickListener {
            var requestDescription = binding.editRequestReasonInput.text.toString()

            if (wellItem != null && !requestDescription.isNullOrBlank()) {
                val dattype= mysharedpref(requireContext()).getDataType()
                when (dattype) {
                    "operations" -> makeRequestoperations(wellItem,requestDescription)
                    "wellSurveys" -> makeRequestsurvey(wellItem,requestDescription)
                    "test" -> makeRequesttest(wellItem,requestDescription)
                    "trouble" -> makeRequesttrouble(wellItem,requestDescription)
                }
            }
            else
            {
                Toast.makeText(requireContext(), "Request Reason is Empty", Toast.LENGTH_SHORT).show()
            }


        }
    }

    fun makeRequestoperations(wellItem : ViewWellsItem, requestDescription : String) {
        val updateWellRequest = com.redsea.redsea.network.PostData.MakeRequest(wellId = wellItem.id,
            description = requestDescription
        )
        RetrofitClient.instance.makeReqeust("Bearer ${UserID.userAccessToken}",updateWellRequest)
            .enqueue(object : Callback<MakeRequestResponse> {
                override fun onResponse(
                    call: Call<MakeRequestResponse>,
                    response: Response<MakeRequestResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.d("REQUESTBODYRESPONSE", responseBody.toString())
                        requestDone = true
                        Toast.makeText(context, "Request Posted op", Toast.LENGTH_SHORT).show()

                        navigateto(OperationsFragment())


                        } else {
                            Toast.makeText(
                                context,
                                "Null",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }

                override fun onFailure(call: Call<MakeRequestResponse>, t: Throwable) {
                    Toast.makeText(
                        context,
                        "Failed to fetch data: ${t.message}",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    Log.d("Options Data", t.message.toString())
                }


            })
    }
    fun makeRequestsurvey(wellItem : ViewWellsItem, requestDescription : String){
        val updateWellRequest = com.redsea.redsea.network.PostData.MakeRequest(
            wellId = wellItem.id,
            description = requestDescription
        )
        RetrofitClient.instance.makeReqeustsurvey("Bearer ${UserID.userAccessToken}",updateWellRequest)
            .enqueue(object : Callback<MakeRequestResponse> {
                override fun onResponse(
                    call: Call<MakeRequestResponse>,
                    response: Response<MakeRequestResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.d("REQUESTBODYRESPONSE", responseBody.toString())
                        requestDone = true
                        Toast.makeText(context, "Request Posted surv", Toast.LENGTH_SHORT).show()

                        navigateto(OperationsFragment())


                    } else {
                        Toast.makeText(
                            context,
                            "Null",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }

                override fun onFailure(call: Call<MakeRequestResponse>, t: Throwable) {
                    Toast.makeText(
                        context,
                        "Failed to fetch data: ${t.message}",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    Log.d("Options Data", t.message.toString())
                }


            })
    }
    fun makeRequesttest(wellItem : ViewWellsItem, requestDescription : String){
        val updateWellRequest = com.redsea.redsea.network.PostData.MakeRequest(
            wellId = wellItem.id,
            description = requestDescription
        )
        RetrofitClient.instance.makeReqeusttest("Bearer ${UserID.userAccessToken}",updateWellRequest)
            .enqueue(object : Callback<MakeRequestResponse> {
                override fun onResponse(
                    call: Call<MakeRequestResponse>,
                    response: Response<MakeRequestResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.d("REQUESTBODYRESPONSE", responseBody.toString())
                        requestDone = true
                        Toast.makeText(context, "Request Posted test", Toast.LENGTH_SHORT).show()

                        navigateto(OperationsFragment())


                    } else {
                        Toast.makeText(
                            context,
                            "Null",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }

                override fun onFailure(call: Call<MakeRequestResponse>, t: Throwable) {
                    Toast.makeText(
                        context,
                        "Failed to fetch data: ${t.message}",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    Log.d("Options Data", t.message.toString())
                }


            })

    }
    fun makeRequesttrouble(wellItem : ViewWellsItem, requestDescription : String){
        val updateWellRequest = com.redsea.redsea.network.PostData.MakeRequest(
            wellId = wellItem.id,
            description = requestDescription
        )
        RetrofitClient.instance.makeReqeusttrouble("Bearer ${UserID.userAccessToken}",updateWellRequest)
            .enqueue(object : Callback<MakeRequestResponse> {
                override fun onResponse(
                    call: Call<MakeRequestResponse>,
                    response: Response<MakeRequestResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.d("REQUESTBODYRESPONSE", responseBody.toString())
                        requestDone = true
                        Toast.makeText(context, "Request Posted trouble", Toast.LENGTH_SHORT).show()

                        navigateto(OperationsFragment())


                    } else {
                        Toast.makeText(context, "Null", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<MakeRequestResponse>, t: Throwable) {
                    Toast.makeText(
                        context,
                        "Failed to fetch data: ${t.message}",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    Log.d("Options Data", t.message.toString())
                }


            })

    }
    fun navigateto(fragment: Fragment){
        val transaction=fragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragmentContainer,fragment)
        transaction?.addToBackStack(null)
        transaction?.commit()

    }
}