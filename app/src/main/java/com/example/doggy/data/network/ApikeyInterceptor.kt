package com.example.doggy.data.network

import com.example.doggy.BuildConfig
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class ApikeyInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()

        val newRequest: Request = request.newBuilder()
            .addHeader("x-api-key", BuildConfig.THE_DOG_API_KEY)
            .build()
        return chain.proceed(newRequest)
    }
}