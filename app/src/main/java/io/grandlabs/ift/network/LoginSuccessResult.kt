package io.grandlabs.ift.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginSuccessResult(
        @SerializedName("access_token") @Expose val token: String,
        @SerializedName("expires_in") @Expose val expiration: Long,
        @SerializedName("token_type") @Expose val tokenType: String
)