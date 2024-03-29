package io.grandlabs.ift.calendar

import io.grandlabs.ift.login.SessionManager
import io.grandlabs.ift.network.IftClient
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CalendarManager
@Inject constructor(
        private val iftClient: IftClient,
        private val sessionManager: SessionManager
) {

    companion object {
        const val favoritesUrl = "member_favoritecalendarevent"
    }

    fun getCalendar(): Observable<List<CalendarItem>> {
        val sortString = "DateFrom ASC"
        val todayFormatted = SimpleDateFormat("MM/dd/yyyy", Locale.US).format(Date())
        val filterString = "(DateTo >= $todayFormatted OR DateTo IS NULL)"

        return Observables.combineLatest(
                iftClient
                        .calendar(
                                sessionManager.authorizationHeader,
                                1,
                                50,
                                filterString,
                                sortString)
                        .map { it.body()?.items },
                iftClient
                        .favoritesCalendarEvents(sessionManager.authorizationHeader)
        ) { items, favorites ->
            items?.forEach {
                it.isFavorite = favorites.body()?.contains(it) ?: false
            }
            items
        }
    }

    fun saveEvent(addEventRequest: AddEventRequest): Observable<Boolean> {
        return iftClient
                .addEvent(
                        sessionManager.authorizationHeader,
                        addEventRequest)
                .map { true }
                .onErrorReturnItem(false)
    }
}