package io.grandlabs.ift.advocate

import android.content.Context
import android.graphics.drawable.Drawable
import com.google.gson.annotations.SerializedName
import io.grandlabs.ift.R
import io.grandlabs.ift.WebItem
import io.grandlabs.ift.advocate.AdvocacyItem.AdvocacyType.*
import io.grandlabs.ift.sharing.LinkHelper
import io.reactivex.Observable

data class AdvocacyItem(
        @SerializedName("Title") override val title: String,
        @SerializedName("Summary") override val summary: String,
        @SerializedName("Content") override val content: String,
        @SerializedName("contentUrl") override val contentUrl: String?,
        @SerializedName("Type") val typeInt: Int,
        @SerializedName("EventID") val eventID: String,
        @SerializedName("EmailTo") val emailTo: String?,
        @SerializedName("EmailSubject") val emailSubject: String?,
        @SerializedName("EmailBody") val emailBody: String?,
        @SerializedName("PhoneNumber") val phoneNumber: String?,
        @SerializedName("ActionLink") val actionLink: String?,
        @SerializedName("base_id") override val contentId: String,
        @SerializedName("ShareLink") val shareLink: String?
) : WebItem() {
    override val attributedTitle: String?
        get() = null
    override val actionBarTitle: String
        get() = "Advocacy Center"
    override val favoriteUrl: String
        get() = AdvocacyProvider.favoritesUrl
    override val redirectUrl: String?
        get() = null
    override var isFavorite: Boolean = false
    val type get() = when(typeInt) {
        1 -> Call
        2 -> Email
        3 -> Link
        else -> Share
    }

    enum class AdvocacyType {
        Call,
        Email,
        Link,
        Share
    }

    override fun associatedImage(context: Context): Observable<Drawable>? = Observable.just(
            context.resources.getDrawable(
                    when (typeInt) {
                        1 -> R.drawable.call_advocacy
                        2 -> R.drawable.email_advocacy
                        3 -> R.drawable.link_advocacy
                        else -> R.drawable.megaphone_icon
                    }
            ))

    fun takeAction(linkHelper: LinkHelper) {
        when (type) {
            Call -> if (!phoneNumber.isNullOrBlank()) {
                linkHelper.openDialerForNumber(phoneNumber!!)
            }
            Email -> if (!emailTo.isNullOrBlank()) {
                linkHelper.sendEmail(emailTo, emailSubject, emailBody)
            }
            Link -> if (!actionLink.isNullOrBlank()) {
                linkHelper.openWebLink(actionLink!!)
            }
            Share -> if (!shareLink.isNullOrBlank()) {
                linkHelper.openWebLink(shareLink!!)
            }
        }
    }
}

