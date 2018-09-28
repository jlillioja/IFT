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

    private val customActionBar: View
        get() = supportActionBar!!.customView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        IftApp.graph.inject(this)

        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener)

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
                is NavigationState.Back -> navigateBackwards()
            }
        }
    }

    override fun setCurrentlySelectedFragment(fragment: IftFragment) {
        configureActionBarForSelectedFragment(fragment)

        val menuItem = navigation.menu.findItem(getNavigationIdForFragment(fragment))
        menuItem?.isChecked = true
        // TODO: unselect for null?
    }

    private fun configureActionBarForSelectedFragment(fragment: IftFragment) {
        setActionBarToDefault()

        customActionBar.titleText.text = fragment.getActionBarTitle()
        when (fragment) {
            is CalendarFragment.LocalCalendarListFragment -> {
                customActionBar.plus.visibility = View.VISIBLE
            }
            is SettingsFragment -> {
                customActionBar.save.apply {
                    visibility = View.VISIBLE
                    setOnClickListener {
                        fragment.onSaveClicked()
                    }
                }
                customActionBar.cancel.apply {
                    visibility = View.VISIBLE
                    setOnClickListener {
                        fragment.onCancelClicked()
                    }
                }
                customActionBar.search.visibility = View.GONE
                customActionBar.settings.visibility = View.GONE
                customActionBar.favorite.visibility = View.GONE
            }
        }
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
    }

    private fun navigateBackwards() {
        supportFragmentManager.popBackStackImmediate()
    }

    private fun replaceContentWith(fragment: Fragment) {
        if (!fragment.isAdded) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(contentView.id, fragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss()
        }
    }

    private fun setActionBarToDefault() {
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(R.layout.action_bar_layout)


        customActionBar.search.setOnClickListener {
            navigateToSearch()
        }

        customActionBar.settings.setOnClickListener {
            navigateToSettings()
        }

        customActionBar.favorite.setOnClickListener {
            navigateToFavorites()
        }

        customActionBar.plus.setOnClickListener {
            navigateToAddEvent()
        }
    }
}