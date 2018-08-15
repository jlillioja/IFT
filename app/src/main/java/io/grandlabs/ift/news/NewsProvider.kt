package io.grandlabs.ift.news

import io.grandlabs.ift.network.IftClient
import io.grandlabs.ift.login.LoginManager
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NewsProvider
@Inject constructor(
        val iftClient: IftClient,
        val loginManager: LoginManager
) {
    fun getNews() : Observable<NewsResult> = iftClient
            .news(1, 10, "Bearer: ${loginManager.token}")
            .subscribeOn(Schedulers.io())
            .map { it.body() }
}