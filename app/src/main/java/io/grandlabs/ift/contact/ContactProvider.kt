package io.grandlabs.ift.contact

import io.grandlabs.ift.login.LoginManager
import io.grandlabs.ift.network.IftClient
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import java.util.logging.LogManager
import javax.inject.Inject

class ContactProvider @Inject constructor(
        val iftClient: IftClient,
        val loginManager: LoginManager
) {
    fun getOfficeItems(): Observable<List<OfficeItem>> = iftClient
            .contact(
                    "Bearer: Authorization ${loginManager.token}",
                    1,
                    50
            )
            .subscribeOn(Schedulers.io())
            .map {
                it.body()?.items
            }
}