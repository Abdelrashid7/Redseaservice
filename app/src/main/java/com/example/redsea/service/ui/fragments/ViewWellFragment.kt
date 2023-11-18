package com.example.redsea.service.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.redsea.R
import com.example.redsea.databinding.FragmentViewWellBinding
import com.example.redsea.network.ViewWellsResponse.ViewWells
import com.example.redsea.network.retrofit.RetrofitClient
import com.example.redsea.service.ui.TitleInterface
import com.example.redsea.service.ui.adapters.adapters.ViewWellAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

lateinit var  viewWell : ViewWells
lateinit var adapter: ViewWellAdapter


class ViewWellFragment : Fragment(){
    lateinit var binding: FragmentViewWellBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewWellBinding.inflate(layoutInflater)
        binding.viewWellRecyclerView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? TitleInterface)?.onTextChange("View", getString(R.string.view_toolbar))

        getWells()


    }

    private fun getWells() {
        val fragmentTransaction = fragmentManager?.beginTransaction()
        binding.viewWellProgress.visibility = View.VISIBLE
        RetrofitClient.instance.getViewWells("Bearer 1|z6tuJt8kLXVVoKuxYsmwdcWQhIUKVpov6v7cCN3Zcd95947e")
            .enqueue(object : Callback<ViewWells> {
                override fun onResponse(
                    call: Call<ViewWells>,
                    response: Response<ViewWells>
                ) {
                    if (response.isSuccessful) {
                        viewWell = response.body()!!
                        adapter = ViewWellAdapter(fragmentTransaction , viewWell)
                        Log.d("WELL DATA", viewWell.toString())
                        binding.viewWellRecyclerView.adapter = adapter

                    } else {
                            Toast.makeText(
                                context,
                                "NullHELLO",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    binding.viewWellProgress.visibility = View.GONE
                    }


                override fun onFailure(call: Call<ViewWells>, t: Throwable) {
                    Toast.makeText(
                        context,
                        "Failed to fetch data: ${t.message}",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    Log.d("Options Data", t.message.toString())
                    binding.viewWellProgress.visibility = View.GONE
                }

            })
    }
}