package com.vanillax.televisionbingecalculator.app.kotlin.network

import com.vanillax.televisionbingecalculator.app.serverapi.TVBCLogger.EmptyResponse
import com.vanillax.televisionbingecalculator.app.serverapi.TVBCLogger.SearchTerm
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


/**
 * Created by mitchross on 6/7/15.
 */
interface TVBCLoggerService {

    companion object {

        fun create(): TVBCLoggerService {

            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val httpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .baseUrl("https://tvbc-logger.herokuapp.com/api/")
                    .build()
            return retrofit.create(TVBCLoggerService::class.java)
        }
    }


    @Headers("Content-type: application/json")
    @POST("searchterms")
    fun postSearchTerm(@Body searchTerm: SearchTerm): io.reactivex.Observable<EmptyResponse>
}
