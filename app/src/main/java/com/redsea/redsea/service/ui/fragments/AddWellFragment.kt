package com.redsea.redsea.service.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.redsea.redsea.databinding.FragmentAddWellBinding
import com.redsea.redsea.network.PostData.Publish
import com.redsea.redsea.network.Response.OpenRequest.OpenRequestItem
import com.redsea.redsea.network.Response.PublishWellResponse.PublishWellResponse
import com.redsea.redsea.network.Response.UpdatWellResponse.UpdateWellResponse
import com.redsea.redsea.network.Response.UserWells.UserWellsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import com.redsea.redsea.service.ui.activity.MainActivity
import com.redsea.redsea.network.retrofit.RetrofitClient
import com.redsea.redsea.service.shared.NextBackInteraction
import com.redsea.redsea.service.ui.TitleInterface
import com.redsea.redsea.service.ui.UserID
import com.redsea.redsea.service.ui.activity.mysharedpref
import com.redsea.redsea.service.ui.adapters.adapters.AddWellAdapter


lateinit var addWellAdapter: AddWellAdapter
lateinit var intent: Intent
var optionsFetched = false
var iseditmode: Boolean = false
var edittype: String? = null
var toDay: Int = 0
var toMonth: Int = 0
var toYear: Int = 0
var fromDay: Int = 0
var fromMonth: Int = 0
var fromYear: Int = 0


class AddWellFragment : Fragment(), NextBackInteraction {
    lateinit var binding: FragmentAddWellBinding
    private var publishData: Publish =
        Publish("", "", "", mutableListOf())
    private var optionPos = 0
    private var allList = arrayListOf<ArrayList<Publish>>()
    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {
        try {

            binding = FragmentAddWellBinding.inflate(layoutInflater)
            var linearLayoutManager = LinearLayoutManager(context)
            binding.recyclerViewAddItem.layoutManager = linearLayoutManager
            if (!optionsFetched) {
                binding.addWellProgressBar.visibility = View.VISIBLE
                val dattype = mysharedpref(requireContext()).getDataType()
                when (dattype) {
                    "operations" -> fetchOptionsoperations(requireContext())
                    "wellSurveys" -> fetchOptionssurvey()
                    "test" -> fetchOptionstest()
                }

            }


        } catch (e: Exception) {
            Log.d("CRASHCAUSE", e.message.toString())
        }
        return binding.root
    }

    private fun loadWellData(it: OpenRequestItem) {
        binding.wellNameInputText.setText(it.well.name)
        val from = it.well.from.split("-")
        val to = it.well.to.split("-")


    }

    private fun loadWellDraft(it: UserWellsItem) {
        binding.wellNameInputText.setText(it.name)
        val from = it.from.split("-")
        val to = it.to.split("-")


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? TitleInterface)?.onTextChange("Add new Report", "Add Report")
        initSpinner()
        val dattype = mysharedpref(requireContext()).getDataType()
        when (dattype) {
            "operations" -> fetchOptionsoperations(requireContext())
            "wellSurveys" -> fetchOptionssurvey()
            "test" -> fetchOptionstest()
            "trouble" -> fetchOptionstrouble()
        }

        try {

            binding.recyclerViewAddItem.adapter = addWellAdapter
        } catch (e: Exception) {
            Log.d("WHYCRASH", e.message.toString())
        }
        Log.d("REFRESH WORK", " BINDING")
        arguments?.let { args ->
            iseditmode = args.getBoolean("iseditmode")
            edittype = args.getString("edittype")
            if (iseditmode) {
                if (edittype == "editrequest") {
                    Toast.makeText(context, "editmode on request", Toast.LENGTH_SHORT).show()
                    val wellData =
                        args.getSerializable("openrequest") as OpenRequestItem?
                    wellData?.let { loadWellData(it) }
                } else {
                    Toast.makeText(context, "editmode on draft", Toast.LENGTH_SHORT).show()
                    val wellData =
                        args.getSerializable("draftitem") as UserWellsItem
                    wellData?.let { loadWellDraft(it) }

                }


            }
        }

        if (iseditmode) {
            binding.publishWellBtn.visibility = View.GONE
            binding.updateWellBtn.visibility = View.VISIBLE
            if (edittype == "editdraft") {
                binding.updateWellBtn.setOnClickListener {
                    val updatewell = addWellAdapter.adapter.enteredList()
                    updatewell.name = binding.wellNameInputText.text.toString()
                    updatewell.to = "$toYear-$toMonth-$toDay"
                    updatewell.from = "$fromYear-$fromMonth-$fromDay"
                    validateSpinnersDate()
                    val editdarft =
                        arguments?.getSerializable("editdraftitem") as UserWellsItem?
                    val id = editdarft?.id
                    updatewell(id!!, updatewell)
                }
            } else if (edittype == "editrequest") {
                binding.updateWellBtn.setOnClickListener {
                    val updatewell = addWellAdapter.adapter.enteredList()
                    updatewell.name = binding.wellNameInputText.text.toString()
                    updatewell.to = "$toYear-$toMonth-$toDay"
                    updatewell.from = "$fromYear-$fromMonth-$fromDay"
                    validateSpinnersDate()
                    val openrequest =
                        arguments?.getSerializable("openrequest") as OpenRequestItem?
                    val id = openrequest?.well?.id
                    updatewell(id!!, updatewell)
                }

            }
        }
        binding.publishWellBtn.setOnClickListener {
            when (dattype) {
                "operations" -> {
                    publish("operations")
                }

                "wellSurveys" -> {
                    publish("wellSurveys")

                }

                "test" -> {
                    publish("test")
                }

                "trouble" -> {
                    publish("trouble")
                }
            }

        }

        binding.saveDraftBtn.setOnClickListener {
            when (dattype) {
                "operations" -> {
                    saveDraft("operations")
                }

                "wellSurveys" -> {
                    saveDraft("wellSurveys")

                }

                "test" -> {
                    saveDraft("test")
                }

                "trouble" -> {
                    saveDraft("trouble")
                }
            }


        }
    }

    private fun initSpinner() {

        val days = listOf(
            1, 2, 3, 4, 5, 6,
            7, 8, 9, 10, 11,
            12, 13, 14, 15, 16,
            17, 18, 19, 20, 21,
            22, 23, 24, 25, 26,
            27, 28, 29, 30, 31
        )
        val month =
            listOf(
                "Jan", "Feb", "Mar",
                "Apr", "May", "Jun",
                "Jul", "Aug", "Sep",
                "Oct", "Nov", "Dec"
            )
        val year = listOf(
            2033, 2032, 2030,
            2030, 2029, 2028,
            2027, 2026, 2025,
            2024, 2023, 2022

        )
        // Get the current date
        val currentDate = Calendar.getInstance()
        val currentDay = currentDate.get(Calendar.DAY_OF_MONTH)
        val currentMonth = currentDate.get(Calendar.MONTH) + 1
        val currentYear = currentDate.get(Calendar.YEAR)
        //indexes in lists
        val dayIndex = days.indexOf(currentDay)
        val monthIndex = currentMonth - 1
        val yearIndex = year.indexOf(currentYear)


        val dayAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, days)
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        val monthAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, month)
        val yearAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, year)

        binding.fromDaySpinner.apply {
            adapter = dayAdapter
            setSelection(dayIndex)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    fromDay = days[position]

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {


                }

            }
        }
        binding.fromMonthSpinner.apply {
            adapter = monthAdapter
            setSelection(monthIndex)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    fromMonth = position + 1

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {


                }

            }
        }
        binding.fromYearSpinner.apply {
            adapter = yearAdapter
            setSelection(yearIndex)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    fromYear = year[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }
        }
        binding.toDaySpinner.apply {
            adapter = dayAdapter
            setSelection(dayIndex)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    toDay = days[position]

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }
        }
        binding.toMonthSpinner.apply {
            adapter = monthAdapter
            setSelection(monthIndex)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    toMonth = position + 1

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }
        }
        binding.toYearSpinner.apply {
            adapter = yearAdapter
            setSelection(yearIndex)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    toYear = year[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }
        }
    }


    private fun fetchOptionsoperations(context: Context) {
        Log.d("HelloRetrofit", "no connection")
        try {
            binding.addWellProgressBar.visibility = View.VISIBLE

            RetrofitClient.instance.getWells("Bearer ${UserID.userAccessToken}")
                .enqueue(object :
                    Callback<com.redsea.redsea.network.Response.WellOptions.WellOptionsResponse> {
                    override fun onResponse(
                        call: Call<com.redsea.redsea.network.Response.WellOptions.WellOptionsResponse>,
                        response: Response<com.redsea.redsea.network.Response.WellOptions.WellOptionsResponse>
                    ) {
                        if (response.isSuccessful) {
                            val wellOptionsResponse = response.body()
                            if (wellOptionsResponse != null) {

                                if (allList.isEmpty()) {
                                    for ((counter, item) in wellOptionsResponse.withIndex()) {
                                        allList.add(arrayListOf())
                                        for (item2 in item.structures) {
                                            allList[counter].add(publishData)
                                        }
                                    }
                                }


                                addWellAdapter =
                                    AddWellAdapter(wellOptionsResponse, this@AddWellFragment)
                                binding.recyclerViewAddItem.adapter = addWellAdapter
                            } else {

                                Toast.makeText(
                                    context,
                                    "Null",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        } else {
                            Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show()
                            Log.d("HelloRetrofit", "No1")
                            Log.d("Response Code", response.code().toString())
                        }
                        binding.addWellProgressBar.visibility = View.GONE
                    }

                    override fun onFailure(
                        call: Call<com.redsea.redsea.network.Response.WellOptions.WellOptionsResponse>,
                        t: Throwable
                    ) {
                        Toast.makeText(
                            context,
                            "Failed to fetch data: ${t.message}",
                            Toast.LENGTH_LONG
                        )
                            .show()
                        Log.d("Options Data", t.message.toString())
                        binding.addWellProgressBar.visibility = View.GONE
                    }

                })
        } catch (e: Exception) {
            Log.d("CRASHRETROFIT", e.message.toString())

        }
    }

    private fun fetchOptionssurvey() {
        Log.d("HelloRetrofit", "no connection")
        try {
            binding.addWellProgressBar.visibility = View.VISIBLE

            RetrofitClient.instance.getWellssurvey("Bearer ${UserID.userAccessToken}")
                .enqueue(object :
                    Callback<com.redsea.redsea.network.Response.WellOptions.WellOptionsResponse> {
                    override fun onResponse(
                        call: Call<com.redsea.redsea.network.Response.WellOptions.WellOptionsResponse>,
                        response: Response<com.redsea.redsea.network.Response.WellOptions.WellOptionsResponse>
                    ) {
                        if (response.isSuccessful) {
                            val wellOptionsResponse = response.body()
                            if (wellOptionsResponse != null) {
                                if (allList.isEmpty()) {
                                    for ((counter, item) in wellOptionsResponse.withIndex()) {
                                        allList.add(arrayListOf())
                                        for (item2 in item.structures) {
                                            allList[counter].add(publishData)
                                        }
                                    }
                                }
                                addWellAdapter =
                                    AddWellAdapter(wellOptionsResponse, this@AddWellFragment)
                                binding.recyclerViewAddItem.adapter = addWellAdapter
                                Log.d(
                                    "ChildData",
                                    "start + survey + $optionPos + ${0} +${allList.size} + ${allList[1].size}"
                                )
                            } else {
                                Toast.makeText(
                                    context,
                                    "Null",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        } else {
                            Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show()
                            Log.d("HelloRetrofit", "No1")
                            Log.d("Response Code", response.code().toString())
                        }
                        binding.addWellProgressBar.visibility = View.GONE
                    }

                    override fun onFailure(
                        call: Call<com.redsea.redsea.network.Response.WellOptions.WellOptionsResponse>,
                        t: Throwable
                    ) {
                        Toast.makeText(
                            context,
                            "Failed to fetch data: ${t.message}",
                            Toast.LENGTH_LONG
                        )
                            .show()
                        Log.d("Options Data", t.message.toString())
                        binding.addWellProgressBar.visibility = View.GONE
                    }

                })
        } catch (e: Exception) {
            Log.d("CRASHRETROFIT", e.message.toString())

        }

    }

    private fun fetchOptionstest() {
        Log.d("HelloRetrofit", "no connection")
        try {
            binding.addWellProgressBar.visibility = View.VISIBLE

            RetrofitClient.instance.getWellstest("Bearer ${UserID.userAccessToken}")
                .enqueue(object :
                    Callback<com.redsea.redsea.network.Response.WellOptions.WellOptionsResponse> {
                    override fun onResponse(
                        call: Call<com.redsea.redsea.network.Response.WellOptions.WellOptionsResponse>,
                        response: Response<com.redsea.redsea.network.Response.WellOptions.WellOptionsResponse>
                    ) {
                        if (response.isSuccessful) {
                            val wellOptionsResponse = response.body()
                            if (wellOptionsResponse != null) {
                                if (allList.isEmpty()) {
                                    for ((counter, item) in wellOptionsResponse.withIndex()) {
                                        allList.add(arrayListOf())
                                        for (item2 in item.structures) {
                                            allList[counter].add(publishData)
                                        }
                                    }
                                }
                                addWellAdapter =
                                    AddWellAdapter(wellOptionsResponse, this@AddWellFragment)
                                binding.recyclerViewAddItem.adapter = addWellAdapter
                                Log.d(
                                    "ChildData",
                                    "start + set + $optionPos + ${0} +${allList.size} + ${allList[1].size}"
                                )

                            } else {
                                Toast.makeText(
                                    context,
                                    "Null",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        } else {
                            Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show()
                            Log.d("HelloRetrofit", "No1")
                            Log.d("Response Code", response.code().toString())
                        }
                        binding.addWellProgressBar.visibility = View.GONE
                    }

                    override fun onFailure(
                        call: Call<com.redsea.redsea.network.Response.WellOptions.WellOptionsResponse>,
                        t: Throwable
                    ) {
                        Toast.makeText(
                            context,
                            "Failed to fetch data: ${t.message}",
                            Toast.LENGTH_LONG
                        )
                            .show()
                        Log.d("Options Data", t.message.toString())
                        binding.addWellProgressBar.visibility = View.GONE
                    }

                })
        } catch (e: Exception) {
            Log.d("CRASHRETROFIT", e.message.toString())

        }
    }

    private fun fetchOptionstrouble() {
        Log.d("HelloRetrofit", "no connection")
        try {
            binding.addWellProgressBar.visibility = View.VISIBLE

            RetrofitClient.instance.getWellstrouble("Bearer ${UserID.userAccessToken}")
                .enqueue(object :
                    Callback<com.redsea.redsea.network.Response.WellOptions.WellOptionsResponse> {
                    override fun onResponse(
                        call: Call<com.redsea.redsea.network.Response.WellOptions.WellOptionsResponse>,
                        response: Response<com.redsea.redsea.network.Response.WellOptions.WellOptionsResponse>
                    ) {
                        if (response.isSuccessful) {
                            val wellOptionsResponse = response.body()
                            if (wellOptionsResponse != null) {
                                if (allList.isEmpty()) {
                                    for ((counter, item) in wellOptionsResponse.withIndex()) {
                                        allList.add(arrayListOf())
                                        for (item2 in item.structures) {
                                            allList[counter].add(publishData)
                                        }
                                    }
                                }
                                addWellAdapter =
                                    AddWellAdapter(wellOptionsResponse, this@AddWellFragment)
                                binding.recyclerViewAddItem.adapter = addWellAdapter
                                Log.d(
                                    "ChildData",
                                    "start + trouble + $optionPos + ${0} +${allList.size} + ${allList[1].size}"
                                )

                            } else {
                                Toast.makeText(
                                    context,
                                    "Null",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        } else {
                            Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show()
                            Log.d("HelloRetrofit", "No1")
                            Log.d("Response Code", response.code().toString())
                        }
                        binding.addWellProgressBar.visibility = View.GONE
                    }

                    override fun onFailure(
                        call: Call<com.redsea.redsea.network.Response.WellOptions.WellOptionsResponse>,
                        t: Throwable
                    ) {
                        Toast.makeText(
                            context,
                            "Failed to fetch data: ${t.message}",
                            Toast.LENGTH_LONG
                        )
                            .show()
                        Log.d("Options Data", t.message.toString())
                        binding.addWellProgressBar.visibility = View.GONE
                    }

                })
        } catch (e: Exception) {
            Log.d("CRASHRETROFIT", e.message.toString())

        }
    }

    fun saveDraft(publish: Publish) {
        binding.addWellProgressBar.visibility = View.VISIBLE
        RetrofitClient.instance.saveDraft("Bearer ${UserID.userAccessToken}", publish)
            .enqueue(object :
                Callback<com.redsea.redsea.network.Response.SaveDraft.SaveDraftResponse> {
                override fun onResponse(
                    call: Call<com.redsea.redsea.network.Response.SaveDraft.SaveDraftResponse>,
                    response: Response<com.redsea.redsea.network.Response.SaveDraft.SaveDraftResponse>
                ) {
                    if (response.isSuccessful) {
                        val wellOptionsResponse = response.body()
                        Log.d("SAVEDDRAFT", "$wellOptionsResponse")
                        Toast.makeText(context, "Draft Saved", Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(context, "Empty Field Required", Toast.LENGTH_SHORT).show()

                    }
                    binding.addWellProgressBar.visibility = View.GONE
                }


                override fun onFailure(
                    call: Call<com.redsea.redsea.network.Response.SaveDraft.SaveDraftResponse>,
                    t: Throwable
                ) {
                    Toast.makeText(
                        context,
                        "Failed to fetch data: ${t.message}",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    Log.d("Options Data", t.message.toString())
                    binding.addWellProgressBar.visibility = View.GONE
                }

            })
    }

    fun saveDraftsurvey(publish: Publish) {
        binding.addWellProgressBar.visibility = View.VISIBLE
        RetrofitClient.instance.saveDraftsurvey("Bearer ${UserID.userAccessToken}", publish)
            .enqueue(object :
                Callback<com.redsea.redsea.network.Response.SaveDraft.SaveDraftResponse> {
                override fun onResponse(
                    call: Call<com.redsea.redsea.network.Response.SaveDraft.SaveDraftResponse>,
                    response: Response<com.redsea.redsea.network.Response.SaveDraft.SaveDraftResponse>
                ) {
                    if (response.isSuccessful) {
                        val wellOptionsResponse = response.body()
                        Log.d("SAVEDDRAFT", wellOptionsResponse.toString())
                        Toast.makeText(context, "Draft Saved", Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(
                            context,
                            "Empty Field Required",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                    binding.addWellProgressBar.visibility = View.GONE
                }


                override fun onFailure(
                    call: Call<com.redsea.redsea.network.Response.SaveDraft.SaveDraftResponse>,
                    t: Throwable
                ) {
                    Toast.makeText(
                        context,
                        "Failed to fetch data: ${t.message}",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    Log.d("Options Data", t.message.toString())
                    binding.addWellProgressBar.visibility = View.GONE
                }

            })
    }

    fun saveDrafttest(publish: Publish) {
        binding.addWellProgressBar.visibility = View.VISIBLE
        RetrofitClient.instance.saveDrafttest("Bearer ${UserID.userAccessToken}", publish)
            .enqueue(object :
                Callback<com.redsea.redsea.network.Response.SaveDraft.SaveDraftResponse> {
                override fun onResponse(
                    call: Call<com.redsea.redsea.network.Response.SaveDraft.SaveDraftResponse>,
                    response: Response<com.redsea.redsea.network.Response.SaveDraft.SaveDraftResponse>
                ) {
                    if (response.isSuccessful) {
                        val wellOptionsResponse = response.body()
                        Log.d("SAVEDDRAFT", wellOptionsResponse.toString())
                        Toast.makeText(context, "Draft Saved", Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(
                            context,
                            "Empty Field Required",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                    binding.addWellProgressBar.visibility = View.GONE
                }


                override fun onFailure(
                    call: Call<com.redsea.redsea.network.Response.SaveDraft.SaveDraftResponse>,
                    t: Throwable
                ) {
                    Toast.makeText(
                        context,
                        "Failed to fetch data: ${t.message}",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    Log.d("Options Data", t.message.toString())
                    binding.addWellProgressBar.visibility = View.GONE
                }

            })
    }

    fun saveDrafttrouble(publish: Publish) {
        binding.addWellProgressBar.visibility = View.VISIBLE
        RetrofitClient.instance.saveDrafttrouble("Bearer ${UserID.userAccessToken}", publish)
            .enqueue(object :
                Callback<com.redsea.redsea.network.Response.SaveDraft.SaveDraftResponse> {
                override fun onResponse(
                    call: Call<com.redsea.redsea.network.Response.SaveDraft.SaveDraftResponse>,
                    response: Response<com.redsea.redsea.network.Response.SaveDraft.SaveDraftResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.d("response", "$response.body()")
                        val wellOptionsResponse = response.body()
                        Log.d("SAVEDDRAFT", wellOptionsResponse.toString())
                        Toast.makeText(context, "Draft Saved", Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(
                            context,
                            "Empty Field Required}",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                    binding.addWellProgressBar.visibility = View.GONE
                }


                override fun onFailure(
                    call: Call<com.redsea.redsea.network.Response.SaveDraft.SaveDraftResponse>,
                    t: Throwable
                ) {
                    Toast.makeText(
                        context,
                        "Failed to fetch data: ${t.message}",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    Log.d("Options Data", t.message.toString())
                    binding.addWellProgressBar.visibility = View.GONE
                }

            })
    }


    fun publishWell(publish: Publish) {
        binding.addWellProgressBar.visibility = View.VISIBLE
        RetrofitClient.instance.publishWell("Bearer ${UserID.userAccessToken}", publish)
            .enqueue(object :
                Callback<PublishWellResponse> {
                override fun onResponse(
                    call: Call<PublishWellResponse>,
                    response: Response<PublishWellResponse>
                ) {
                    if (response.isSuccessful) {
                        val wellOptionsResponse = response.body()
                        Log.d("SAVEDDRAFT", wellOptionsResponse.toString())
                        Toast.makeText(context, "Well Published", Toast.LENGTH_SHORT).show()

                        intent = Intent(context, MainActivity::class.java)
                        intent.putExtra("fragmentToLoad", "MainCategoryFragment")
                        startActivity(intent)
                    } else {
                        try {
                            // Print the error response body
                            val errorBody = response.errorBody()?.string()
                            Log.e("FAILED_RESPONSE", errorBody ?: "Error body is null or empty")
                            Log.e("FAILED_RESPONSE", response?.code().toString())
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        Toast.makeText(
                            context,
                            "Empty Field Required",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                    binding.addWellProgressBar.visibility = View.GONE
                }


                override fun onFailure(
                    call: Call<PublishWellResponse>,
                    t: Throwable
                ) {
                    Toast.makeText(context, "Failed to fetch data: ${t.message}", Toast.LENGTH_LONG)
                        .show()
                    Log.d("Options Data", t.message.toString())
                    binding.addWellProgressBar.visibility = View.GONE
                }

            })
    }

    fun publishsurvey(publish: Publish) {
        binding.addWellProgressBar.visibility = View.VISIBLE
        RetrofitClient.instance.publishsurvey("Bearer ${UserID.userAccessToken}", publish)
            .enqueue(object :
                Callback<PublishWellResponse> {
                override fun onResponse(
                    call: Call<PublishWellResponse>,
                    response: Response<PublishWellResponse>
                ) {
                    if (response.isSuccessful) {
                        val wellOptionsResponse = response.body()
                        Log.d("SAVEDDRAFT", wellOptionsResponse.toString())
                        Toast.makeText(context, "Well Published", Toast.LENGTH_SHORT).show()

                        intent = Intent(context, MainActivity::class.java)
                        intent.putExtra("fragmentToLoad", "MainCategoryFragment")
                        startActivity(intent)
                    } else {
                        try {
                            // Print the error response body
                            val errorBody = response.errorBody()?.string()
                            Log.e("FAILED_RESPONSE", errorBody ?: "Error body is null or empty")
                            Log.e("FAILED_RESPONSE", response?.code().toString())
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        Toast.makeText(
                            context,
                            "Empty Field Required",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                    binding.addWellProgressBar.visibility = View.GONE
                }


                override fun onFailure(
                    call: Call<PublishWellResponse>,
                    t: Throwable
                ) {
                    Toast.makeText(context, "Failed to fetch data: ${t.message}", Toast.LENGTH_LONG)
                        .show()
                    Log.d("Options Data", t.message.toString())
                    binding.addWellProgressBar.visibility = View.GONE
                }

            })
    }

    fun publishtset(publish: Publish) {
        binding.addWellProgressBar.visibility = View.VISIBLE
        RetrofitClient.instance.publishtest("Bearer ${UserID.userAccessToken}", publish)
            .enqueue(object :
                Callback<PublishWellResponse> {
                override fun onResponse(
                    call: Call<PublishWellResponse>,
                    response: Response<PublishWellResponse>
                ) {
                    if (response.isSuccessful) {
                        val wellOptionsResponse = response.body()
                        Log.d("SAVEDDRAFT", wellOptionsResponse.toString())
                        Toast.makeText(context, "Well Published", Toast.LENGTH_SHORT).show()

                        intent = Intent(context, MainActivity::class.java)
                        intent.putExtra("fragmentToLoad", "MainCategoryFragment")
                        startActivity(intent)
                    } else {
                        try {
                            // Print the error response body
                            val errorBody = response.errorBody()?.string()
                            Log.e("FAILED_RESPONSE", errorBody ?: "Error body is null or empty")
                            Log.e("FAILED_RESPONSE", response?.code().toString())
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        Toast.makeText(
                            context,
                            "Empty Field Required",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                    binding.addWellProgressBar.visibility = View.GONE
                }


                override fun onFailure(
                    call: Call<PublishWellResponse>,
                    t: Throwable
                ) {
                    Toast.makeText(context, "Failed to fetch data: ${t.message}", Toast.LENGTH_LONG)
                        .show()
                    Log.d("Options Data", t.message.toString())
                    binding.addWellProgressBar.visibility = View.GONE
                }

            })
    }

    fun publishtouble(publish: Publish) {
        binding.addWellProgressBar.visibility = View.VISIBLE
        RetrofitClient.instance.publishtrouble("Bearer ${UserID.userAccessToken}", publish)
            .enqueue(object :
                Callback<PublishWellResponse> {
                override fun onResponse(
                    call: Call<PublishWellResponse>,
                    response: Response<PublishWellResponse>
                ) {
                    if (response.isSuccessful) {
                        val wellOptionsResponse = response.body()
                        Log.d("SAVEDDRAFT", wellOptionsResponse.toString())
                        Toast.makeText(context, "Well Published", Toast.LENGTH_SHORT).show()

                        intent = Intent(context, MainActivity::class.java)
                        intent.putExtra("fragmentToLoad", "MainCategoryFragment")
                        startActivity(intent)
                    } else {
                        try {
                            // Print the error response body
                            val errorBody = response.errorBody()?.string()
                            Log.e("FAILED_RESPONSE", errorBody ?: "Error body is null or empty")
                            Log.e("FAILED_RESPONSE", response?.code().toString())
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        Toast.makeText(
                            context,
                            "Empty Field Required",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                    binding.addWellProgressBar.visibility = View.GONE
                }


                override fun onFailure(
                    call: Call<PublishWellResponse>,
                    t: Throwable
                ) {
                    Toast.makeText(context, "Failed to fetch data: ${t.message}", Toast.LENGTH_LONG)
                        .show()
                    Log.d("Options Data", t.message.toString())
                    binding.addWellProgressBar.visibility = View.GONE
                }

            })
    }

    private fun updatewell(id: Int, publish: Publish) {
        RetrofitClient.instance.updatewell("Bearer ${UserID.userAccessToken}", id, publish)
            .enqueue(object :
                Callback<UpdateWellResponse> {
                override fun onResponse(
                    call: Call<UpdateWellResponse>,
                    response: Response<UpdateWellResponse>
                ) {
                    if (response.isSuccessful) {
                        val wellOptionsResponse = response.body()
                        Log.d("UPDATEWELL", wellOptionsResponse.toString())
                        Toast.makeText(context, "well updated", Toast.LENGTH_SHORT).show()


                    } else {
                        try {
                            // Print the error response body
                            val errorBody = response.errorBody()?.string()
                            Log.e("FAILED_RESPONSE", errorBody ?: "Error body is null or empty")
                            Log.e("FAILED_RESPONSE", response?.code().toString())
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        Toast.makeText(
                            context,
                            "Empty Field Required",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                    binding.addWellProgressBar.visibility = View.GONE

                }

                override fun onFailure(
                    call: Call<UpdateWellResponse>,
                    t: Throwable
                ) {
                    Toast.makeText(context, "Failed to fetch data: ${t.message}", Toast.LENGTH_LONG)
                        .show()
                    Log.d("Options Data", t.message.toString())
                    binding.addWellProgressBar.visibility = View.GONE
                }
            })

    }

    private fun validateSpinnersDate() {
        val todayCalendar = Calendar.getInstance()
        val fromDatespinner = Calendar.getInstance().apply {
            set(Calendar.YEAR, fromYear)
            set(Calendar.MONTH, fromMonth - 1)
            set(Calendar.DAY_OF_MONTH, fromDay)
        }
        if (fromDatespinner.before(todayCalendar)) {
            Toast.makeText(
                context, "from date starts from today's date",
                Toast.LENGTH_SHORT
            ).show()

        }
        val toDatespinner = Calendar.getInstance().apply {
            set(Calendar.YEAR, toYear)
            set(Calendar.MONTH, toMonth - 1)
            set(Calendar.DAY_OF_MONTH, toDay)
        }

        if (toDatespinner.before(fromDatespinner)) {
            Toast.makeText(
                context, "to date can't be set before from date",
                Toast.LENGTH_SHORT
            ).show()

        }

    }

    override fun onCLickNext(position: Int) {
        try {
            val l = allList[optionPos][position]
            addWellAdapter.setData(l)


            if (addWellAdapter.adapter.enteredList().well_data.isNotEmpty()) {
                allList[optionPos][position - 1] = addWellAdapter.adapter.enteredList()
            }

        } catch (e: Exception) {
            addWellAdapter.setData(null)
        }

    }

    override fun onCLickBack(position: Int) {
        try {
            val l = allList[optionPos][position]
            addWellAdapter.setData(l)
            Log.d("ChildData", "back + $optionPos + ${position} + ${l}")

        } catch (e: Exception) {
            addWellAdapter.setData(null)
        }

    }

    override fun onClickOption(position: Int) {
        optionPos = position

        try {
            val l = allList[optionPos][0]
            addWellAdapter.setData(l)

        } catch (e: Exception) {
            addWellAdapter.setData(null)
        }
    }

    override fun onClickDone(position: Int) {
        try {
            if (addWellAdapter.adapter.enteredList().well_data.isNotEmpty())
                allList[optionPos][position - 1] = addWellAdapter.adapter.enteredList()

        } catch (e: Exception) {
            addWellAdapter.setData(null)

        }
    }

    fun publish(type: String) {
        val publish = getPublishData()
        publish.name = binding.wellNameInputText.text.toString()
        publish.to = "$toYear-$toDay-$toMonth"
        publish.from = "$fromYear-$fromDay-$fromMonth"
        validateSpinnersDate()
        Log.d("PublishData","PublishData + $publish")
        when(type){
            "operations" -> publishWell(publish)
            "wellSurveys" -> publishsurvey(publish)
            "test" -> publishtset(publish)
            "trouble" -> publishtouble(publish)
        }
    }

    fun saveDraft(type: String) {
        val publish = getPublishData()
        publish.name = binding.wellNameInputText.text.toString()
        publish.to = "$toYear-$toDay-$toMonth"
        publish.from = "$fromYear-$fromDay-$fromMonth"
        validateSpinnersDate()
        Log.d("PublishData","PublishData + $publish")
        when(type){
            "operations" -> saveDraft(publish)
            "wellSurveys" -> saveDraftsurvey(publish)
            "test" -> saveDrafttest(publish)
            "trouble" -> saveDrafttrouble(publish)
        }
    }

    private fun getPublishData(): Publish {
        val publish = Publish("","","", mutableListOf())

        for (item in allList) {
            for (item2 in item){
                publish.well_data.addAll(item2.well_data)
            }
        }
        return publish
    }
}





