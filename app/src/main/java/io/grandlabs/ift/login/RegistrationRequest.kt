package io.grandlabs.ift.login

import com.google.gson.annotations.SerializedName

data class RegistrationRequest(
        @SerializedName("HomeEmail") val email: String,
        @SerializedName("password") val password: String
)