package com.vanillax.televisionbingecalculator.app.Kotlin.network

import com.vanillax.televisionbingecalculator.app.Kotlin.network.response.JustWatchResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface JustWatchAPIService {

    companion object {

        fun create(): JustWatchAPIService {

            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val httpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .baseUrl("https://api.justwatch.com/")
                    .build()
            return retrofit.create(JustWatchAPIService::class.java)
        }
    }

    @Headers("content-type: application/json", "User-Agent: JustWatch Python client (github.com/dawoudt/JustWatchAPI)")
    @POST("titles/en_US/popular")
    fun getMovieStreamingSources(@Body movieSearch: com.vanillax.televisionbingecalculator.app.Kotlin.network.response.JustWatchSearch): io.reactivex.Observable<JustWatchResponse>

}