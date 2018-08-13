package io.grandlabs.ift.invite

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.grandlabs.ift.IftApp
import io.grandlabs.ift.R
import io.grandlabs.ift.calendar.CalendarFragment
import javax.inject.Inject

class InviteFragment
@Inject constructor(): Fragment() {

    init {
        IftApp.graph.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_invite, container, false)

        return view
    }
}