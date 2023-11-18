package com.example.redsea.service.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.redsea.R
import com.example.redsea.databinding.FragmentEditRequestBinding
import com.example.redsea.network.PostData.MakeRequest
import com.example.redsea.network.Response.MakeRequest.MakeRequestResponse
import com.example.redsea.network.ViewWellsResponse.ViewWellsItem
import com.example.redsea.network.retrofit.RetrofitClient
import com.example.redsea.service.ui.TitleInterface
import com.example.redsea.service.ui.UserID
import com.example.redsea.ui.fragments.OperationsFragment
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
        (activity as? TitleInterface)?.onTextChange("View", getString(R.string.view_toolbar))
        val wellItem = arguments?.getSerializable("wellItemRequest") as? ViewWellsItem
        Log.d("HELLOTEXT" , wellItem?.name.toString())
        binding.wellNameViewWellTV.text = wellItem?.name
        binding.dateInTV.text = wellItem?.from
        binding.dateOutTV.text = wellItem?.to
        binding.submitRequestBtn.setOnClickListener {
            var requestDescription = binding.editRequestReasonInput.text.toString()

            if (wellItem != null && !requestDescription.isNullOrBlank()) {
                makeRequest(wellItem, requestDescription)
            }
            else
            {
                Toast.makeText(requireContext(), "Request Reason is Empty", Toast.LENGTH_SHORT).show()
            }


        }
    }

    fun makeRequest(wellItem : ViewWellsItem, requestDescription : String)
    {
        val updateWellRequest = MakeRequest(wellId = wellItem.id, description = requestDescription)
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
                        Toast.makeText(context, "Request Posted", Toast.LENGTH_SHORT).show()
                        val transaction : FragmentTransaction? = fragmentManager?.beginTransaction()
                        transaction?.replace(R.id.fragmentContainer, OperationsFragment())
                        transaction?.addToBackStack(null)
                        transaction?.commit()

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
}