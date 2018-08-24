package io.grandlabs.ift.calendar

import io.grandlabs.ift.login.SessionManager
import io.grandlabs.ift.network.IftClient
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CalendarProvider
@Inject constructor(
        val iftClient: IftClient,
        val sessionManager: SessionManager
) {
    fun getCalendar(): Observable<List<CalendarItem>> {

        val sortString = "DateFrom ASC"
        val todayFormatted = SimpleDateFormat("MM/dd/yyyy", Locale.US).format(Date())
        val filterString = "(DateTo >= $todayFormatted OR DateTo IS NULL)"

        return iftClient
                .calendar(
                        sortString,
                        filterString,
                        1,
                        50,
                        sessionManager.authorizationHeader)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.body()?.items }
    }
}