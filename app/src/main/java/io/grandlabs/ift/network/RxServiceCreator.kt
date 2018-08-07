package io.grandlabs.ift.network

import com.google.gson.GsonBuilder
import io.grandlabs.ift.BuildConfig
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RxServiceCreator {

    private const val defaultTimeout = 30

    private val httpClient = OkHttpClient.Builder()
            .connectTimeout(defaultTimeout.toLong(), TimeUnit.SECONDS)
            .readTimeout(defaultTimeout.toLong(), TimeUnit.SECONDS)
            .writeTimeout(defaultTimeout.toLong(), TimeUnit.SECONDS)
            .also {
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.level = HttpLoggingInterceptor.Level.BODY
                    it.addInterceptor(logging)
                }
            }

    private val gson = GsonBuilder().create()
    private val rxAdapter = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())

    private const val apiUrl = "https://api-dev.ift-aft.org/app/"
    private val builder = Retrofit.Builder()
            .baseUrl(apiUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(rxAdapter)

    fun <RESTService> createService(service: Class<RESTService>): RESTService {
        val retrofit = builder.client(httpClient.build()).build()
        return retrofit.create(service)
    }
}