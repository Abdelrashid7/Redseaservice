package com.redsea.redsea.network.retrofit

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
            }.addInterceptor(com.redsea.redsea.network.retrofit.RetrofitClient.logger).build()

        private val okHttp = OkHttpClient.Builder().addInterceptor(com.redsea.redsea.network.retrofit.RetrofitClient.logger)

        val instance: com.redsea.redsea.network.api.api by lazy {
            val retrofit = Retrofit.Builder(    )
                .baseUrl(com.redsea.redsea.network.retrofit.RetrofitClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                .client(com.redsea.redsea.network.retrofit.RetrofitClient.okHttpClient)
                .build()


            retrofit.create(com.redsea.redsea.network.api.api::class.java)
        }
    }
