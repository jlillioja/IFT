package io.grandlabs.ift.calendar


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.grandlabs.ift.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_calendar_list.view.*
import javax.inject.Inject

class CalendarFragment : IftFragment() {

    init {
        IftApp.graph.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        listener?.setCurrentlySelectedFragment(this)

        val adapter = CalendarFragmentPagerAdapter(childFragmentManager)
        view.findViewById<ViewPager>(R.id.calendarPager).adapter = adapter

        return view
    }

    override fun getActionBarTitle(): String = "Calendar"


    class CalendarFragmentPagerAdapter(supportFragmentManager: FragmentManager)
        : FragmentPagerAdapter(supportFragmentManager) {

        override fun getItem(position: Int): Fragment = when (position) {
//            0 -> LocalCalendarListFragment()
            else -> IftCalendarListFragment()
        }

        override fun getCount(): Int = 1

        override fun getPageTitle(position: Int): CharSequence? = when (position) {
//            0 -> "LOCAL/COUNCIL EVENTS"
            else -> "IFT EVENTS"
        }

    }

    open class CalendarListFragment : Fragment() {

        @Inject
        lateinit var calendarProvider: CalendarProvider
        @Inject
        lateinit var calendarAdapter: CalendarAdapter
        @Inject
        lateinit var navigationController: NavigationController

        protected open var itemFilter: (CalendarItem) -> Boolean = { true }

        init {
            IftApp.graph.inject(this)
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.fragment_calendar_list, container, false)

            view.calendarListView.adapter = calendarAdapter
            view.calendarListView.setOnItemClickListener { _, _, position, _ ->
                navigationController.navigateTo(NavigationState.CalendarDetail(calendarAdapter.getItem(position)))
            }

            view.swipeRefresh.setOnRefreshListener {
                refresh()
            }

            refresh()

            return view
        }

        private fun refresh() {
            calendarProvider.getCalendar()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map { it.filter(itemFilter) }
                    .subscribe({
                        view?.loadingSpinner?.visibility = View.GONE
                        view?.swipeRefresh?.isRefreshing = false
                        calendarAdapter.clear()
                        calendarAdapter.addAll(it)
                        calendarAdapter.notifyDataSetChanged()
                    }, {}, {})
        }
    }

    class LocalCalendarListFragment : CalendarListFragment() {
        override var itemFilter: (CalendarItem) -> Boolean = { item ->
            (item.calendarType == CalendarItemType.LOCAL
                    || item.calendarType == CalendarItemType.COUNCIL)
        }
    }

    class IftCalendarListFragment : CalendarListFragment() {
        override var itemFilter: (CalendarItem) -> Boolean = { item ->
            !(item.calendarType == CalendarItemType.LOCAL
                    || item.calendarType == CalendarItemType.COUNCIL)
        }
    }
}
