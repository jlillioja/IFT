package io.grandlabs.ift.login

import android.content.Context
import android.util.Base64
import android.util.Log
import com.google.gson.GsonBuilder
import io.grandlabs.ift.defaultSharedPreferences
import io.grandlabs.ift.network.IftClient
import io.grandlabs.ift.network.TokenData
import io.reactivex.Observable
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

    private val scope = productionScope // TODO: change back

    private val defaultLoginParams = mapOf(
            "client_id" to clientId,
            "client_secret" to clientSecret,
            "scope" to scope,
            "grant_type" to grantType
    )

    var token: String? = null
    var tokenData: TokenData? = null
    val memberId: Int get() = tokenData?.id?.toInt() ?: 0
    val authorizationHeader: String get() = "Bearer $token"

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
                    decodeToken(it)
                }
            }
        }, {
            Log.d(LOG_TAG, it.localizedMessage)
        }, {})

        return result.map { if (it.isSuccessful) LoginResult.Success else LoginResult.Failure }
    }

    private fun decodeToken(token: String) {
        /*
        - (NSDictionary *) dataDictFromJWTToken {

    NSString *jwt = [self getToken];
    if (jwt == nil) return nil;

    NSArray *segments = [jwt componentsSeparatedByString:@"."];
    NSString *base64String = [segments objectAtIndex: 1];
    NSLog(@"%@", base64String);
    // => "eyJmb28iOiJiYXIifQ"


    int requiredLength = (int)(4 * ceil((float)[base64String length] / 4.0));
    NSUInteger nbrPaddings = requiredLength - [base64String length];


    // Pad with leading =
    if (nbrPaddings > 0) {
        NSString *padding = [[NSString string] stringByPaddingToLength:nbrPaddings withString:@"=" startingAtIndex:0];
        base64String = [base64String stringByAppendingString:padding];
    }

    base64String = [base64String stringByReplacingOccurrencesOfString:@"-" withString:@"+"];
    base64String = [base64String stringByReplacingOccurrencesOfString:@"_" withString:@"/"];

    // Decode Base64
    NSData *decodedData = [[NSData alloc] initWithBase64EncodedString:base64String options:0];
    NSString *decodedString = [[NSString alloc] initWithData:decodedData encoding:NSUTF8StringEncoding];
    NSLog(@"%@", decodedString);

    NSDictionary *jsonDictionary = [NSJSONSerialization JSONObjectWithData:[decodedString dataUsingEncoding:NSUTF8StringEncoding] options:0 error:nil];
    return jsonDictionary;

}
         */

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
}