package io.grandlabs.ift.network

import com.google.gson.annotations.SerializedName

data class PutAlertPreferencesRequest(
        @SerializedName("EmailAlerts") val emailAlertsActive: Int,
        @SerializedName("PushNotifications") val pushNotificationsActive: Int
)
