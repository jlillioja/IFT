package io.grandlabs.ift.login

import com.google.gson.annotations.SerializedName

data class CheckRegistrationResult(
        @SerializedName("StatusCode") val code: Int,
        @SerializedName("Message") val message: String,
        @SerializedName("URL") val url: String
)