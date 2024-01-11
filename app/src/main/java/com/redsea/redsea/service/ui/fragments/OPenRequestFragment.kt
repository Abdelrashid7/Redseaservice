package com.redsea.redsea.service.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.redsea.databinding.FragmentOpenRequestBinding
import com.redsea.redsea.service.ui.TitleInterface
import com.redsea.redsea.service.ui.UserID
import com.redsea.redsea.service.ui.activity.mysharedpref
import com.redsea.redsea.service.ui.adapters.adapters.OPenRequestAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class OPenRequestFragment : Fragment() {
    lateinit var binding: FragmentOpenRequestBinding
    lateinit var  openrequest : com.redsea.redsea.network.Response.OpenRequest.OPenRequests
    lateinit var adapter: OPenRequestAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOpenRequestBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? TitleInterface)?.onTextChange("Open Request", "Open Request")
        val dattype= mysharedpref(requireContext()).getDataType()
        when (dattype) {
            "operations" -> getaccrequestoperations()
            "wellSurveys" -> getaccrequestsurvey()
            "test" -> getaccrequesttest()
            "trouble" -> getaccrequesttrouble()
        }
        binding.searchrequestSearchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filteredrequest(newText!!)
                return true
            }

        })

    }
    private fun getaccrequestoperations(){
        val transiaction:FragmentTransaction?=fragmentManager?.beginTransaction()
        binding.openRequestProgress.visibility=View.VISIBLE
        com.redsea.redsea.network.retrofit.RetrofitClient.instance.openRequest("Bearer ${UserID.userAccessToken}")
            .enqueue(object :Callback<com.redsea.redsea.network.Response.OpenRequest.OPenRequests>{
                override fun onResponse(
                    call: Call<com.redsea.redsea.network.Response.OpenRequest.OPenRequests>,
                    response: Response<com.redsea.redsea.network.Response.OpenRequest.OPenRequests>
                ) {
                    if (response.isSuccessful){
                        openrequest=response.body()!!
                        adapter=OPenRequestAdapter(transiaction,openrequest)
                        binding.opnRequestRecyclerView.adapter=adapter
                        openrequest.reverse()

                    }
                    else{
                        Toast.makeText(context, "response code: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                    binding.openRequestProgress.visibility=View.GONE
                }

                override fun onFailure(call: Call<com.redsea.redsea.network.Response.OpenRequest.OPenRequests>, t: Throwable) {
                    Toast.makeText(context, "failed to fetch${t.message}", Toast.LENGTH_SHORT).show()
                }
            })

    }
    private fun getaccrequestsurvey(){
        val transiaction:FragmentTransaction?=fragmentManager?.beginTransaction()
        binding.openRequestProgress.visibility=View.VISIBLE
        com.redsea.redsea.network.retrofit.RetrofitClient.instance.openRequestsurvey("Bearer ${UserID.userAccessToken}")
            .enqueue(object :Callback<com.redsea.redsea.network.Response.OpenRequest.OPenRequests>{
                override fun onResponse(
                    call: Call<com.redsea.redsea.network.Response.OpenRequest.OPenRequests>,
                    response: Response<com.redsea.redsea.network.Response.OpenRequest.OPenRequests>
                ) {
                    if (response.isSuccessful){
                        openrequest=response.body()!!
                        adapter=OPenRequestAdapter(transiaction,openrequest)
                        binding.opnRequestRecyclerView.adapter=adapter
                        openrequest.reverse()

                    }
                    else{
                        Toast.makeText(context, "response code: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                    binding.openRequestProgress.visibility=View.GONE
                }

                override fun onFailure(call: Call<com.redsea.redsea.network.Response.OpenRequest.OPenRequests>, t: Throwable) {
                    Toast.makeText(context, "failed to fetch${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
    private fun getaccrequesttest(){
        val transiaction:FragmentTransaction?=fragmentManager?.beginTransaction()
        binding.openRequestProgress.visibility=View.VISIBLE
        com.redsea.redsea.network.retrofit.RetrofitClient.instance.openRequesttest("Bearer ${UserID.userAccessToken}")
            .enqueue(object :Callback<com.redsea.redsea.network.Response.OpenRequest.OPenRequests>{
                override fun onResponse(
                    call: Call<com.redsea.redsea.network.Response.OpenRequest.OPenRequests>,
                    response: Response<com.redsea.redsea.network.Response.OpenRequest.OPenRequests>
                ) {
                    if (response.isSuccessful){
                        openrequest=response.body()!!
                        adapter=OPenRequestAdapter(transiaction,openrequest)
                        binding.opnRequestRecyclerView.adapter=adapter
                        openrequest.reverse()

                    }
                    else{
                        Toast.makeText(context, "response code: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                    binding.openRequestProgress.visibility=View.GONE
                }

                override fun onFailure(call: Call<com.redsea.redsea.network.Response.OpenRequest.OPenRequests>, t: Throwable) {
                    Toast.makeText(context, "failed to fetch${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
    private fun getaccrequesttrouble(){
        val transiaction:FragmentTransaction?=fragmentManager?.beginTransaction()
        binding.openRequestProgress.visibility=View.VISIBLE
        com.redsea.redsea.network.retrofit.RetrofitClient.instance.openRequesttrouble("Bearer ${UserID.userAccessToken}")
            .enqueue(object :Callback<com.redsea.redsea.network.Response.OpenRequest.OPenRequests>{
                override fun onResponse(
                    call: Call<com.redsea.redsea.network.Response.OpenRequest.OPenRequests>,
                    response: Response<com.redsea.redsea.network.Response.OpenRequest.OPenRequests>
                ) {
                    if (response.isSuccessful){
                        openrequest=response.body()!!
                        adapter=OPenRequestAdapter(transiaction,openrequest)
                        binding.opnRequestRecyclerView.adapter=adapter
                        openrequest.reverse()

                    }
                    else{
                        Toast.makeText(context, "response code: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                    binding.openRequestProgress.visibility=View.GONE
                }

                override fun onFailure(call: Call<com.redsea.redsea.network.Response.OpenRequest.OPenRequests>, t: Throwable) {
                    Toast.makeText(context, "failed to fetch${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
    private fun filteredrequest(query:String){
        if(query!=null){
            val filteredlist=ArrayList<com.redsea.redsea.network.Response.OpenRequest.OpenRequestItem>()
            for(i in openrequest){
                if(i.well.name.lowercase(Locale.ROOT).contains(query)){
                    filteredlist.add(i)

                }
            }
            if (filteredlist.isEmpty()){

                Toast.makeText(context, "Request Not Found", Toast.LENGTH_SHORT).show()
            }

            else{
                adapter.setfilterrequest(filteredlist)
            }
        }
    }

}