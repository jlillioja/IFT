package io.grandlabs.ift.dagger

import dagger.Component
import io.grandlabs.ift.IftApp
import io.grandlabs.ift.MainActivity
import io.grandlabs.ift.advocate.AdvocateFragment
import io.grandlabs.ift.calendar.CalendarFragment
import io.grandlabs.ift.invite.InviteFragment
import io.grandlabs.ift.login.SignInActivity
import io.grandlabs.ift.news.CalendarDetailFragment
import io.grandlabs.ift.news.NewsDetailFragment
import io.grandlabs.ift.news.NewsListFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidModule::class])
interface ApplicationComponent {
    fun inject(signInActivity: SignInActivity)
    fun inject(application: IftApp)
    fun inject(mainActivity: MainActivity)
    fun inject(newsListFragment: NewsListFragment)
    fun inject(newsDetailFragment: NewsDetailFragment)
    fun inject(calendarFragment: CalendarFragment)
    fun inject(calendarListFragment: CalendarFragment.CalendarListFragment)
    fun inject(inviteFragment: InviteFragment)
    fun inject(calendarDetailFragment: CalendarDetailFragment)
    fun inject(advocateFragment: AdvocateFragment)
}