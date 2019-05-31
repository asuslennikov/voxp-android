package ru.voxp.android.data

import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response

const val HOST_PLACEHOLDER: String = "http://placeholder.me"

class HostInterceptor : Interceptor {
    override fun intercept(chain: Chain): Response {
        val request = chain.request()
        return chain.proceed(
            request.newBuilder()
                .url(request.url().toString().replace(HOST_PLACEHOLDER, "http://voxp.ru", true))
                .build()
        )
    }
}