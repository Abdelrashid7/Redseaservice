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
import com.redsea.redsea.network.Response.SaveDraft.SaveDraftResponse
import com.redsea.redsea.network.Response.ShowRequest.ShowRequest
import com.redsea.redsea.network.Response.UpdatWellResponse.UpdateWellResponse
import com.redsea.redsea.network.Response.UserWellData.UserWellData
import com.redsea.redsea.network.Response.UserWells.UserWellsItem
import com.redsea.redsea.network.Response.WellOptions.StructureDescription
import com.redsea.redsea.network.Response.WellOptions.WellOptionsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import com.redsea.redsea.service.ui.activity.MainActivity
import com.redsea.redsea.network.retrofit.RetrofitClient
import com.redsea.redsea.service.shared.NextBackInteraction
import com.redsea.redsea.service.test.WellData
import com.redsea.redsea.service.ui.TitleInterface
import com.redsea.redsea.service.ui.UserID
import com.redsea.redsea.service.ui.activity.mysharedpref
import com.redsea.redsea.service.ui.adapters.adapters.AddWellAdapter


lateinit var addWellAdapter: AddWellAdapter
lateinit var intent: Intent
var well_id:Int?=null
lateinit var loaded_well_data:ArrayList<com.redsea.redsea.network.PostData.WellData>
var request_id:Int?=null
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
                Log.d("data tupe","$dattype")
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
        val dattype=mysharedpref(requireContext()).getDataType()
        when (dattype) {
            "operations" -> fetchopenop(it.id)
            "wellSurveys" -> fetchopensurv(it.id)
            "test" -> fetchopentest(it.id)
            "trouble"-> fetchopentrouble(it.id)
        }

    }

    private fun loadWellDraft(it: UserWellsItem) {
        binding.wellNameInputText.setText(it.name)
        val dattype=mysharedpref(requireContext()).getDataType()
        when (dattype) {
            "operations" -> fetchdraftop(it.id)
            "wellSurveys" -> fetchdraftsurv(it.id)
            "test" -> fetchdrafttest(it.id)
            "trouble"-> fetchdrafttrouble(it.id)
        }


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

            if (edittype == "editdraft") {
                val editdarft =
                    arguments?.getSerializable("draftitem") as UserWellsItem?
                well_id = editdarft?.id
                Log.d("id","$id")
                Log.d("name","${editdarft?.name}")
            } else if (edittype == "editrequest") {
                binding.saveDraftBtn.visibility=View.GONE
                binding.publishWellBtn.visibility=View.GONE
                binding.updateWellBtn.visibility=View.VISIBLE
                val openrequest =
                    arguments?.getSerializable("openrequest") as OpenRequestItem?
                well_id = openrequest?.well?.id
                request_id=openrequest?.id
                Log.d("request_id","$request_id")

                binding.updateWellBtn.setOnClickListener {
                    when (dattype) {
                        "operations" -> {
                            updatewell(well_id!!,"operations")
                        }

                        "wellSurveys" -> {
                            updatewell(well_id!!,"wellSurveys")

                        }

                        "test" -> {
                            updatewell(well_id!!,"test")
                        }

                        "trouble" -> {
                            updatewell(well_id!!,"trouble")
                        }
                    }

                }



            }
        }
        binding.publishWellBtn.setOnClickListener {
            if (edittype == "editdraft") {
                when (dattype) {
                    "operations" -> {
                        publishsavedDraft("operations")
                    }

                    "wellSurveys" -> {
                        publishsavedDraft("wellSurveys")

                    }

                    "test" -> {
                        publishsavedDraft("test")
                    }

                    "trouble" -> {
                        publishsavedDraft("trouble")
                    }
                }
            }
            else{
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

        }

        binding.saveDraftBtn.setOnClickListener {
            if (edittype=="editdraft"){
                when (dattype) {
                    "operations" -> {
                        updatesavedDraft("operations")
                    }

                    "wellSurveys" -> {
                        updatesavedDraft("wellSurveys")

                    }

                    "test" -> {
                        updatesavedDraft("test")
                    }

                    "trouble" -> {
                        updatesavedDraft("trouble")
                    }
                }

            }
            else{
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
                        call: Call<WellOptionsResponse>,
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
                    Callback<WellOptionsResponse> {
                    override fun onResponse(
                        call: Call<WellOptionsResponse>,
                        response: Response<WellOptionsResponse>
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
//                                Log.d(
//                                    "ChildData",
//                                    "start + survey + $optionPos + ${0} +${allList.size} + ${allList[1].size}"
//                                )
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
                        call: Call<WellOptionsResponse>,
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
                    Callback<WellOptionsResponse> {
                    override fun onResponse(
                        call: Call<WellOptionsResponse>,
                        response: Response<WellOptionsResponse>
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
                                    addWellAdapter =
                                        AddWellAdapter(wellOptionsResponse, this@AddWellFragment)
                                    binding.recyclerViewAddItem.adapter = addWellAdapter
//                                    Log.d(
//                                        "ChildData",
//                                        "start + set + $optionPos + ${0} +${allList.size} + ${allList[1].size}"
//                                    )
                                }

                            } else {
                                Toast.makeText(context, "Null", Toast.LENGTH_SHORT).show()

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
//                                Log.d(
//                                    "ChildData",
//                                    "start + trouble + $optionPos + ${0} +${allList.size} + ${allList[1].size}"
//                                )

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

    fun saveDraftopertion(publish: Publish) {
        binding.addWellProgressBar.visibility = View.VISIBLE
        RetrofitClient.instance.saveDraft("Bearer ${UserID.userAccessToken}",publish)
            .enqueue(object :
                Callback<SaveDraftResponse> {
                override fun onResponse(
                    call: Call<SaveDraftResponse>,
                    response: Response<SaveDraftResponse>
                ) {
                    if (response.isSuccessful) {
                        val wellOptionsResponse = response.body()
                        Log.d("SAVEDDRAFT", "$wellOptionsResponse")

                        Toast.makeText(context, "Draft Saved", Toast.LENGTH_SHORT).show()

                    } else {

                        Toast.makeText(context, "Empty Field Required", Toast.LENGTH_SHORT).show()
                        Log.d("response","${response.errorBody()}")

                    }
                    binding.addWellProgressBar.visibility = View.GONE
                }


                override fun onFailure(
                    call: Call<SaveDraftResponse>,
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
                Callback<SaveDraftResponse> {
                override fun onResponse(
                    call: Call<SaveDraftResponse>,
                    response: Response<SaveDraftResponse>
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
                    call: Call<SaveDraftResponse>,
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
        RetrofitClient.instance.saveDrafttest("Bearer ${UserID.userAccessToken}",publish)
            .enqueue(object :
                Callback<SaveDraftResponse> {
                override fun onResponse(
                    call: Call<SaveDraftResponse>,
                    response: Response<SaveDraftResponse>
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
                Callback<SaveDraftResponse> {
                override fun onResponse(
                    call: Call<SaveDraftResponse>,
                    response: Response<SaveDraftResponse>
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
                    call: Call<SaveDraftResponse>,
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
                        Toast.makeText(context, "empty field required", Toast.LENGTH_SHORT).show()

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

    fun publishtrouble(publish: Publish) {
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
// update saved draft
    fun updatesaveddraftop(id: Int,publish: Publish){
        binding.addWellProgressBar.visibility = View.VISIBLE
        RetrofitClient.instance.upsavedDraft("Bearer ${UserID.userAccessToken}",id,publish)
            .enqueue(object :
                Callback<SaveDraftResponse> {
                override fun onResponse(
                    call: Call<SaveDraftResponse>,
                    response: Response<SaveDraftResponse>
                ) {
                    if (response.isSuccessful) {
                        val wellOptionsResponse = response.body()
                        Log.d("SAVEDDRAFT", "$wellOptionsResponse")

                        Toast.makeText(context, "Draft updated", Toast.LENGTH_SHORT).show()

                    } else {

                        Toast.makeText(context, "Empty Field Required", Toast.LENGTH_SHORT).show()

                    }
                    binding.addWellProgressBar.visibility = View.GONE
                }


                override fun onFailure(
                    call: Call<SaveDraftResponse>,
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
    fun updatesaveddraftsurvey(id: Int,publish: Publish){
        binding.addWellProgressBar.visibility = View.VISIBLE
        RetrofitClient.instance.upsavedDraftsurvey("Bearer ${UserID.userAccessToken}",id,publish)
            .enqueue(object :
                Callback<SaveDraftResponse> {
                override fun onResponse(
                    call: Call<SaveDraftResponse>,
                    response: Response<SaveDraftResponse>
                ) {
                    if (response.isSuccessful) {
                        val wellOptionsResponse = response.body()
                        Log.d("SAVEDDRAFT", "$wellOptionsResponse")

                        Toast.makeText(context, "Draft updated", Toast.LENGTH_SHORT).show()

                    } else {

                        Toast.makeText(context, "Empty Field Required", Toast.LENGTH_SHORT).show()

                    }
                    binding.addWellProgressBar.visibility = View.GONE
                }


                override fun onFailure(
                    call: Call<SaveDraftResponse>,
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
    fun updatesaveddraftest(id: Int,publish: Publish){
        binding.addWellProgressBar.visibility = View.VISIBLE
        RetrofitClient.instance.upsavedDrafttest("Bearer ${UserID.userAccessToken}",id,publish)
            .enqueue(object :
                Callback<SaveDraftResponse> {
                override fun onResponse(
                    call: Call<SaveDraftResponse>,
                    response: Response<SaveDraftResponse>
                ) {
                    if (response.isSuccessful) {
                        val wellOptionsResponse = response.body()
                        Log.d("SAVEDDRAFT", "$wellOptionsResponse")

                        Toast.makeText(context, "Draft updated", Toast.LENGTH_SHORT).show()

                    } else {

                        Toast.makeText(context, "Empty Field Required", Toast.LENGTH_SHORT).show()

                    }
                    binding.addWellProgressBar.visibility = View.GONE
                }


                override fun onFailure(
                    call: Call<SaveDraftResponse>,
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
    fun updatesaveddraftrouble(id: Int,publish: Publish){
        binding.addWellProgressBar.visibility = View.VISIBLE
        RetrofitClient.instance.upsavedDrafttrouble("Bearer ${UserID.userAccessToken}",id,publish)
            .enqueue(object :
                Callback<SaveDraftResponse> {
                override fun onResponse(
                    call: Call<SaveDraftResponse>,
                    response: Response<SaveDraftResponse>
                ) {
                    if (response.isSuccessful) {
                        val wellOptionsResponse = response.body()
                        Log.d("SAVEDDRAFT", "$wellOptionsResponse")

                        Toast.makeText(context, "Draft updated", Toast.LENGTH_SHORT).show()

                    } else {

                        Toast.makeText(context, "Empty Field Required", Toast.LENGTH_SHORT).show()

                    }
                    binding.addWellProgressBar.visibility = View.GONE
                }


                override fun onFailure(
                    call: Call<SaveDraftResponse>,
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
    //publish saved draft
    fun publishsavedDraft(id: Int,publish: Publish) {
        binding.addWellProgressBar.visibility = View.VISIBLE
        RetrofitClient.instance.publishsavedWell("Bearer ${UserID.userAccessToken}",id,publish)
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
                        Toast.makeText(context, "empty field required", Toast.LENGTH_SHORT).show()

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
    fun publishsavedsurvey(id: Int,publish: Publish) {
        binding.addWellProgressBar.visibility = View.VISIBLE
        RetrofitClient.instance.publishsavedsurvey("Bearer ${UserID.userAccessToken}",id, publish)
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
                        Toast.makeText(context, "empty field required", Toast.LENGTH_SHORT).show()

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
    fun publishsavedtest(id: Int,publish: Publish) {
        binding.addWellProgressBar.visibility = View.VISIBLE
        RetrofitClient.instance.publishsavedtest("Bearer ${UserID.userAccessToken}",id, publish)
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
                        Toast.makeText(context, "empty field required", Toast.LENGTH_SHORT).show()

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
    fun publishsavedtrouble(id: Int,publish: Publish) {
        binding.addWellProgressBar.visibility = View.VISIBLE
        RetrofitClient.instance.publishsavedtrouble("Bearer ${UserID.userAccessToken}",id, publish)
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
                        Toast.makeText(context, "empty field required", Toast.LENGTH_SHORT).show()

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
                        Toast.makeText(context, "Empty Field Required", Toast.LENGTH_SHORT).show()
                    }
                    binding.addWellProgressBar.visibility = View.GONE

                }

                override fun onFailure(
                    call: Call<UpdateWellResponse>,
                    t: Throwable
                ) {
                    Toast.makeText(context, "Failed to fetch data: ${t.message}", Toast.LENGTH_LONG).show()
                    Log.d("Options Data", t.message.toString())
                    binding.addWellProgressBar.visibility = View.GONE
                }
            })

    }
    private fun updatewellsurvey(id: Int, publish: Publish) {
        RetrofitClient.instance.updatewellsurvey("Bearer ${UserID.userAccessToken}", id, publish)
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
    private fun updatewelltest(id: Int, publish: Publish) {
        RetrofitClient.instance.updatewelltest("Bearer ${UserID.userAccessToken}", id, publish)
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
    private fun updatewelltrouble(id: Int, publish: Publish) {
        RetrofitClient.instance.updatewelltrouble("Bearer ${UserID.userAccessToken}", id, publish)
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
    //fetch data for open request
    private fun fetchopenop(id: Int){
        RetrofitClient.instance.showrequest("Bearer ${UserID.userAccessToken}",id)
            .enqueue(object : Callback<ShowRequest> {
                override fun onResponse(call: Call<ShowRequest>, response: Response<ShowRequest>) {
                    if (response.isSuccessful){
                        loaded_well_data= response.body()?.well?.well_data!!
                    }
                }

                override fun onFailure(call: Call<ShowRequest>, t: Throwable) {
                    Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
                    Log.d("request Data", t.message.toString())
                }

            })
        Toast.makeText(context, "fetchin open operation", Toast.LENGTH_SHORT).show()

    }
    private fun fetchopensurv(id: Int){
        RetrofitClient.instance.showrequestsurvey("Bearer ${UserID.userAccessToken}",id)
            .enqueue(object : Callback<ShowRequest> {
                override fun onResponse(call: Call<ShowRequest>, response: Response<ShowRequest>) {
                    if (response.isSuccessful){
                        loaded_well_data= response.body()?.well?.well_data!!
                    }
                }

                override fun onFailure(call: Call<ShowRequest>, t: Throwable) {
                    Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
                    Log.d("request Data", t.message.toString())
                }

            })
        Toast.makeText(context, "fetchin open survey", Toast.LENGTH_SHORT).show()
    }
    private fun fetchopentest(id: Int){
        RetrofitClient.instance.showrequesttest("Bearer ${UserID.userAccessToken}",id)
            .enqueue(object : Callback<ShowRequest> {
                override fun onResponse(call: Call<ShowRequest>, response: Response<ShowRequest>) {
                    if (response.isSuccessful){
                        loaded_well_data= response.body()?.well?.well_data!!
                    }
                }

                override fun onFailure(call: Call<ShowRequest>, t: Throwable) {
                    Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
                    Log.d("request Data", t.message.toString())
                }

            })
        Toast.makeText(context, "fetchin open test", Toast.LENGTH_SHORT).show()
    }
    private fun fetchopentrouble(id: Int){
        RetrofitClient.instance.showrequestrouble("Bearer ${UserID.userAccessToken}",id)
            .enqueue(object : Callback<ShowRequest> {
                override fun onResponse(call: Call<ShowRequest>, response: Response<ShowRequest>) {
                    if (response.isSuccessful){
                        loaded_well_data= response.body()?.well?.well_data!!
                    }
                }

                override fun onFailure(call: Call<ShowRequest>, t: Throwable) {
                    Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
                    Log.d("request Data", t.message.toString())
                }

            })
    }

    //fetch draft
    private fun fetchdraftop(id: Int){
        Toast.makeText(context, "fetching draft opeartion", Toast.LENGTH_SHORT).show()
        RetrofitClient.instance.showdraftop("Bearer  ${UserID.userAccessToken}",id)
            .enqueue(object : Callback<UserWellData> {
                override fun onResponse(
                    call: Call<UserWellData>,
                    response: Response<UserWellData>
                ) {
                    if (response.isSuccessful){
                        loaded_well_data=response.body()?.well_data!!
                    }
                }

                override fun onFailure(call: Call<UserWellData>, t: Throwable) {
                    Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
                }

            })


    }
    private fun fetchdraftsurv(id: Int){
        Toast.makeText(context, "fetching draft survey", Toast.LENGTH_SHORT).show()
        RetrofitClient.instance.showdraftsurv("Bearer  ${UserID.userAccessToken}",id)
            .enqueue(object : Callback<UserWellData> {
                override fun onResponse(
                    call: Call<UserWellData>,
                    response: Response<UserWellData>
                ) {
                    if (response.isSuccessful){
                        loaded_well_data=response.body()?.well_data!!
                    }
                }

                override fun onFailure(call: Call<UserWellData>, t: Throwable) {
                    Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
                }

            })


    }
    private fun fetchdrafttest(id:Int){
        Toast.makeText(context, "fetching draft test", Toast.LENGTH_SHORT).show()
        RetrofitClient.instance.showdrafttest("Bearer  ${UserID.userAccessToken}",id)
            .enqueue(object : Callback<UserWellData> {
                override fun onResponse(
                    call: Call<UserWellData>,
                    response: Response<UserWellData>
                ) {
                    if (response.isSuccessful){
                        loaded_well_data=response.body()?.well_data!!
                    }
                }

                override fun onFailure(call: Call<UserWellData>, t: Throwable) {
                    Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
                }

            })

    }
    private fun fetchdrafttrouble(id: Int){
        Toast.makeText(context, "fetching draft trouble", Toast.LENGTH_SHORT).show()
        RetrofitClient.instance.showdrafttrouble("Bearer  ${UserID.userAccessToken}",id)
            .enqueue(object : Callback<UserWellData> {
                override fun onResponse(
                    call: Call<UserWellData>,
                    response: Response<UserWellData>
                ) {
                    if (response.isSuccessful){
                        loaded_well_data=response.body()?.well_data!!
                    }
                }

                override fun onFailure(call: Call<UserWellData>, t: Throwable) {
                    Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
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
        publish.from = "$fromYear-$fromMonth-$fromDay"
        publish.to = "$toYear-$toMonth-$toDay"
        validateSpinnersDate()
        Log.d("PublishData","PublishData + $publish")
        when(type){
            "operations" -> publishWell(publish)
            "wellSurveys" -> publishsurvey(publish)
            "test" -> publishtset(publish)
            "trouble" -> publishtrouble(publish)
        }
    }

    fun saveDraft(type: String) {
        val publish = getPublishData()
        publish.name = binding.wellNameInputText.text.toString()
        publish.from = "$fromYear-$fromMonth-$fromDay"
        publish.to = "$toYear-$toMonth-$toDay"
        validateSpinnersDate()
        Log.d("PublishData","PublishData + $publish")
        when(type){
            "operations" -> saveDraftopertion(publish)
            "wellSurveys" -> saveDraftsurvey(publish)
            "test" -> saveDrafttest(publish)
            "trouble" -> saveDrafttrouble(publish)
        }
    }
    fun updatesavedDraft(type: String) {
        val publish = getPublishData()
        publish.name = binding.wellNameInputText.text.toString()
        publish.from = "$fromYear-$fromMonth-$fromDay"
        publish.to = "$toYear-$toMonth-$toDay"
        validateSpinnersDate()
        Log.d("PublishData","PublishData + $publish")
        when(type){
            "operations" -> updatesaveddraftop(well_id!!,publish)
            "wellSurveys" -> updatesaveddraftsurvey(well_id!!,publish)
            "test" -> updatesaveddraftest(well_id!!,publish)
            "trouble" -> updatesaveddraftrouble(well_id!!,publish)
        }
    }
fun publishsavedDraft(type: String) {
    val publish = getPublishData()
    publish.name = binding.wellNameInputText.text.toString()
    publish.from = "$fromYear-$fromMonth-$fromDay"
    publish.to = "$toYear-$toMonth-$toDay"
    validateSpinnersDate()
    Log.d("PublishData","PublishData + $publish")
    when(type){
        "operations" -> publishsavedDraft(well_id!!,publish)
        "wellSurveys" -> publishsavedsurvey(well_id!!,publish)
        "test" -> publishsavedtest(well_id!!,publish)
        "trouble" -> publishsavedtrouble(well_id!!,publish)
    }
}

    fun updatewell(id: Int,type: String){
        val publish = getPublishData()
        publish.name = binding.wellNameInputText.text.toString()
        publish.from = "$fromYear-$fromMonth-$fromDay"
        publish.to = "$toYear-$toMonth-$toDay"

        when(type){
            "operations" -> updatewell(id,publish)
            "wellSurveys" -> updatewellsurvey(id,publish)
            "test" -> updatewelltest(id,publish)
            "trouble" -> updatewelltrouble(id,publish)
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

    override fun onDestroyView() {
        super.onDestroyView()
        iseditmode=false
    }
}





