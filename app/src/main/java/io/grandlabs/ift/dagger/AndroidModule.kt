package io.grandlabs.ift.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import io.grandlabs.ift.IftApp
import io.grandlabs.ift.advocate.AdvocateFragment
import io.grandlabs.ift.calendar.CalendarFragment
import io.grandlabs.ift.contact.ContactFragment
import io.grandlabs.ift.network.IftClient
import io.grandlabs.ift.network.RxServiceCreator
import io.grandlabs.ift.news.NewsFragment
import io.grandlabs.ift.settings.SettingsFragment
import javax.inject.Singleton

@Module
class AndroidModule(private val application: IftApp) {
    @Provides
    fun provideIftClient(): IftClient = RxServiceCreator.createService(IftClient::class.java)

    @Provides
    fun provideContext(): Context = application

    @Provides
    @Singleton
    fun provideNewsFragment(): NewsFragment = NewsFragment()

    @Provides
    @Singleton
    fun provideCalendarFragment(): CalendarFragment = CalendarFragment()

    @Provides
    @Singleton
    fun provideAdvocateFragment(): AdvocateFragment = AdvocateFragment()

    @Provides
    @Singleton
    fun provideContactFragment(): ContactFragment = ContactFragment()

    @Provides
    @Singleton
    fun provideSettingsFragment(): SettingsFragment = SettingsFragment()

}
