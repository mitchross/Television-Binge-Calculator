package com.vanillax.televisionbingecalculator.app.Kotlin.network

import android.content.Context
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.io.IOException

interface BaseService
{


     fun getOkHttpClient(context: Context): OkHttpClient {
        val SIZE_OF_CACHE = (10 * 1024 * 1024).toLong() // 10 MiB
        val cache = Cache(File(context.cacheDir, "http"), SIZE_OF_CACHE)

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY


        return OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(RewriteRequestInterceptor())
                .addInterceptor(httpLoggingInterceptor)
                .addNetworkInterceptor(RewriteResponseCacheControlInterceptor())
                .build()

    }

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

}
