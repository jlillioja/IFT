package io.grandlabs.ift.invite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.grandlabs.ift.IftApp
import io.grandlabs.ift.IftFragment
import io.grandlabs.ift.R
import io.grandlabs.ift.sharing.SharingHelper
import kotlinx.android.synthetic.main.fragment_invite.view.*
import javax.inject.Inject


class InviteFragment : IftFragment() {

    @Inject lateinit var sharingHelper: SharingHelper

    init {
        IftApp.graph.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_invite, container, false)

        listener?.setCurrentlySelectedFragment(this)

        view.facebook_button.setOnClickListener { onClickFacebook() }
        view.twitter_button.setOnClickListener { onClickTwitter() }
        view.email_button.setOnClickListener { onClickEmail() }
        view.message_button.setOnClickListener { onClickMessage() }

        return view
    }

    override fun getActionBarTitle(): String = "Invite Friends"

    private fun onClickFacebook() {
        sharingHelper.shareAppLinkByFacebook(this)
    }

    private fun onClickTwitter() {
        sharingHelper.shareAppLinkByTwitter()
    }

    private fun onClickEmail() {
        sharingHelper.shareAppLinkByEmail()
    }

    private fun onClickMessage() {
        sharingHelper.shareAppBySms()
    }

}