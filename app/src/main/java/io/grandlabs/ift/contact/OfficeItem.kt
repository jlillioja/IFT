package io.grandlabs.ift.contact

import com.google.gson.annotations.SerializedName

data class OfficeItem(
        @SerializedName("id") val id: Int,
        @SerializedName("Name") val name: String,
        @SerializedName("Address1") val address: String,
        @SerializedName("City") val city: String,
        @SerializedName("State") val state: String,
        @SerializedName("Zip") val zip: String,
        @SerializedName("Fax") val fax: String,
        @SerializedName("Phone") val phone: String
)