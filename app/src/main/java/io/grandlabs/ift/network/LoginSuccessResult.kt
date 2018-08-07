package io.grandlabs.ift.network

import com.google.gson.annotations.SerializedName

data class LoginSuccessResult(
        @SerializedName("access_token") val token: String,
        @SerializedName("expires_in") val expiration: Long,
        @SerializedName("token_type") val tokenType: String
)