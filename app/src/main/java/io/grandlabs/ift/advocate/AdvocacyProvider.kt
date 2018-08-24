package io.grandlabs.ift.advocate

import io.grandlabs.ift.login.SessionManager
import io.grandlabs.ift.network.IftClient
import io.reactivex.Observable
import javax.inject.Inject

class AdvocacyProvider
@Inject constructor(
        val iftClient: IftClient,
        val sessionManager: SessionManager
) {

    fun getAdvocacyItems(): Observable<List<AdvocacyItem>> {
        return iftClient
                .advocacy(sessionManager.authorizationHeader)
                .map { it.body() }
    }

}