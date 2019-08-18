package com.vanillax.televisionbingecalculator.app.kotlin.network

import android.content.Context
import com.vanillax.televisionbingecalculator.app.kotlin.network.response.JustWatchResponse
import io.reactivex.Single
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.io.File

interface JustWatchAPIService  {

    companion object {

        fun create( context: Context): JustWatchAPIService {



//            val interceptor = HttpLoggingInterceptor()
//            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
//            val httpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
//
            val SIZE_OF_CACHE = (10 * 1024 * 1024).toLong() // 10 MiB
            val cache = Cache(File(context.cacheDir, "http"), SIZE_OF_CACHE)
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            var httpClient = OkHttpClient.Builder()
                    .cache(cache)
                    .addInterceptor(RewriteRequestInterceptor())
                    .addInterceptor(httpLoggingInterceptor)
                    .addNetworkInterceptor(RewriteResponseCacheControlInterceptor())
                    .build()


            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .baseUrl("https://apis.justwatch.com/")
                    .build()
            return retrofit.create(JustWatchAPIService::class.java)
        }

    }

//
//    fun getOkHttpClient(context: Context): OkHttpClient {
//        val SIZE_OF_CACHE = (10 * 1024 * 1024).toLong() // 10 MiB
//        val cache = Cache(File(context.cacheDir, "http"), SIZE_OF_CACHE)
//
//        val httpLoggingInterceptor = HttpLoggingInterceptor()
//        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
//
//
//        return OkHttpClient.Builder()
//                .cache(cache)
//                .addInterceptor(RewriteRequestInterceptor())
//                .addInterceptor(httpLoggingInterceptor)
//                .addNetworkInterceptor(RewriteResponseCacheControlInterceptor())
//                .build()
//
//    }




    @Headers("content-type: application/json", "User-Agent: JustWatch Python client (github.com/dawoudt/JustWatchAPI)")
    @POST("/content/titles/en_US/popular")
    fun getMovieStreamingSources(@Body movieSearch: com.vanillax.televisionbingecalculator.app.kotlin.network.response.JustWatchSearch): Single<JustWatchResponse>

}