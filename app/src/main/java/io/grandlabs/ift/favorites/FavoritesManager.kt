package io.grandlabs.ift.favorites

import io.grandlabs.ift.WebItem
import io.grandlabs.ift.login.SessionManager
import io.grandlabs.ift.network.IftClient
import io.reactivex.Observable
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
}