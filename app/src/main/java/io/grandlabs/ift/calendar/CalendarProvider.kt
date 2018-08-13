package io.grandlabs.ift.calendar

import io.grandlabs.ift.network.CalendarItem
import io.grandlabs.ift.network.IftClient
import io.grandlabs.ift.network.LoginManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CalendarProvider
@Inject constructor(
        val iftClient: IftClient,
        val loginManager: LoginManager
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
                        "Bearer: ${loginManager.token}")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.body()?.items }
    }
}