package io.grandlabs.ift.network

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import io.grandlabs.ift.BuildConfig
import io.grandlabs.ift.IftApp
import io.grandlabs.ift.R.id.passwordEntry
import io.grandlabs.ift.R.id.usernameEntry
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.ResponseBody
import retrofit2.Response
import java.util.*
import javax.inject.Inject

class LoginManager
@Inject constructor(
        var iftClient: IftClient,
        var context: Context
) {

    private val LOG_TAG = "LoginManager"

    private val clientSecret = "uGewjGd6pPtmeWcz"
    private val clientId = "Vault"
    private val productionScope = "api"
    private val devScope = "api-dev"
    private val grantType = "password"

    private val scope = if (BuildConfig.DEBUG) devScope else productionScope

    var token: String? = null

    private val defaultLoginParams = mapOf(
            "client_id" to clientId,
            "client_secret" to clientSecret,
            "scope" to scope,
            "grant_type" to grantType
    )

    fun login(username: String, password: String): Observable<LoginResult> {
        val params = defaultLoginParams.plus(mapOf("username" to username, "password" to password))
        val result = iftClient.login(params)
        result.subscribeBy(onNext = {
            if (it.isSuccessful && it.body()?.token != null) {
                Log.d(LOG_TAG, "Success!")
                Log.d(LOG_TAG, "token: ${it.body()?.token}")
                it.body()?.token?.let {
                    context.defaultSharedPreferences
                            .edit()
                            .putString("authToken", it)
                            .apply()
                }
            }
        }, onError = {
            Log.d(LOG_TAG, "Error")
        })

        return result.map { if (it.isSuccessful) LoginResult.Success else LoginResult.Failure }
    }
}

inline val Context.defaultSharedPreferences: SharedPreferences
    get() = PreferenceManager.getDefaultSharedPreferences(this)