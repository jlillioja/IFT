package io.grandlabs.ift.news

import io.grandlabs.ift.login.SessionManager
import io.grandlabs.ift.network.IftClient
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NewsProvider
@Inject constructor(
        val iftClient: IftClient,
        val sessionManager: SessionManager
) {
    fun getNews() : Observable<NewsResult> = iftClient
            .news(sessionManager.authorizationHeader, 1, 10)
            .subscribeOn(Schedulers.io())
            .map { it.body() }
}