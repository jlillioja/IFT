package io.grandlabs.ift.news

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.grandlabs.ift.*
import io.grandlabs.ift.calendar.CalendarItem
import io.grandlabs.ift.favorites.FavoritesManager
import io.grandlabs.ift.sharing.LinkHelper
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_web_item.view.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CalendarDetailFragment : DetailFragment() {
    @Inject
    lateinit var navigationController: NavigationController

    @Inject
    override lateinit var linkHelper: LinkHelper

    @Inject
    override lateinit var favoritesManager: FavoritesManager

    var item: CalendarItem? = null

    init {
        IftApp.graph.inject(this)
        navigationController.navigation
                .subscribe { if (it is NavigationState.CalendarDetail) this.item = it.item }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        val date = item?.dateFrom ?: item?.dateTo
        if (date != null) {
            view?.subtitleText?.visibility = View.VISIBLE
            view?.subtitleText?.text = SimpleDateFormat("MMMM dd, yyyy", Locale.US)
                    .format(date)
        }

        view?.addToCalendarButton?.visibility = View.VISIBLE
        view?.addToCalendarButton?.setColorFilter(ContextCompat.getColor(context!!, R.color.light_neutral_grey))
        view?.addToCalendarButton?.setOnClickListener {
            linkHelper.addEventToCalendar(item!!)
        }

        return view
    }

    override fun getActionBarTitle(): String = item?.title ?: "Calendar"

    private val LOG_TAG = this::class.simpleName

    override fun getItem(): WebItem = item!!

    override fun getTitle(): String? = item?.title

    override fun getRedirectUrl(): String? = null

    override fun getBodyHtml(): String? = item?.description

    override fun fetchImage(): Observable<Drawable> {
        return Observable.error(Throwable("No image."))
    }


}
