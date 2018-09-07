package io.grandlabs.ift

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.View
import io.grandlabs.ift.advocate.AdvocacyDetailFragment
import io.grandlabs.ift.advocate.AdvocacyFragment
import io.grandlabs.ift.calendar.AddEventFragment
import io.grandlabs.ift.calendar.CalendarFragment
import io.grandlabs.ift.calendar.CalendarItem
import io.grandlabs.ift.contact.ContactFragment
import io.grandlabs.ift.favorites.FavoritesFragment
import io.grandlabs.ift.invite.InviteFragment
import io.grandlabs.ift.news.CalendarDetailFragment
import io.grandlabs.ift.news.NewsDetailFragment
import io.grandlabs.ift.news.NewsItem
import io.grandlabs.ift.news.NewsListFragment
import io.grandlabs.ift.search.SearchFragment
import io.grandlabs.ift.settings.SettingsFragment
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.action_bar_layout.view.*
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), IftFragment.OnFragmentInteractionListener {

    @Inject
    lateinit var newsListFragment: NewsListFragment
    @Inject
    lateinit var newsDetailFragment: NewsDetailFragment
    @Inject
    lateinit var calendarFragment: CalendarFragment
    @Inject
    lateinit var calendarDetailFragment: CalendarDetailFragment
    @Inject
    lateinit var advocacyFragment: AdvocacyFragment
    @Inject
    lateinit var advocacyDetailFragment: AdvocacyDetailFragment
    @Inject
    lateinit var contactFragment: ContactFragment
    @Inject
    lateinit var inviteFragment: InviteFragment
    @Inject
    lateinit var settingsFragment: SettingsFragment
    @Inject
    lateinit var searchFragment: SearchFragment
    @Inject
    lateinit var favoritesFragment: FavoritesFragment
    @Inject
    lateinit var addEventFragment: AddEventFragment

    @Inject
    lateinit var navigationController: NavigationController

    private val actionBar: View
        get() = supportActionBar!!.customView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        IftApp.graph.inject(this)

        setContentView(R.layout.activity_main)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(R.layout.action_bar_layout)

        navigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener)

        actionBar.search.setOnClickListener {
            navigateToSearch()
        }

        actionBar.settings.setOnClickListener {
            navigateToSettings()
        }

        actionBar.favorite.setOnClickListener {
            navigateToFavorites()
        }

        actionBar.plus.setOnClickListener {
            navigateToAddEvent()
        }

        navigateToNews()
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

    private var navigationSubscription: Disposable? = null

    override fun onResumeFragments() {
        super.onResumeFragments()

        navigationSubscription = navigationController.navigation.subscribe {
            when (it) {
                is NavigationState.NewsList -> navigateToNews()
                is NavigationState.NewsDetail -> navigateToNewsDetail(it.item)
                is NavigationState.Calendar -> navigateToCalendar()
                is NavigationState.CalendarDetail -> navigateToCalendarDetail(it.item)
                is NavigationState.AddEvent -> navigateToAddEvent()
                is NavigationState.Advocacy -> navigateToAdvocacyCenter()
                is NavigationState.AdvocacyDetail -> navigateToAdvocacyDetail()
                is NavigationState.Contact -> navigateToContact()
                is NavigationState.Invite -> navigateToInvite()
                is NavigationState.Settings -> navigateToSettings()
            }
        }
    }

    override fun setCurrentlySelectedFragment(fragment: IftFragment) {
        actionBar.titleText.text = fragment.getActionBarTitle()
        val menuItem = navigation.menu.findItem(getNavigationIdForFragment(fragment))
        menuItem?.isChecked = true
        // TODO: unselect for null?
    }

    private fun getNavigationIdForFragment(fragment: IftFragment): Int {
        // TODO: switch on navigation state
        return when (fragment) {
            is NewsListFragment -> R.id.navigation_news
            is NewsDetailFragment -> R.id.navigation_news
            is CalendarFragment -> R.id.navigation_calendar
            is CalendarFragment.CalendarListFragment -> R.id.navigation_calendar
            is AdvocacyFragment -> R.id.navigation_advocate
            is AdvocacyDetailFragment -> R.id.navigation_advocate
            is ContactFragment -> R.id.navigation_contact
            is InviteFragment -> R.id.navigation_invite
            else -> 0
        }
    }

    override fun onPause() {
        super.onPause()

        navigationSubscription?.dispose()
    }

    private val navigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
        when (it.itemId) {
            R.id.navigation_news -> navigationController.navigateTo(NavigationState.NewsList)
            R.id.navigation_calendar -> navigationController.navigateTo(NavigationState.Calendar)
            R.id.navigation_advocate -> navigationController.navigateTo(NavigationState.Advocacy)
            R.id.navigation_contact -> navigationController.navigateTo(NavigationState.Contact)
            R.id.navigation_invite -> navigationController.navigateTo(NavigationState.Invite)
            else -> return@OnNavigationItemSelectedListener false
        }
        true
    }

    private fun navigateToNews() {
        replaceContentWith(newsListFragment)
    }

    private fun navigateToNewsDetail(item: NewsItem) {
        replaceContentWith(newsDetailFragment)
    }

    private fun navigateToCalendar() {
        replaceContentWith(calendarFragment)
        actionBar.plus.visibility = View.VISIBLE
    }

    private fun navigateToCalendarDetail(item: CalendarItem) {
        replaceContentWith(calendarDetailFragment)
    }

    private fun navigateToAdvocacyCenter() {
        replaceContentWith(advocacyFragment)
    }

    private fun navigateToAdvocacyDetail() {
        replaceContentWith(advocacyDetailFragment)
    }

    private fun navigateToContact() {
        replaceContentWith(contactFragment)
    }

    private fun navigateToInvite() {
        replaceContentWith(inviteFragment)
    }

    private fun navigateToSettings() {
        replaceContentWith(settingsFragment)
    }

    private fun navigateToSearch() {
        replaceContentWith(searchFragment)
    }

    private fun navigateToFavorites() {
        replaceContentWith(favoritesFragment)
    }

    private fun navigateToAddEvent() {
        replaceContentWith(addEventFragment)
        actionBar.plus.visibility = View.VISIBLE
    }

    private fun replaceContentWith(fragment: Fragment) {
        setActionBarToDefault()
        if (!fragment.isAdded) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(contentView.id, fragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss()
        }
    }

    private fun setActionBarToDefault() {
        actionBar.plus.visibility = View.GONE
    }

}
//    }
//        }
//            else -> super.onOptionsItemSelected(item)
//            }
//                true
//                navigateToFavorites()
//            R.id.favorite -> {
//            }
//                true
//                navigateToSearch()
//            R.id.search -> {
//            }
//                true
//                navigateToSearch()
//            R.id.settings -> {
//        return when (item?.itemId) {
//    }
//        return true
//
//        menu?.findItem(R.id.favorite)?.icon?.setColorFilter(ContextCompat.getColor(this, R.color.light_neutral_grey), PorterDuff.Mode.MULTIPLY)
//
//        menuInflater.inflate(R.menu.action_bar, menu)