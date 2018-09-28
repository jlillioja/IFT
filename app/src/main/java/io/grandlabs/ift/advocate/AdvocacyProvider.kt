package io.grandlabs.ift.advocate

import io.grandlabs.ift.login.SessionManager
import io.grandlabs.ift.network.IftClient
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import javax.inject.Inject

class AdvocacyProvider
@Inject constructor(
        private val iftClient: IftClient,
        private val sessionManager: SessionManager
) {

    companion object {
        const val favoritesUrl = "member_favoriteadvocacy"
    }

    fun getAdvocacyItems(): Observable<List<AdvocacyItem>> {
        return Observables.combineLatest(
                iftClient
                        .advocacy(sessionManager.authorizationHeader)
                        .map { it.body() },
                iftClient
                        .favoritesAdvocacy(sessionManager.authorizationHeader)
                        .map { it.body() }
        ) { items, favorites ->
            items?.forEach {
                it.isFavorite = favorites?.contains(it) ?: false
            }
            items
        }
    }

}