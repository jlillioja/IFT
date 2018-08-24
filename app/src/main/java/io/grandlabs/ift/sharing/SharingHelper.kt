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
        val content = ShareLinkContent.Builder().setContentUrl(Uri.parse(appStoreLink)).build()
        ShareDialog(fragment).let {
            if (it.canShow(content, ShareDialog.Mode.NATIVE)) {
                it.show(content, ShareDialog.Mode.NATIVE)
            } else if (it.canShow(content, ShareDialog.Mode.WEB)) {
                it.show(content, ShareDialog.Mode.WEB)
            } else {
                Toast.makeText(context, "Failed to share.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun shareAppLinkByTwitter() {
        val intent = TweetComposer.Builder(context)
                .text("View the IFT iOS App in the App Store:\n")
                .url(URL(appStoreLink))
                .createIntent()

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        startActivity(context, intent, null)
    }

    fun shareAppLinkByEmail() {
        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "IFT App in the Google Play Store!")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "See the IFT App in the Google Play Store")

        startActivity(context, Intent.createChooser(emailIntent, "Send Email"), null)
    }

    fun shareAppBySms() {
        val smsIntent = Intent(Intent.ACTION_VIEW, Uri.parse("sms"))
        if (smsIntent.resolveActivity(context.packageManager) != null) {
            startActivity(context, smsIntent, null)
        } else {
            Toast.makeText(context, "No SMS app available. Try email!", Toast.LENGTH_SHORT).show()
        }
    }

}