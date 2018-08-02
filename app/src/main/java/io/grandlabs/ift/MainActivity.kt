package io.grandlabs.ift

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import io.grandlabs.ift.advocate.AdvocateFragment
import io.grandlabs.ift.calendar.CalendarFragment
import io.grandlabs.ift.contact.ContactFragment
import io.grandlabs.ift.news.NewsFragment
import io.grandlabs.ift.settings.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var newsFragment: NewsFragment
    @Inject lateinit var calendarFragment: CalendarFragment
    @Inject lateinit var advocateFragment: AdvocateFragment
    @Inject lateinit var contactFragment: ContactFragment
    @Inject lateinit var settingsFragment: SettingsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        IftApp.graph.inject(this)

        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener)

        navigateToNews()
    }

    private val navigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
        when (it.itemId) {
            R.id.navigation_news -> navigateToNews()
            R.id.navigation_calendar -> navigateToCalendar()
            R.id.navigation_advocate -> navigateToAdvocate()
            R.id.navigation_contact -> navigateToContact()
            R.id.navigation_settings -> navigateToSettings()
            else -> return@OnNavigationItemSelectedListener false
        }
        true
    }

    private fun navigateToNews() {
        title = "News"
        replaceContentWith(newsFragment)
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
        supportFragmentManager.beginTransaction().replace(contentView.id, fragment).commit()
    }
}
