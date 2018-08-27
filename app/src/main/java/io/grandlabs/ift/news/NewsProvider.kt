package io.grandlabs.ift.news

import io.grandlabs.ift.login.SessionManager
import io.grandlabs.ift.network.IftClient
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import javax.inject.Inject

class NewsProvider
@Inject constructor(
        val iftClient: IftClient,
        val sessionManager: SessionManager
) {

    companion object {
        const val favoritesUrl = "member_favoritenews"
    }

    fun getNews(): Observable<NewsResult> = Observables.combineLatest(
            iftClient
                    .news(sessionManager.authorizationHeader, 1, 10)
                    .map { it.body() },
            iftClient
                    .favoriteNews(sessionManager.authorizationHeader)
                    .map { it.body() }
    ) { items, favorites ->
        items?.items?.forEach {
            it.isFavorite = favorites?.contains(it) ?: false
        }
        items
    }
}