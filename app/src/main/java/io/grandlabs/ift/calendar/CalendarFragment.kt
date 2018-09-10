package io.grandlabs.ift.calendar


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.grandlabs.ift.*
import io.grandlabs.ift.login.SessionManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_calendar.view.*
import kotlinx.android.synthetic.main.fragment_calendar_list.view.*
import javax.inject.Inject

class CalendarFragment : IftFragment() {

    @Inject
    lateinit var sessionManager: SessionManager

    init {
        IftApp.graph.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        val adapter = CalendarFragmentPagerAdapter(childFragmentManager, sessionManager.isUserAMember())
        view.calendarPager.adapter = adapter
        view.calendarPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                listener?.setCurrentlySelectedFragment(adapter.getItem(position) as IftFragment)

            }
        })

        listener?.setCurrentlySelectedFragment(adapter.getItem(0) as IftFragment)

        return view
    }

    override fun getActionBarTitle(): String = "Calendar"


    class CalendarFragmentPagerAdapter(supportFragmentManager: FragmentManager,
                                       private val shouldShowLocalEvents: Boolean
    ) : FragmentPagerAdapter(supportFragmentManager) {

        private val iftCalendarTitle = "IFT EVENTS"
        private val localCalendarTitle = "LOCAL/COUNCIL EVENTS"

        override fun getCount(): Int = if (shouldShowLocalEvents) 2 else 1

        override fun getPageTitle(position: Int): CharSequence? = if (shouldShowLocalEvents) {
            when (position) {
                0 -> localCalendarTitle
                else -> iftCalendarTitle
            }
        } else {
            iftCalendarTitle
        }

        override fun getItem(position: Int): Fragment = if (shouldShowLocalEvents) {
            when (position) {
                0 -> LocalCalendarListFragment()
                else -> IftCalendarListFragment()
            }
        } else {
            IftCalendarListFragment()
        }
    }

    open class CalendarListFragment : IftFragment() {
        @Inject
        lateinit var calendarManager: CalendarManager

        @Inject
        lateinit var calendarAdapter: CalendarAdapter
        @Inject
        lateinit var navigationController: NavigationController
        protected open var itemFilter: (CalendarItem) -> Boolean = { true }

        init {
            IftApp.graph.inject(this)
        }

        override fun getActionBarTitle(): String = "Calendar"

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.fragment_calendar_list, container, false)

            view.calendarListView.adapter = calendarAdapter
            view.calendarListView.setOnItemClickListener { _, _, position, _ ->
                navigationController.navigateTo(NavigationState.CalendarDetail(calendarAdapter.getItem(position)))
            }

            view.addEventButton.setOnClickListener {
                navigationController.navigateTo(NavigationState.AddEvent)
            }
            view.addEventText.setOnClickListener {
                navigationController.navigateTo(NavigationState.AddEvent)
            }

            view.swipeRefresh.setOnRefreshListener {
                refresh()
            }

            refresh()

            return view
        }

        private fun refresh() {
            calendarManager.getCalendar()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map { it.filter(itemFilter) }
                    .subscribe({
                        view?.loadingSpinner?.visibility = View.GONE
                        view?.swipeRefresh?.isRefreshing = false
                        calendarAdapter.clear()
                        calendarAdapter.addAll(it)
                        calendarAdapter.notifyDataSetChanged()

                        view?.noEventsView?.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE

                    }, {
                        view?.loadingSpinner?.visibility = View.GONE
                        view?.swipeRefresh?.isRefreshing = false
                        Log.d("CalendarFragment", it.localizedMessage)
                        Toast.makeText(
                                context,
                                "Failed to load calendar events. Please try again later.",
                                Toast.LENGTH_LONG
                        ).show()
                    }, {})
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
