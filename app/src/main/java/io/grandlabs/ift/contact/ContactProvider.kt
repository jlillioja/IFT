package io.grandlabs.ift.contact

import io.grandlabs.ift.login.SessionManager
import io.grandlabs.ift.network.IftClient
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ContactProvider @Inject constructor(
        val iftClient: IftClient,
        val sessionManager: SessionManager
) {
    fun getOfficeItems(): Observable<List<OfficeItem>> = iftClient
            .contact(
                    sessionManager.authorizationHeader,
                    1,
                    50
            )
            .subscribeOn(Schedulers.io())
            .map {
                it.body()?.items
            }
}