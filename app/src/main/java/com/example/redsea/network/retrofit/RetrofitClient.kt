package com.example.redsea.network.retrofit

import com.example.redsea.network.api.api
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

    object RetrofitClient {
        private const val BASE_URL = "https://redseaoil.xyz/"

        private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        private val okHttpClient = OkHttpClient
            .Builder().hostnameVerifier { _, _ -> true }
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original
                    .newBuilder()
                    .method(original.method, original.body)

                val request = requestBuilder.build()
                chain.proceed(request)
            }.addInterceptor(logger).build()

        private val okHttp = OkHttpClient.Builder().addInterceptor(logger)

        val instance: api by lazy {
            val retrofit = Retrofit.Builder(    )
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                .client(okHttpClient)
                .build()


            retrofit.create(api::class.java)
        }
    }
