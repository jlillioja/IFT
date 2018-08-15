package io.grandlabs.ift.advocate

import io.grandlabs.ift.login.LoginManager
import io.grandlabs.ift.network.IftClient
import io.reactivex.Observable
import javax.inject.Inject

class AdvocacyProvider
@Inject constructor(
        val iftClient: IftClient,
        val loginManager: LoginManager
) {

    fun getAdvocacyItems(): Observable<List<AdvocacyItem>> {
        return iftClient
                .advocacy("Bearer: ${loginManager.token}")
                .map { it.body() }
    }

}