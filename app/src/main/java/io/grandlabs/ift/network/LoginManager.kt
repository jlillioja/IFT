package io.grandlabs.ift.network

import android.content.Context
import android.util.Log
import io.grandlabs.ift.BuildConfig
import io.grandlabs.ift.defaultSharedPreferences
import io.reactivex.Observable
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

    private val usernameKey = "username"
    private val passwordKey = "password"

    private val scope = if (BuildConfig.DEBUG) devScope else productionScope

    var token: String? = null

    private val defaultLoginParams = mapOf(
            "client_id" to clientId,
            "client_secret" to clientSecret,
            "scope" to scope,
            "grant_type" to grantType
    )

    fun silentLogin(): Observable<LoginResult>? {
        val username = context.defaultSharedPreferences.getString(usernameKey, null)
        val password = context.defaultSharedPreferences.getString(passwordKey, null)
        if (username != null && password != null) {
            return login(username, password)
        } else {
            return null
        }
    }

    fun login(username: String, password: String): Observable<LoginResult> {
        val params = defaultLoginParams.plus(mapOf(usernameKey to username, passwordKey to password))
        val result = iftClient.login(params).share().replay().also { it.connect() }
        result.subscribe({
            if (it.isSuccessful && it.body()?.token != null) {
                Log.d(LOG_TAG, "Success!")
                Log.d(LOG_TAG, "token: ${it.body()?.token}")
                it.body()?.token?.let {
                    token = it
                    context.defaultSharedPreferences
                            .edit()
                            .putString(usernameKey, username)
                            .putString(passwordKey, password)
                            .putString("authToken", it)
                            .apply()
                }
            }
        }, {
            Log.d(LOG_TAG, it.localizedMessage)
        }, {})

        return result.map { if (it.isSuccessful) LoginResult.Success else LoginResult.Failure }
    }
}