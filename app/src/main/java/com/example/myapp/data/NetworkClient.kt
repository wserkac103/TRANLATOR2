package com.example.myapp.data

import okhttp3.OkHttpClient
import retrofit2.Retrofit

object NetworkClient {
    private val okHttpClient: OkHttpClient = OkHttpClient.Builder().build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://example.com/")
        .client(okHttpClient)
        .build()
}
