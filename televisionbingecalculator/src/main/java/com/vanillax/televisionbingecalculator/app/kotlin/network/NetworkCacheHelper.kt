package com.vanillax.televisionbingecalculator.app.kotlin.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class RewriteRequestInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val maxStale = 60 * 60 * 24 * 5
        val request: Request
        request = chain.request().newBuilder().header("Cache-Control", "max-stale=$maxStale").build()
        return chain.proceed(request)
    }
}

class RewriteResponseCacheControlInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val maxStale = 60 * 60 * 24 * 5
        val originalResponse = chain.proceed(chain.request())
        return originalResponse.newBuilder().header("Cache-Control", "public, max-age=120, max-stale=$maxStale").build()
    }
}

