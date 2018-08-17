package io.grandlabs.ift.invite

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.grandlabs.ift.IftApp
import io.grandlabs.ift.R
import kotlinx.android.synthetic.main.fragment_invite.view.*
import javax.inject.Inject


class InviteFragment
@Inject constructor() : Fragment() {

    init {
        IftApp.graph.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_invite, container, false)

        view.facebook_button.setOnClickListener { onClickFacebook() }
        view.twitter_button.setOnClickListener { onClickTwitter() }
        view.email_button.setOnClickListener { onClickEmail() }
        view.message_button.setOnClickListener { onClickMessage() }

        return view
    }

    private fun onClickFacebook() {
        Toast.makeText(context, "Coming soon!", Toast.LENGTH_SHORT).show()
    }

    private fun onClickTwitter() {
        Toast.makeText(context, "Coming soon!", Toast.LENGTH_SHORT).show()
    }

    private fun onClickEmail() {
        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "IFT App in the Google Play Store!")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "See the IFT App in the Google Play Store")

        startActivity(Intent.createChooser(emailIntent, "Send Email"))
    }

    private fun onClickMessage() {
        val smsIntent = Intent(Intent.ACTION_VIEW, Uri.parse("sms"))
        if (smsIntent.resolveActivity(activity?.packageManager) != null) {
            startActivity(smsIntent)
        } else {
            Toast.makeText(context, "No SMS app available. Try email!", Toast.LENGTH_SHORT).show()
        }
    }

}