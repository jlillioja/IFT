package io.grandlabs.ift.advocate

import android.content.Context
import android.graphics.drawable.Drawable
import com.google.gson.annotations.SerializedName
import io.grandlabs.ift.R

data class AdvocacyItem(
        @SerializedName("Title") val title: String,
        @SerializedName("Summary") val summary: String,
        @SerializedName("Content") val content: String,
        @SerializedName("url") val url: String,
        @SerializedName("Type") val type: Int,
        @SerializedName("EventID") val eventID: String,
        @SerializedName("EmailTo") val emailTo: String,
        @SerializedName("EmailBody") val emailBody: String,
        @SerializedName("EmailSubject") val emailSubject: String,
        @SerializedName("PhoneNumber") val phoneNumber: String,
        @SerializedName("ActionLink") val actionLink: String,
        @SerializedName("base_id") val contentID: String,
        @SerializedName("ShareLink") val shareLink: String
) {
    fun associatedImage(context: Context): Drawable = context.resources.getDrawable(
            when (type) {
                1 -> R.drawable.call_advocacy
                2 -> R.drawable.email_advocacy
                3 -> R.drawable.link_advocacy
                else -> R.drawable.megaphone_icon
            }
    )
}

