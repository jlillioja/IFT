package io.grandlabs.ift.network

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import io.grandlabs.ift.BuildConfig
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiServiceCreator {

    private const val defaultTimeout = 5L

    private val httpClient = OkHttpClient.Builder()
            .connectTimeout(defaultTimeout, TimeUnit.SECONDS)
            .readTimeout(defaultTimeout, TimeUnit.SECONDS)
            .writeTimeout(defaultTimeout, TimeUnit.SECONDS)
            .also {
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor(CustomHttpLogging())

                    logging.level = HttpLoggingInterceptor.Level.BODY
                    it.addInterceptor(logging)
                }
            }

    class CustomHttpLogging : HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            val logName = "OkHttp"
            if (!message.startsWith("{")) {
                Log.d(logName, message)
                return
            }
            try {
                val prettyPrintJson = GsonBuilder().setPrettyPrinting().create().toJson(JsonParser().parse(message))
                Log.d(logName, prettyPrintJson)
            } catch (m: JsonSyntaxException) {
                Log.d(logName, message)
            }

        }
    }

    private val gson = GsonBuilder().create()
    private val rxAdapter = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())

    private const val apiUrl = "https://api.ift-aft.org/app/"
    private const val apiDevUrl = "https://api-dev.ift-aft.org/app/"
    val baseUrl = if (BuildConfig.DEBUG) apiDevUrl else apiUrl

    private val builder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(rxAdapter)

    fun <RESTService> createService(service: Class<RESTService>): RESTService {
        val retrofit = builder.client(httpClient.build()).build()
        return retrofit.create(service)
    }
}