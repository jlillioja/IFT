package io.grandlabs.ift.login

import android.content.Context
import android.util.Base64
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.GsonBuilder
import io.grandlabs.ift.BuildConfig
import io.grandlabs.ift.defaultSharedPreferences
import io.grandlabs.ift.network.IftClient
import io.grandlabs.ift.network.TokenData
import io.grandlabs.ift.network.TokenRequest
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager
@Inject constructor(
        var iftClient: IftClient,
        var context: Context
) {

    private val LOG_TAG = this::class.simpleName

    private val clientSecret = "uGewjGd6pPtmeWcz"
    private val clientId = "Vault"
    private val productionScope = "api"
    private val devScope = "api-dev"
    private val grantType = "password"

    private val usernameKey = "username"
    private val passwordKey = "password"
    private val authTokenKey = "authToken"

    //    private val scope = if (BuildConfig.DEBUG) devScope else productionScope
    private val scope = productionScope // TODO: change back again

    private val defaultLoginParams = mapOf(
            "client_id" to clientId,
            "client_secret" to clientSecret,
            "scope" to scope,
            "grant_type" to grantType
    )

    var authToken: String? = null
    var tokenData: TokenData? = null
    val memberId: Int get() = tokenData?.id?.toInt() ?: 0
//    var member: IftMember? = null
    fun isUserAMember(): Boolean = !(tokenData?.localNum?.equals("9911") ?: true)
    val authorizationHeader: String get() = "Bearer $authToken"

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
                Log.d(LOG_TAG, "authToken: ${it.body()?.token}")

                it.body()?.token?.let {
                    authToken = it
                    context.defaultSharedPreferences
                            .edit()
                            .putString(usernameKey, username)
                            .putString(passwordKey, password)
                            .putString(authTokenKey, it)
                            .apply()
                    decodeToken(it)

                }

                updateDeviceToken()
            }
        }, {
            Log.d(LOG_TAG, it.localizedMessage)
        }, {})

        return result.map { if (it.isSuccessful) LoginResult.Success else LoginResult.Failure }
    }

    fun logout() {
        authToken = null
        tokenData = null
        context.defaultSharedPreferences.edit()
                .remove(usernameKey)
                .remove(passwordKey)
                .remove("authToken")
                .apply()
    }

    private fun decodeToken(token: String) {
        val segments = token.split('.')
        var encodedInfo = segments[1]

        // Required length is length rounded up to nearest multiple of 4
        // Done with modular arithmetic
        val requiredLength = (4 - encodedInfo.length % 4) % 4 + encodedInfo.length
        encodedInfo.padStart(requiredLength, '=')

        // Replace '-' with '+'
        // Replace '_' with '/'
        encodedInfo = encodedInfo.replace('-', '+')
        encodedInfo = encodedInfo.replace('_', '/')

        val decodedInfo = Base64.decode(encodedInfo, 0)
        val recodedInfo = String(decodedInfo)
        val json = JSONObject(recodedInfo)
        tokenData = GsonBuilder().create().fromJson(recodedInfo, TokenData::class.java)
    }

    fun checkRegistration(
            email: String,
            firstName: String,
            lastName: String
    ): Observable<RegistrationStatus> {
        return iftClient.checkRegistration(
                email,
                if (firstName.isNotBlank()) firstName else null,
                if (lastName.isNotBlank()) lastName else null
        ).map {
            if (it.code() == 404) {
                RegistrationStatus.NonMember
            } else {
                RegistrationStatus.AlreadyRegistered
            }
        }.onErrorReturnItem(RegistrationStatus.NonMember)
    }

    fun submitRegistration(
            email: String,
            password: String
    ): Observable<RegistrationResult> {
        return iftClient.register(RegistrationRequest(email, password))
                .map { it.body() }
    }

    private val prodtokenType = 3
    private val devTokenType = 4
    private val tokenTypeId = if (BuildConfig.DEBUG) devTokenType else prodtokenType

    fun updateDeviceToken(deviceToken: String? = null) {
        if (deviceToken != null) {
            iftClient.postToken(
                    authorizationHeader,
                    TokenRequest(
                            tokenTypeId,
                            deviceToken
                    )
            ).subscribeBy(
                    onNext = {
                        Log.d(LOG_TAG, "Successfully posted token.")
                    },
                    onError = {
                        Log.d(LOG_TAG, "Error posting token.")
                    }
            )
        } else {
            FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateDeviceToken(task.result.token)
                } else {
                    Log.d(LOG_TAG, "getInstanceId failed", task.exception)
                }
            }
        }
    }

}