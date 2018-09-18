package io.grandlabs.ift.settings

import com.google.gson.annotations.SerializedName

data class IftMember(
        @SerializedName("FirstName") val firstName: String,
        @SerializedName("LastName") val lastName: String,
        @SerializedName("Address1") val address: String,
        @SerializedName("City") val city: String,
        @SerializedName("State") val state: String,
        @SerializedName("Zip") val zip: String,
        @SerializedName("HomePhone") val homePhone: String,
        @SerializedName("WorkPhone") val workPhone: String,
        @SerializedName("CellPhone") val cellPhone: String,
        @SerializedName("HomeEmail") val homeEmail: String,
        @SerializedName("ID") val memberId: String,
        @SerializedName("LocalNum") val localNum: Int,
        @SerializedName("PushNotifications") val isPushNotificationsEnabled: Boolean,
        @SerializedName("EmailAlerts") val isEmailAlertsEnabled: Boolean
) {
    val displayedPhone: String
        get() = when {
            workPhone.isNotBlank() -> workPhone
            cellPhone.isNotBlank() -> cellPhone
            else -> homePhone
        }
}