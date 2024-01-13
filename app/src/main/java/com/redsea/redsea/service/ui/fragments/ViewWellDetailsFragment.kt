package com.example.redsea.service.ui.fragments

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.redsea.redsea.R
import com.redsea.redsea.databinding.FragmentViewWellDetailsBinding
import com.redsea.redsea.network.Response.Wellpdf.WellPdfResponse
import com.redsea.redsea.network.ViewWellsResponse.ViewWellsItem
import com.redsea.redsea.network.retrofit.RetrofitClient
import com.redsea.redsea.service.ui.TitleInterface
import com.redsea.redsea.service.ui.UserID
import com.redsea.redsea.service.ui.activity.mysharedpref
import com.redsea.redsea.service.ui.fragments.EditRequestFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class ViewWellDetailsFragment : Fragment() {
    lateinit var binding: FragmentViewWellDetailsBinding
    var pdfurl:String?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewWellDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? TitleInterface)?.onTextChange("Well Details", "Well Details")
        val wellItem = arguments?.getSerializable("wellItem") as? ViewWellsItem
        (activity as? TitleInterface)?.onTextChange("View", getString(R.string.view_toolbar))

        binding.webView.settings.loadWithOverviewMode=true
        binding.webView.settings.javaScriptEnabled=true
        binding.webView.webViewClient =object :WebViewClient(){
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.pdfProgress.visibility=View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.pdfProgress.visibility=View.GONE
            }
        }


        val dattype= mysharedpref(requireContext()).getDataType()
        when (dattype) {
            "operations" -> {
                binding.pdfProgress.visibility=View.VISIBLE
                RetrofitClient.instance.Wellpdf("Bearer ${UserID.userAccessToken}", wellItem?.id!!)
                    .enqueue(object : Callback<WellPdfResponse> {
                        override fun onResponse(
                            call: Call<WellPdfResponse>,
                            response: Response<WellPdfResponse>
                        ) {
                            if (response.isSuccessful) {
                                pdfurl = response.body()?.url ?: ""
                                binding.webView.loadUrl("$pdfurl")

                            }
                            else {
                                Toast.makeText(context, "PDF NOT FOUND", Toast.LENGTH_SHORT).show()
                            }
                            binding.pdfProgress.visibility=View.GONE
                        }



                        override fun onFailure(call: Call<WellPdfResponse>, t: Throwable) {
                            Toast.makeText(context, "Why pdf fail: ${t.message}", Toast.LENGTH_LONG)
                                .show()
                            Log.d("Pdf url data", t.message.toString())
                        }

                    })



            }
            "wellSurveys" -> {
                binding.pdfProgress.visibility=View.VISIBLE
                RetrofitClient.instance.Wellpdfsurvey("Bearer ${UserID.userAccessToken}", wellItem?.id!!)
                    .enqueue(object : Callback<WellPdfResponse> {
                        override fun onResponse(
                            call: Call<WellPdfResponse>,
                            response: Response<WellPdfResponse>
                        ) {
                            if (response.isSuccessful) {
                                pdfurl = response.body()?.url ?: ""
                                binding.webView.loadUrl("$pdfurl")

                            } else {
                                Toast.makeText(context, "PDF NOT FOUND", Toast.LENGTH_SHORT).show()
                            }
                            binding.pdfProgress.visibility=View.GONE
                        }


                        override fun onFailure(call: Call<WellPdfResponse>, t: Throwable) {
                            Toast.makeText(context, "Why pdf fail: ${t.message}", Toast.LENGTH_LONG)
                                .show()
                            Log.d("Pdf url data", t.message.toString())
                        }

                    })
            }
            "test" -> {
                binding.pdfProgress.visibility=View.VISIBLE
                RetrofitClient.instance.Wellpdftest("Bearer ${UserID.userAccessToken}", wellItem?.id!!)
                    .enqueue(object : Callback<WellPdfResponse> {
                        override fun onResponse(
                            call: Call<WellPdfResponse>,
                            response: Response<WellPdfResponse>
                        ) {
                            if (response.isSuccessful) {
                                pdfurl = response.body()?.url ?: ""
                                binding.webView.loadUrl("$pdfurl")

                            } else {
                                Toast.makeText(context, "PDF NOT FOUND", Toast.LENGTH_SHORT).show()
                            }
                            binding.pdfProgress.visibility=View.GONE
                        }


                        override fun onFailure(call: Call<WellPdfResponse>, t: Throwable) {
                            Toast.makeText(context, "Why pdf fail: ${t.message}", Toast.LENGTH_LONG)
                                .show()
                            Log.d("Pdf url data", t.message.toString())
                        }

                    })
            }
            "trouble" -> {
                binding.pdfProgress.visibility=View.VISIBLE
                RetrofitClient.instance.Wellpdftrouble("Bearer ${UserID.userAccessToken}", wellItem?.id!!)
                    .enqueue(object : Callback<WellPdfResponse> {
                        override fun onResponse(
                            call: Call<WellPdfResponse>,
                            response: Response<WellPdfResponse>
                        ) {
                            if (response.isSuccessful) {
                                pdfurl = response.body()?.url ?: ""

                                binding.webView.loadUrl("$pdfurl")

                            } else {
                                Toast.makeText(context, "PDF NOT FOUND", Toast.LENGTH_SHORT).show()
                            }
                            binding.pdfProgress.visibility=View.GONE
                        }


                        override fun onFailure(call: Call<WellPdfResponse>, t: Throwable) {
                            Toast.makeText(context, "Why pdf fail: ${t.message}", Toast.LENGTH_LONG)
                                .show()
                            Log.d("Pdf url data", t.message.toString())
                        }

                    })
            }
        }
        Log.d("HELLODATA", wellItem?.name.toString())
        Log.d("USERCHECK", UserID.userId.toString())
        Log.d("USERCHECK", UserID.userAccessToken.toString())
        if(wellItem?.user?.id != UserID.userId)
        {
            binding.editRequestBtn.visibility = View.GONE
        }
        else {
            binding.editRequestBtn.setOnClickListener {

                val editRequestFragment = EditRequestFragment()
                val bundle = Bundle()
                bundle.putSerializable("wellItemRequest", wellItem)
                editRequestFragment.arguments = bundle

                val transaction: FragmentTransaction? = fragmentManager?.beginTransaction()
                transaction?.replace(R.id.fragmentContainer, editRequestFragment)
                transaction?.addToBackStack(null)
                transaction?.commit()
            }

        }
        binding.printWellBtn.setOnClickListener {
//            downloadpdf(wellItem!!.name)
            openpdfbrowser(pdfurl!!)




        }




    }
    fun openpdfbrowser(pdfurl:String?){
        val intent= Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(pdfurl))
        startActivity(intent)

    }
    fun downloadpdf(name: String){
        val request = DownloadManager.Request(Uri.parse(pdfurl))
            .setTitle("${name} pdf")
            .setDescription("Downloading PDF")
            .setMimeType("application/pdf")
            .setAllowedOverMetered(true)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "${name}.pdf")
        val downloadManager = requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
        Toast.makeText(context, "PDF download started", Toast.LENGTH_SHORT).show()
        Log.d("pdfurl","$pdfurl")

    }
    fun convertWebViewToPdf(webView: WebView) {
        val imageList = mutableListOf<Bitmap>()

        //capturing screen shot
            val bitmap = Bitmap.createBitmap(webView.width, webView.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            webView.draw(canvas)
            imageList.add(bitmap)
        //----


        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(webView.width, webView.height, 1).create()

        for (bitmap in imageList) {
            val page = document.startPage(pageInfo)
            val canvas = page.canvas
            canvas.drawBitmap(bitmap, 0f, 0f, null)
            document.finishPage(page)
        }

        val outputFile = File(requireActivity().getExternalFilesDir(null), "output.pdf")
        val fileOutputStream = FileOutputStream(outputFile)
        document.writeTo(fileOutputStream)
        document.close()
        fileOutputStream.close()
    }



}




