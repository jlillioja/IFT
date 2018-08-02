package io.grandlabs.ift

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener)
    }

    private val navigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
        when (it.itemId) {
            R.id.navigation_news -> {
//                contentView
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_calendar -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_advocate -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_contact -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
}
