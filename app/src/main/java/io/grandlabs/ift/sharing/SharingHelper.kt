package io.grandlabs.ift.sharing

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.startActivity
import android.widget.Toast
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog
import com.twitter.sdk.android.tweetcomposer.TweetComposer
import java.net.URL
import javax.inject.Inject


class SharingHelper
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

        val intent = intentBuilder.createIntent()

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        startActivity(context, intent, null)
    }

    fun shareLinkByEmail(title: String, url: String?) {
        val body = "<a href=$url>$title</a>"

        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, title)
        emailIntent.putExtra(Intent.EXTRA_TEXT, body)
        val chooserIntent = Intent.createChooser(emailIntent, "Send Email")
        chooserIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(chooserIntent)
    }

    fun shareAppLinkByEmail() {
        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"))
        emailIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "IFT App in the Google Play Store!")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "See the IFT App in the Google Play Store")

        startActivity(context, Intent.createChooser(emailIntent, "Send Email"), null)
    }

    fun shareLinkBySms(title: String, url: String?) {
        val smsIntent = Intent(Intent.ACTION_VIEW, Uri.parse("sms"))
        smsIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        smsIntent.putExtra("sms_body", "$title\n\n$url")
        if (smsIntent.resolveActivity(context.packageManager) != null) {
            startActivity(context, smsIntent, null)
        } else {
            Toast.makeText(context, "No SMS app available. Try email!", Toast.LENGTH_SHORT).show()
        }
    }

    fun shareAppBySms() {
        shareLinkBySms("IFT App in the Google Play Store!", appStoreLink)
    }

}