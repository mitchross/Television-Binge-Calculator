package com.vanillax.televisionbingecalculator.app.Kotlin.network

import com.vanillax.televisionbingecalculator.app.Dagger.TBCModule
import com.vanillax.televisionbingecalculator.app.Kotlin.network.response.CastResponse
import com.vanillax.televisionbingecalculator.app.Kotlin.network.response.QueryResponse
import com.vanillax.televisionbingecalculator.app.Kotlin.network.response.TVShowByIdResponse
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by mitchross on 4/14/18.
 */
interface TheMovieDBService {

    companion object {

        fun create(): TheMovieDBService {

            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val httpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .baseUrl("https://api.themoviedb.org/3/")
                    .build()
            return retrofit.create(TheMovieDBService::class.java)
        }
    }

    @GET("search/tv?" + "api_key=" + TBCModule.API_KEY)
     fun queryTV(@Query("query") show: String): Observable<QueryResponse>

    @GET("tv/{showId}" + "?api_key=" + TBCModule.API_KEY)
     fun queryTVDetails(@Path("showId") showId: String): Observable<TVShowByIdResponse>

    @GET("{searchType}/{showId}/credits" + "?api_key=" + TBCModule.API_KEY)
     fun queryCast(@Path("showId") showId: String, @Path("searchType") searchType: String): Observable<CastResponse>

    @GET("search/movie?" + "api_key=" + TBCModule.API_KEY)
     fun queryMovie(@Query("query") show: String): Observable<QueryResponse>

    @GET("movie/{showId}" + "?api_key=" + TBCModule.API_KEY)
     fun queryMovieDetails(@Path("showId") showId: String): Observable<TVShowByIdResponse>

    @GET("movie/{showId}/credits" + "?api_key=" + TBCModule.API_KEY)
     fun queryMovieCast(@Path("showId") showId: String): Observable<CastResponse>
}