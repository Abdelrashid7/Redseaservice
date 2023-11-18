package com.example.redsea.service.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.redsea.databinding.FragmentAddWellBinding
import com.example.redsea.network.PostData.Publish
import com.example.redsea.network.PostData.Text
import com.example.redsea.network.Response.SaveDraft.SaveDraftResponse
import com.example.redsea.network.Response.UserWells.UserWellsItem
import com.example.redsea.network.Response.WellOptions.WellOptionsResponse
import com.example.redsea.network.Response.WellOptions.WellOptionsResponseItem
import com.example.redsea.network.retrofit.RetrofitClient
import com.example.redsea.service.ui.TitleInterface
import com.example.redsea.service.ui.UserID
import com.example.redsea.service.ui.adapters.adapters.AddWellAdapter
import okhttp3.internal.notify
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

lateinit var addWellAdapter: AddWellAdapter
val refresh = MutableLiveData(false)
var optionsFetched = false
private lateinit var viewModel: UserWellsItem


var toDay: Int = 0
var toMonth: Int = 0
var toYear: Int = 0
var fromDay: Int = 0
var fromMonth: Int = 0
var fromYear: Int = 0

class AddWellFragment : Fragment() {
    lateinit var binding: FragmentAddWellBinding
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
                fetchOptions(requireContext())
            }
        } catch (e: Exception) {
            Log.d("CRASHCAUSE", e.message.toString())
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? TitleInterface)?.onTextChange("Add new Report", "Add Report")


        initSpinner()
        try {

        } catch (e: Exception) {
            Log.d("LINEAR LAYOUT CRASH", e.message.toString())
        }
        try {

            binding.recyclerViewAddItem.adapter = addWellAdapter
        } catch (e: Exception) {
            Log.d("WHYCRASH", e.message.toString())
        }
        Log.d("REFRESH WORK", " BINDING")
        binding.publishWellBtn.setOnClickListener {
            Log.d("PRINTINPUT", addWellAdapter.adapter.enteredList().well_data.toString())
            val saveDraft = addWellAdapter.adapter.enteredList()
            saveDraft.name = binding.wellNameInputText.text.toString()
            saveDraft.to = "$toYear-$toMonth-$toDay"
            saveDraft.from = "$fromYear-$fromMonth-$fromDay"
            Log.d("WHOLESAVE", saveDraft.toString())
            publishWell(saveDraft)

        }
        binding.saveDraftBtn.setOnClickListener {
            val saveDraft = addWellAdapter.adapter.enteredList()
            saveDraft.name = binding.wellNameInputText.text.toString()
            saveDraft.to = "$toYear-$toMonth-$toDay"
            saveDraft.from = "$fromYear-$fromMonth-$fromDay"
            Log.d("WHOLESAVE", saveDraft.toString())
            saveDraft(saveDraft)


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
            2023, 2022, 2021,
            2020, 2019, 2018,
            2017, 2016, 2015,
            2014, 2013
        )
        val dayAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, days)
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        val monthAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, month)
        val yearAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, year)

        binding.fromDaySpinner.apply {
            adapter = dayAdapter
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
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    fromMonth = position+1
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }
        }
        binding.fromYearSpinner.apply {
            adapter = yearAdapter
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
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    toMonth = position +1
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }
        }
        binding.toYearSpinner.apply {
            adapter = yearAdapter
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
        //recieving data from edit button
        val wellName = arguments?.getString("Wellname")
        val wellFrom = arguments?.getString("wellFrom")
        val wellTo = arguments?.getString("wellTo")
        //fun to get spinner values
         fun setSpinnerValues(daySpinner: Spinner, monthSpinner: Spinner, yearSpinner: Spinner, date: String?) {
            date?.let {
                val calendar = Calendar.getInstance()
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                calendar.time = dateFormat.parse(it) ?: Date()

                // Set spinner selections based on the date
                daySpinner.setSelection(calendar.get(Calendar.DAY_OF_MONTH) - 1)
                monthSpinner.setSelection(calendar.get(Calendar.MONTH))
                yearSpinner.setSelection(yearAdapter.getPosition(calendar.get(Calendar.YEAR)))
            }
        }
        //update data
        binding.wellNameInputText.setText(wellName)
        setSpinnerValues(binding.fromDaySpinner, binding.fromMonthSpinner, binding.fromYearSpinner, wellFrom)
        setSpinnerValues(binding.toDaySpinner, binding.toMonthSpinner, binding.toYearSpinner, wellTo)

    }




    private fun fetchOptions(context: Context) {
        Log.d("HelloRetrofit", "no connection")
        try {
            binding.addWellProgressBar.visibility = View.VISIBLE

            RetrofitClient.instance.getWells("Bearer ${UserID.userAccessToken}")
                .enqueue(object : Callback<WellOptionsResponse> {
                    override fun onResponse(
                        call: Call<WellOptionsResponse>,
                        response: Response<WellOptionsResponse>
                    ) {
                        if (response.isSuccessful) {
                            val wellOptionsResponse = response.body()
                            if (wellOptionsResponse != null) {
                                addWellAdapter = AddWellAdapter(wellOptionsResponse)
                                binding.recyclerViewAddItem.adapter = addWellAdapter
                                optionsFetched = true
                                refresh.value = true
                                addWellAdapter.notifyItemRangeInserted(0,wellOptionsResponse.size)

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

                    override fun onFailure(call: Call<WellOptionsResponse>, t: Throwable) {
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
            .enqueue(object : Callback<SaveDraftResponse> {
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


                override fun onFailure(call: Call<SaveDraftResponse>, t: Throwable) {
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
            .enqueue(object : Callback<SaveDraftResponse> {
                override fun onResponse(
                    call: Call<SaveDraftResponse>,
                    response: Response<SaveDraftResponse>
                ) {
                    if (response.isSuccessful) {
                        val wellOptionsResponse = response.body()
                        Log.d("SAVEDDRAFT", wellOptionsResponse.toString())
                        Toast.makeText(context, "Well Published", Toast.LENGTH_SHORT).show()

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


                override fun onFailure(call: Call<SaveDraftResponse>, t: Throwable) {
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

}