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

    private val httpClient = OkHttpClient.Builder()

    private val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
    private val rxAdapter = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())

    private const val apiUrl = "https://api-dev.ift-aft.org/app/"
    private val builder = Retrofit.Builder()
            .baseUrl(apiUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(rxAdapter)

//    private const val ssoUrl = "https://sso.ift-aft.org/connect/token/"
//    private val loginBuilder = Retrofit.Builder()
//            .baseUrl(ssoUrl)
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .addCallAdapterFactory(rxAdapter)

    private const val defaultTimeout = 30

    init {

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(logging)
        }

        httpClient.connectTimeout(defaultTimeout.toLong(), TimeUnit.SECONDS)
        httpClient.readTimeout(defaultTimeout.toLong(), TimeUnit.SECONDS)
        httpClient.writeTimeout(defaultTimeout.toLong(), TimeUnit.SECONDS)
    }

    fun <RESTService> createService(service: Class<RESTService>): RESTService {
        val retrofit = builder.client(httpClient.build()).build()
        return retrofit.create(service)
    }
//
//
//    fun <RESTService> createLoginService(service: Class<RESTService>): RESTService {
//        val retrofit = loginBuilder.client(httpClient.build()).build()
//        return retrofit.create(service)
//    }

}