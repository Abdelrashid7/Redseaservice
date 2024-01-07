package com.example.redsea.service.ui.fragments
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.redsea.R
import com.example.redsea.network.Response.Wellpdf.WellPdfResponse
import com.example.redsea.network.retrofit.RetrofitClient
import com.example.redsea.service.ui.TitleInterface
import com.example.redsea.service.ui.UserID
import com.example.redsea.service.ui.activity.mysharedpref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowPdfFragment:Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_show_pdf,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dattype= mysharedpref(requireContext()).getDataType()
        Log.d("datatyp","$dattype")
        (activity as? TitleInterface)?.onTextChange("Well Pdf", "Well Pdf")

        val clickeditemid = arguments?.getInt("wellItemId")

        if (clickeditemid != null) {

            when (dattype) {
                "operations" -> {
                    RetrofitClient.instance.Wellpdf("Bearer ${UserID.userAccessToken}", clickeditemid)
                        .enqueue(object : Callback<WellPdfResponse> {
                            override fun onResponse(
                                call: Call<WellPdfResponse>,
                                response: Response<WellPdfResponse>
                            ) {
                                if (response.isSuccessful) {
                                    val pdfurl = response.body()?.url
                                    val pdfprogress: ProgressBar = view.findViewById(R.id.pdfProgress)
                                    val webview: WebView = view.findViewById(R.id.webView)
                                    webview.webViewClient = WebViewClient()
                                    val webSettings: WebSettings = webview.settings
                                    webSettings.javaScriptEnabled = true
                                    webSettings.loadWithOverviewMode=true
                                    webview.webChromeClient = object : WebChromeClient() {
                                        override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                            super.onProgressChanged(view, newProgress)
                                            pdfprogress.progress = newProgress
                                        }
                                    }

                                    webview.webViewClient = object : WebViewClient() {
                                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                            super.onPageStarted(view, url, favicon)
                                            pdfprogress.visibility = View.VISIBLE
                                        }

                                        override fun onPageFinished(view: WebView?, url: String?) {
                                            super.onPageFinished(view, url)
                                            pdfprogress.visibility = View.GONE
                                        }
                                    }

                                    webview.loadUrl("$pdfurl")

                                } else {
                                    Toast.makeText(context, "PDF NOT FOUND", Toast.LENGTH_SHORT).show()
                                }
                            }


                            override fun onFailure(call: Call<WellPdfResponse>, t: Throwable) {
                                Toast.makeText(context, "Why pdf fail: ${t.message}", Toast.LENGTH_LONG)
                                    .show()
                                Log.d("Pdf url data", t.message.toString())
                            }

                        })

                }
                "wellSurveys" -> {
                    RetrofitClient.instance.Wellpdfsurvey("Bearer ${UserID.userAccessToken}", clickeditemid)
                        .enqueue(object : Callback<WellPdfResponse> {
                            override fun onResponse(
                                call: Call<WellPdfResponse>,
                                response: Response<WellPdfResponse>
                            ) {
                                if (response.isSuccessful) {
                                    val pdfurl = response.body()?.url
                                    val pdfprogress: ProgressBar = view.findViewById(R.id.pdfProgress)
                                    val webview: WebView = view.findViewById(R.id.webView)
                                    webview.webViewClient = WebViewClient()
                                    val webSettings: WebSettings = webview.settings
                                    webSettings.javaScriptEnabled = true
                                    webSettings.loadWithOverviewMode=true
                                    webview.webChromeClient = object : WebChromeClient() {
                                        override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                            super.onProgressChanged(view, newProgress)
                                            pdfprogress.progress = newProgress
                                        }
                                    }

                                    webview.webViewClient = object : WebViewClient() {
                                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                            super.onPageStarted(view, url, favicon)
                                            pdfprogress.visibility = View.VISIBLE
                                        }

                                        override fun onPageFinished(view: WebView?, url: String?) {
                                            super.onPageFinished(view, url)
                                            pdfprogress.visibility = View.GONE
                                        }
                                    }

                                    webview.loadUrl("$pdfurl")

                                } else {
                                    Toast.makeText(context, "PDF NOT FOUND", Toast.LENGTH_SHORT).show()
                                }
                            }


                            override fun onFailure(call: Call<WellPdfResponse>, t: Throwable) {
                                Toast.makeText(context, "Why pdf fail: ${t.message}", Toast.LENGTH_LONG)
                                    .show()
                                Log.d("Pdf url data", t.message.toString())
                            }

                        })
                }
                "test" -> {
                    RetrofitClient.instance.Wellpdftest("Bearer ${UserID.userAccessToken}", clickeditemid)
                        .enqueue(object : Callback<WellPdfResponse> {
                            override fun onResponse(
                                call: Call<WellPdfResponse>,
                                response: Response<WellPdfResponse>
                            ) {
                                if (response.isSuccessful) {
                                    val pdfurl = response.body()?.url
                                    val pdfprogress: ProgressBar = view.findViewById(R.id.pdfProgress)
                                    val webview: WebView = view.findViewById(R.id.webView)
                                    webview.webViewClient = WebViewClient()
                                    val webSettings: WebSettings = webview.settings
                                    webSettings.javaScriptEnabled = true
                                    webSettings.loadWithOverviewMode=true
                                    webview.webChromeClient = object : WebChromeClient() {
                                        override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                            super.onProgressChanged(view, newProgress)
                                            pdfprogress.progress = newProgress
                                        }
                                    }

                                    webview.webViewClient = object : WebViewClient() {
                                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                            super.onPageStarted(view, url, favicon)
                                            pdfprogress.visibility = View.VISIBLE
                                        }

                                        override fun onPageFinished(view: WebView?, url: String?) {
                                            super.onPageFinished(view, url)
                                            pdfprogress.visibility = View.GONE
                                        }
                                    }

                                    webview.loadUrl("$pdfurl")

                                } else {
                                    Toast.makeText(context, "PDF NOT FOUND", Toast.LENGTH_SHORT).show()
                                }
                            }


                            override fun onFailure(call: Call<WellPdfResponse>, t: Throwable) {
                                Toast.makeText(context, "Why pdf fail: ${t.message}", Toast.LENGTH_LONG)
                                    .show()
                                Log.d("Pdf url data", t.message.toString())
                            }

                        })
                }
                "trouble" -> {
                    RetrofitClient.instance.Wellpdftrouble("Bearer ${UserID.userAccessToken}", clickeditemid)
                        .enqueue(object : Callback<WellPdfResponse> {
                            override fun onResponse(
                                call: Call<WellPdfResponse>,
                                response: Response<WellPdfResponse>
                            ) {
                                if (response.isSuccessful) {
                                    val pdfurl = response.body()?.url
                                    val pdfprogress: ProgressBar = view.findViewById(R.id.pdfProgress)
                                    val webview: WebView = view.findViewById(R.id.webView)
                                    webview.webViewClient = WebViewClient()
                                    val webSettings: WebSettings = webview.settings
                                    webSettings.javaScriptEnabled = true
                                    webSettings.loadWithOverviewMode=true
                                    webview.webChromeClient = object : WebChromeClient() {
                                        override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                            super.onProgressChanged(view, newProgress)
                                            pdfprogress.progress = newProgress
                                        }
                                    }

                                    webview.webViewClient = object : WebViewClient() {
                                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                            super.onPageStarted(view, url, favicon)
                                            pdfprogress.visibility = View.VISIBLE
                                        }

                                        override fun onPageFinished(view: WebView?, url: String?) {
                                            super.onPageFinished(view, url)
                                            pdfprogress.visibility = View.GONE
                                        }
                                    }

                                    webview.loadUrl("$pdfurl")

                                } else {
                                    Toast.makeText(context, "PDF NOT FOUND", Toast.LENGTH_SHORT).show()
                                }
                            }


                            override fun onFailure(call: Call<WellPdfResponse>, t: Throwable) {
                                Toast.makeText(context, "Why pdf fail: ${t.message}", Toast.LENGTH_LONG)
                                    .show()
                                Log.d("Pdf url data", t.message.toString())
                            }

                        })
                }
            }




        }
    }


}