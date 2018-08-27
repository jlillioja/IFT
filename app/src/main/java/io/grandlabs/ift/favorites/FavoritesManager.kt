package io.grandlabs.ift.favorites

import io.grandlabs.ift.WebItem
import io.grandlabs.ift.login.SessionManager
import io.grandlabs.ift.network.IftClient
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import javax.inject.Inject

class FavoritesManager
@Inject constructor(
        private val iftClient: IftClient,
        private val sessionManager: SessionManager
) {

    fun setFavorite(item: WebItem, isFavorite: Boolean): Observable<Boolean> {
        return if (isFavorite) {
            iftClient.postFavorite(sessionManager.authorizationHeader, item.favoriteUrl, item.contentId)
        } else {
            iftClient.deleteFavorite(sessionManager.authorizationHeader, item.favoriteUrl, item.contentId)
        }
                .map {
                    if (it.code() == 200 || it.code() == 204) isFavorite
                    else item.isFavorite
                }
                .onErrorReturnItem(item.isFavorite)
    }

    fun getAllFavorites(): Observable<List<WebItem>> {
        return Observables.combineLatest(
                iftClient
                        .favoriteNews(sessionManager.authorizationHeader)
                        .map { it.body() },
                iftClient.favoritesAdvocacy(sessionManager.authorizationHeader)
                        .map { it.body() },
                iftClient.favoritesCalendarEvents(sessionManager.authorizationHeader)
                        .map { it.body() }
        ) { favoriteNews, favoriteAdvocacy, favoriteCalendarEvents ->
            (favoriteNews ?: emptyList())
                    .plus(favoriteAdvocacy ?: emptyList())
                    .plus(favoriteCalendarEvents ?: emptyList())
        }
    }
}