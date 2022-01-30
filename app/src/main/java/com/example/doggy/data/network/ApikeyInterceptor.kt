package com.example.doggy.data.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class ApikeyInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()

        val newRequest: Request = request.newBuilder()
            .addHeader("x-api-key", "83c1fc60-58c8-42ed-81bc-1fad825f7bd8")
            .build()
        return chain.proceed(newRequest)
    }
}