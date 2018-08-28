package io.grandlabs.ift.messaging

import com.google.firebase.messaging.FirebaseMessagingService
import io.grandlabs.ift.IftApp
import io.grandlabs.ift.login.SessionManager
import javax.inject.Inject

class IftFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate() {
        super.onCreate()

        IftApp.graph.inject(this)
    }

    private val LOG_TAG = this::class.simpleName

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String?) {
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

        sessionManager.updateDeviceToken(token)
    }

}