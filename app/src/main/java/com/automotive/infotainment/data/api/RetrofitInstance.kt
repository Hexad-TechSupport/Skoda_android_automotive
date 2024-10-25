package com.automotive.infotainment.data.api

import com.automotive.infotainment.utility.VhalConstant


import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS) // Increase connection timeout
            .readTimeout(30, TimeUnit.SECONDS) // Increase read timeout
            .writeTimeout(30, TimeUnit.SECONDS) // Increase write timeout
            .addInterceptor { chain: Interceptor.Chain ->
                var tryCount = 0
                var response: Response? = null
                while (tryCount < 3) {
                    try {
                        response = chain.proceed(chain.request())
                        break // Success, exit the loop
                    } catch (e: Exception) {
                        tryCount++
                        if (tryCount >= 3) {
                            throw e // Fail after 3 retries
                        }
                    }
                }
                response
            }
            .build()
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(VhalConstant.BASE_URL) // Add your base URL here
        .client(provideOkHttpClient()) // Use custom OkHttpClient
        .addConverterFactory(GsonConverterFactory.create()) // Convert JSON data automatically
        .build()

    val api: UserApi
        get() = retrofit.create(UserApi::class.java)
}