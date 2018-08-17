package io.grandlabs.ift.settings

import io.grandlabs.ift.login.LoginManager
import io.grandlabs.ift.network.IftClient
import javax.inject.Inject

class MemberProvider @Inject constructor(
        val iftClient: IftClient,
        val loginManager: LoginManager
) {

}