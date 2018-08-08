package io.grandlabs.ift

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import io.grandlabs.ift.advocate.AdvocateFragment
import io.grandlabs.ift.calendar.CalendarFragment
import io.grandlabs.ift.contact.ContactFragment
import io.grandlabs.ift.network.NewsItem
import io.grandlabs.ift.news.NewsDetailFragment
import io.grandlabs.ift.news.NewsListFragment
import io.grandlabs.ift.settings.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var newsListFragment: NewsListFragment
    @Inject
    lateinit var newsDetailFragment: NewsDetailFragment
    @Inject
    lateinit var calendarFragment: CalendarFragment
    @Inject
    lateinit var advocateFragment: AdvocateFragment
    @Inject
    lateinit var contactFragment: ContactFragment
    @Inject
    lateinit var settingsFragment: SettingsFragment

    @Inject
    lateinit var navigationController: NavigationController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        IftApp.graph.inject(this)

        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener)

        navigateToNews()
    }

    override fun onResume() {
        super.onResume()

        navigationController.navigation.subscribe {
            when (it) {
                is NavigationState.NewsList -> navigateToNews()
                is NavigationState.NewsDetail -> navigateToNewsDetail(it.item)
                is NavigationState.Calendar -> navigateToCalendar()
                is NavigationState.Advocate -> navigateToAdvocate()
                is NavigationState.Contact -> navigateToContact()
                is NavigationState.Settings -> navigateToSettings()
            }
        }
    }

    private val navigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
        when (it.itemId) {
            R.id.navigation_news -> navigationController.navigateTo(NavigationState.NewsList)
            R.id.navigation_calendar -> navigationController.navigateTo(NavigationState.Calendar)
            R.id.navigation_advocate -> navigationController.navigateTo(NavigationState.Advocate)
            R.id.navigation_contact -> navigationController.navigateTo(NavigationState.Contact)
            R.id.navigation_settings -> navigationController.navigateTo(NavigationState.Settings)
            else -> return@OnNavigationItemSelectedListener false
        }
        true
    }

    private fun navigateToNews() {
        title = "News"
        replaceContentWith(newsListFragment)
    }

    private fun navigateToNewsDetail(item: NewsItem) {
        title = "News"
        replaceContentWith(newsDetailFragment)
    }

    private fun navigateToCalendar() {
        title = "Calendar"
        replaceContentWith(calendarFragment)
    }

    private fun navigateToAdvocate() {
        title = "Advocate"
        replaceContentWith(advocateFragment)
    }

    private fun navigateToContact() {
        title = "Contact"
        replaceContentWith(contactFragment)
    }

    private fun navigateToSettings() {
        title = "Settings"
        replaceContentWith(settingsFragment)
    }

    private fun replaceContentWith(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(contentView.id, fragment)
                .addToBackStack(null)
                .commit()
    }
}
