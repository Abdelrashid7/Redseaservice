package com.example.redsea.service.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.redsea.R
import com.example.redsea.databinding.FragmentViewWellBinding
import com.example.redsea.network.ViewWellsResponse.ViewWells
import com.example.redsea.network.ViewWellsResponse.ViewWellsItem
import com.example.redsea.network.retrofit.RetrofitClient
import com.example.redsea.service.ui.TitleInterface
import com.example.redsea.service.ui.UserID
import com.example.redsea.service.ui.activity.mysharedpref
import com.example.redsea.service.ui.adapters.adapters.ViewWellAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

lateinit var  viewWell : ViewWells
lateinit var adapter:ViewWellAdapter


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
        val dattype= mysharedpref(requireContext()).getDataType()
        when (dattype) {
            "operations" -> getWellsoperations()
            "wellSurveys" -> getWellssurvey()
            "test" -> getWellstest()
            "trouble" -> getWellstrouble()
        }

        binding.searchWellSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filteredlist(newText)
                return true
            }

        })
    }
    private fun filteredlist(query:String?){
        if (query!=null) {
            val filteredlist= mutableListOf<ViewWellsItem>()
            for (i in viewWell) {
                if(i.name.lowercase(Locale.ROOT).contains(query)){

                    filteredlist.add(i)
                }
            }
            if (filteredlist.isEmpty()){
                Toast.makeText(context, "not found", Toast.LENGTH_SHORT).show()
            }
            else{

                adapter.setFilterlist(filteredlist)
            }

        }

        }
    fun initfilterspinner() {
        val filterlist = listOf(
            "sort by recent","sort by name", "sort by id", "sort by from date",
            "sort by to date", "sort by create date", "sort by update date"
        )
        val filteradapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                filterlist
            )
        filteradapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        binding.filterSpinner.apply {
            adapter=filteradapter
            onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when(position){
                        0->{
                            sort(viewWell,"recent")

                        }
                        1->{
                            sort(viewWell,"name")


                        }
                        2->{
                            sort(viewWell,"id")
                        }
                        3->{
                            sort(viewWell,"from")
                        }
                        4->{
                            sort(viewWell,"to")
                        }
                        5->{
                            sort(viewWell,"create")
                        }
                        6->{
                            sort(viewWell,"update")
                        }
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }

        }
    }




    private fun getWellsoperations() {
        Toast.makeText(context, "Operation Data", Toast.LENGTH_SHORT).show()
        val fragmentTransaction = fragmentManager?.beginTransaction()
        binding.viewWellProgress.visibility = View.VISIBLE
        RetrofitClient.instance.getViewWells("Bearer ${UserID.userAccessToken}")
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
                        initfilterspinner()

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
                    Toast.makeText(context, "Failed to fetch data: ${t.message}", Toast.LENGTH_LONG).show()
                    Log.d("Options Data", t.message.toString())
                    binding.viewWellProgress.visibility = View.GONE
                }

            })
    }
    private fun getWellssurvey(){
        Toast.makeText(context, "Survey Data", Toast.LENGTH_SHORT).show()
        binding.viewWellProgress.visibility = View.VISIBLE
        val fragmentTransaction = fragmentManager?.beginTransaction()
        RetrofitClient.instance.getsurveyWells("Bearer ${UserID.userAccessToken}")
            .enqueue(object : Callback<ViewWells> {
                override fun onResponse(call: Call<ViewWells>, response: Response<ViewWells>) {
                    if (response.isSuccessful){
                        viewWell = response.body()!!
                        adapter = ViewWellAdapter(fragmentTransaction , viewWell)
                        Log.d("SURVEY DATA", viewWell.toString())
                        binding.viewWellRecyclerView.adapter = adapter
                        initfilterspinner()

                    }
                    else {
                        Toast.makeText(
                            context,
                            "NullHELLO",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }

                override fun onFailure(call: Call<ViewWells>, t: Throwable) {
                    Toast.makeText(context, "Failed to fetch data: ${t.message}", Toast.LENGTH_LONG).show()
                    Log.d("Options Data", t.message.toString())
                    binding.viewWellProgress.visibility = View.GONE

                }

            })
    }
    private fun getWellstest(){
        Toast.makeText(context, "TEST DATA", Toast.LENGTH_SHORT).show()
        binding.viewWellProgress.visibility = View.VISIBLE
        val fragmentTransaction = fragmentManager?.beginTransaction()
        RetrofitClient.instance.gettestWells("Bearer ${UserID.userAccessToken}")
            .enqueue(object : Callback<ViewWells> {
                override fun onResponse(call: Call<ViewWells>, response: Response<ViewWells>) {
                    if (response.isSuccessful){
                        viewWell = response.body()!!
                        adapter = ViewWellAdapter(fragmentTransaction , viewWell)
                        Log.d("WELL DATA", viewWell.toString())
                        binding.viewWellRecyclerView.adapter = adapter
                        initfilterspinner()

                    }
                    else {
                        Toast.makeText(
                            context,
                            "NullHELLO",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }

                override fun onFailure(call: Call<ViewWells>, t: Throwable) {
                    Toast.makeText(context, "Failed to fetch data: ${t.message}", Toast.LENGTH_LONG).show()
                    Log.d("Options Data", t.message.toString())
                    binding.viewWellProgress.visibility = View.GONE

                }

            })


    }
    private fun getWellstrouble() {
        Toast.makeText(context, "Trouble Data", Toast.LENGTH_SHORT).show()
        binding.viewWellProgress.visibility = View.VISIBLE
        val fragmentTransaction = fragmentManager?.beginTransaction()
        RetrofitClient.instance.gettroubleWells("Bearer ${UserID.userAccessToken}")
            .enqueue(object : Callback<ViewWells> {
                override fun onResponse(call: Call<ViewWells>, response: Response<ViewWells>) {
                    if (response.isSuccessful){
                        viewWell = response.body()!!
                        adapter = ViewWellAdapter(fragmentTransaction , viewWell)
                        Log.d("TROUBLE DATA", viewWell.toString())
                        binding.viewWellRecyclerView.adapter = adapter
                        initfilterspinner()

                    }
                    else {
                        Toast.makeText(
                            context,
                            "NullHELLO",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }

                override fun onFailure(call: Call<ViewWells>, t: Throwable) {
                    Toast.makeText(context, "Failed to fetch data: ${t.message}", Toast.LENGTH_LONG).show()
                    Log.d("Options Data", t.message.toString())
                    binding.viewWellProgress.visibility = View.GONE

                }

            })
    }
    fun sort(list:ViewWells,type:String){
        when(type){
            "recent"->{
                binding.viewWellProgress.visibility=View.VISIBLE
                list.sortBy { it.id }
                list.reverse()
                adapter.notifyDataSetChanged()
                binding.viewWellProgress.visibility=View.GONE

            }
            "name"->{
                binding.viewWellProgress.visibility=View.VISIBLE
                list.sortBy { it.name }
                adapter.notifyDataSetChanged()
                binding.viewWellProgress.visibility=View.GONE

            }
            "id"->{
                binding.viewWellProgress.visibility=View.VISIBLE
                list.sortBy { it.id }
                adapter.notifyDataSetChanged()
                binding.viewWellProgress.visibility=View.GONE
            }
            "from"->{
                binding.viewWellProgress.visibility=View.VISIBLE
                list.sortBy { it.from }
                adapter.notifyDataSetChanged()
                binding.viewWellProgress.visibility=View.GONE
            }
            "to"->{
                binding.viewWellProgress.visibility=View.VISIBLE
                list.sortBy { it.to }
                adapter.notifyDataSetChanged()
                binding.viewWellProgress.visibility=View.GONE
            }
            "create"->{
                binding.viewWellProgress.visibility=View.VISIBLE
                list.sortBy { it.created_at }
                adapter.notifyDataSetChanged()
                binding.viewWellProgress.visibility=View.GONE
            }
            "update"->{
                binding.viewWellProgress.visibility=View.VISIBLE
                list.sortBy { it.updated_at }
                adapter.notifyDataSetChanged()
                binding.viewWellProgress.visibility=View.GONE
            }

        }

    }


}
