package com.example.redsea.service.ui.fragments
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.get
import androidx.fragment.app.Fragment
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import androidx.lifecycle.MutableLiveData
import com.example.redsea.network.Response.PublishWellResponse.PublishWellResponse
import com.example.redsea.network.ViewWellsResponse.ViewWellsItem
import com.example.redsea.service.ui.activity.SearchActivity
import com.example.redsea.service.ui.activity.mysharedpref
import com.example.redsea.ui.activity.MainActivity
import com.example.redsea.ui.fragments.MainCategoryFragment
import com.example.redsea.ui.fragments.OperationsFragment
import okhttp3.internal.format
import java.time.Clock
import java.time.LocalDateTime
import java.time.Year

lateinit var addWellAdapter: AddWellAdapter
lateinit var intent: Intent
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
                binding.addWellProgressBar.visibility = View.VISIBLE
                val dattype= mysharedpref(requireContext()).getDataType()
                when (dattype) {
                    "operations" -> fetchOptionsoperations(requireContext())
                    "wellSurveys" -> fetchOptionssurvey()
                    "test" -> fetchOptionstest()
                    "trouble" -> fetchOptionstrouble()
                }
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
        val dattype= mysharedpref(requireContext()).getDataType()


        when (dattype) {
            "operations" -> fetchOptionsoperations(requireContext())
            "wellSurveys" -> fetchOptionssurvey()
            "test" -> fetchOptionstest()
            "trouble" -> fetchOptionstrouble()
        }

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
            validateSpinnersDate()
            Log.d("WHOLESAVE", saveDraft.toString())
            publishWell(saveDraft)

        }
        binding.saveDraftBtn.setOnClickListener {
            val saveDraft = addWellAdapter.adapter.enteredList()
            saveDraft.name = binding.wellNameInputText.text.toString()
            saveDraft.to = "$toYear-$toMonth-$toDay"
            saveDraft.from = "$fromYear-$fromMonth-$fromDay"
            validateSpinnersDate()
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






    private fun fetchOptionsoperations (context: Context) {
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
    private fun fetchOptionssurvey(){
        Toast.makeText(context, "waiting survey options", Toast.LENGTH_SHORT).show()
    }
    private fun fetchOptionstest(){
        Toast.makeText(context, "waiting test options", Toast.LENGTH_SHORT).show()
    }
    private fun fetchOptionstrouble(){
        Toast.makeText(context, "waiting trouble options", Toast.LENGTH_SHORT).show()
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
            .enqueue(object : Callback<PublishWellResponse> {
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


                override fun onFailure(call: Call<PublishWellResponse>, t: Throwable) {
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

    private fun validateSpinnersDate(){
        val todayCalendar = Calendar.getInstance()
        val fromDatespinner = Calendar.getInstance().apply {
            set(Calendar.YEAR, fromYear)
            set(Calendar.MONTH, fromMonth-1)
            set(Calendar.DAY_OF_MONTH, fromDay)
        }
        if (fromDatespinner.before(todayCalendar)) {
            Toast.makeText(context, "from date starts from today's date",
                Toast.LENGTH_SHORT).show()

        }
        val toDatespinner = Calendar.getInstance().apply {
            set(Calendar.YEAR, toYear)
            set(Calendar.MONTH, toMonth-1)
            set(Calendar.DAY_OF_MONTH, toDay)
        }

        if (toDatespinner.before(fromDatespinner)) {
            Toast.makeText(context, "to date can't be set before from date",
                Toast.LENGTH_SHORT).show()

        }

    }
}


