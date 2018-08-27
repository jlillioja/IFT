package io.grandlabs.ift.settings

import io.grandlabs.ift.login.SessionManager
import io.grandlabs.ift.network.IftClient
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import javax.inject.Inject

class AccountInformationProvider @Inject constructor(
        val iftClient: IftClient,
        val sessionManager: SessionManager
) {

    fun getMember(): Observable<IftMember> {
        return iftClient.member(
                sessionManager.memberId,
                sessionManager.authorizationHeader)
                .share()
                .map { it.body() }
    }

    fun getLocalOffice(): Observable<LocalOffice> {
        return getMember()
                .flatMap {
                    iftClient.local(
                            it.localNum,
                            sessionManager.authorizationHeader
                    )
                }
                .map { it.body()?.get(0) }
    }

    fun getLocalOfficers(): Observable<LocalOfficers> {
        return getMember().flatMap {
            Observables.combineLatest(
                    iftClient.localPresident(it.localNum, sessionManager.authorizationHeader),
                    iftClient.localVicePresident(it.localNum, sessionManager.authorizationHeader),
                    iftClient.localFieldServiceDirector(it.localNum, sessionManager.authorizationHeader)
            ) { presidentResponse, vicePresidentResponse, fieldServiceDirectorResponse ->
                LocalOfficers(
                        presidentResponse.body()?.first(),
                        vicePresidentResponse.body()?.first(),
                        fieldServiceDirectorResponse.body()
                )
            }
        }
    }

    fun getNewsPreferences(): Observable<List<Preference>> {
        return iftClient
                .newsPreferences(sessionManager.authorizationHeader)
                .map { it.body() }
    }

    fun getAdvocacyPreferences(): Observable<List<Preference>> {
        return iftClient
                .advocacyPreferences(sessionManager.authorizationHeader)
                .map { it.body() }
    }
}