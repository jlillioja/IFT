package io.grandlabs.ift.calendar

import com.google.gson.annotations.SerializedName

data class AddEventRequest(
        @SerializedName("Title") val title: String,
        @SerializedName("Summary") val summary: String,
        @SerializedName("Description") val description: String,
        @SerializedName("URL") val url: String,
        @SerializedName("DateFrom") val dateFrom: String,
        @SerializedName("DateTo") val dateTo: String,
        @SerializedName("Address") val address: String,
        @SerializedName("City") val city: String,
        @SerializedName("State") val state: String,
        @SerializedName("Zip") val zip: String,
        @SerializedName("AllDay") val isAllDay: Boolean
)