package com.vanillax.televisionbingecalculator.app.kotlin.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.vanillax.televisionbingecalculator.app.kotlin.enum.SearchType
import com.vanillax.televisionbingecalculator.app.kotlin.network.TVBCLoggerService
import com.vanillax.televisionbingecalculator.app.kotlin.network.TheMovieDBService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by Nandeesh on 2019-07-07.
 */
class LandingActivityViewModelTest {

    @get:Rule
    val server: MockWebServer = MockWebServer()

    @get:Rule
    val instantTaskRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val okHttpClient = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build()
    private lateinit var movieDBService: TheMovieDBService
    private lateinit var tvbcLoggerService: TVBCLoggerService
    private lateinit var viewModel: LandingActivityViewModel

    @Before
    fun setUp() {

        movieDBService = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .baseUrl(server.url("/"))
                .build()
                .create(TheMovieDBService::class.java)

        tvbcLoggerService = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .baseUrl(server.url("/"))
                .build()
                .create(TVBCLoggerService::class.java)
    }



    @Test
    fun `test able to make search call inside viewmodel`() {
        //given
        viewModel = LandingActivityViewModel(movieDBService, tvbcLoggerService)

        server.enqueue(MockResponse().setBody(
                """
                    {
                        "results" : [
                            {"id": 1, "poster_path": "www.posterpath.com", "original_name": "MarvelAvengers", "original_title": "Avengers ultron", "vote_average": 4.3},
                            {"id": 2, "poster_path": "www.posterpath.com", "original_name": "MarvelAvengers", "original_title": "Avengers civil war", "vote_average": 4.1},
                            {"id": 3, "poster_path": "www.posterpath.com", "original_name": "MarvelAvengers", "original_title": "Avengers infinity", "vote_average": 4.2},
                            {"id": 4, "poster_path": "www.posterpath.com", "original_name": "MarvelAvengers", "original_title": "Avengers ending", "vote_average": 4.6}
                        ]
                    }
                """.trimIndent()
        ))

        //when
        viewModel.onGetSearchShow("Avengers")

        server.takeRequest().also { request ->
            assertThat(request.method).isEqualTo("GET")
            assertThat(request.path).contains("search/tv?")
        }

        viewModel.queryResponse.value!!.also {
            assertThat(it.searchType).isEqualTo(SearchType.TV)
            assertThat(it.showPosterListing.size).isEqualTo(4)
            assertThat(it.showPosterListing[0].movie_title).isEqualTo("Avengers ultron")
            assertThat(it.showPosterListing[3].movie_title).isEqualTo("Avengers ending")
        }

    }

    //Todo add more test cases

}