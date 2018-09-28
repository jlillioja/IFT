package io.grandlabs.ift.settings

import com.google.gson.annotations.SerializedName

data class LocalOffice(
        @SerializedName("LocalNum") val localNumber: Int,
        @SerializedName("Name") val name: String,
        @SerializedName("Address1") val address: String,
        @SerializedName("City") val city: String,
        @SerializedName("State") val state: String,
        @SerializedName("Zip") val zip: String,
        @SerializedName("Phone") val phone: String
)