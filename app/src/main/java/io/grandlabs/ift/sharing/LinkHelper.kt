package io.grandlabs.ift.sharing

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.CalendarContract
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.startActivity
import android.widget.Toast
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog
import com.twitter.sdk.android.tweetcomposer.TweetComposer
import io.grandlabs.ift.calendar.CalendarItem
import java.net.URL
import javax.inject.Inject


class LinkHelper
@Inject constructor(
        private val context: Context
) {

    private val appStoreLink = "https://itunes.apple.com/us/app/apple-store/id1372994087?mt=8"

    fun shareAppLinkByFacebook(fragment: Fragment) {
        shareLinkByFacebook(appStoreLink, fragment)
    }

    fun shareLinkByFacebook(link: String?, fragment: Fragment) {
        val contentBuilder = ShareLinkContent.Builder()
        if (link != null) contentBuilder.setContentUrl(Uri.parse(link))
        val content = contentBuilder.build()
        ShareDialog(fragment).let {
            when {
                it.canShow(content, ShareDialog.Mode.NATIVE) -> it.show(content, ShareDialog.Mode.NATIVE)
                it.canShow(content, ShareDialog.Mode.WEB) -> it.show(content, ShareDialog.Mode.WEB)
                else -> Toast.makeText(context, "Failed to share.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun shareAppLinkByTwitter() {
        shareLinkByTwitter("View the IFT iOS App in the App Store:\n", appStoreLink)
    }

    fun shareLinkByTwitter(title: String, url: String?) {
        val intentBuilder = TweetComposer.Builder(context)
                .text(title)

        if (url != null) {
            intentBuilder.url(URL(url))
        }

        val intent = intentBuilder.createIntent().openIfPossible("Error opening Twitter.")
    }

    fun shareAppLinkByEmail() {
        sendEmail(null,
                "IFT App in the Google Play Store!",
                "See the IFT App in the Google Play Store")
    }

    fun shareLinkByEmail(title: String, url: String?) {
        sendEmail(null, title, "<a href=$url>$title</a>")
    }

    fun sendEmail(address: String?, subject: String?, body: String?) {
        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${address ?: ""}")).apply {
            if (subject != null) putExtra(Intent.EXTRA_SUBJECT, subject)
            if (body != null) putExtra(Intent.EXTRA_TEXT, body)
        }

        Intent.createChooser(emailIntent, "Send Email")
                .openIfPossible("No email app installed.")
    }

    fun shareAppBySms() {
        shareLinkBySms("IFT App in the Google Play Store!", appStoreLink)
    }

    fun shareLinkBySms(title: String, url: String?) {
        Intent(Intent.ACTION_VIEW, Uri.parse("sms"))
                .apply { putExtra("sms_body", "$title\n\n$url") }
                .openIfPossible("No SMS app available. Try email!")
    }

    fun openDialerForNumber(number: String) {
        Intent(
                Intent.ACTION_DIAL,
                Uri.parse("tel:$number")
        ).openIfPossible("No phone app available.")
    }

    fun openWebLink(address: String) {
        Intent(Intent.ACTION_VIEW, Uri.parse(address)).openIfPossible("Failed to open web link. Do you have a web browser?")
    }

    fun openMaps(address: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=$address"))
        intent.setPackage("com.google.android.apps.maps")
        intent.openIfPossible("Google Maps not installed.")
    }

    fun addEventToCalendar(item: CalendarItem) {
        Intent(Intent.ACTION_EDIT).apply {
            type = "vnd.android.cursor.item/event"
            putExtra(CalendarContract.Events.TITLE, item.title)
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, item.dateFrom?.time)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, item.dateTo?.time)
            putExtra(CalendarContract.Events.DESCRIPTION, item.summary)
        }.openIfPossible("No calendar app installed.")
    }

    private fun Intent.openIfPossible(errorMessage: String) {
        if (this.canOpen()) {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            startActivity(context, this, null)
        } else {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun Intent.canOpen() =
            this.resolveActivity(context.packageManager) != null

}