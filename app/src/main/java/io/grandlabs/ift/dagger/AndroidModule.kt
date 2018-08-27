package io.grandlabs.ift.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import io.grandlabs.ift.IftApp
import io.grandlabs.ift.NavigationController
import io.grandlabs.ift.NavigationControllerImpl
import io.grandlabs.ift.advocate.AdvocacyDetailFragment
import io.grandlabs.ift.advocate.AdvocacyFragment
import io.grandlabs.ift.calendar.CalendarFragment
import io.grandlabs.ift.contact.ContactFragment
import io.grandlabs.ift.favorites.FavoritesFragment
import io.grandlabs.ift.invite.InviteFragment
import io.grandlabs.ift.network.ApiServiceCreator
import io.grandlabs.ift.network.IftClient
import io.grandlabs.ift.news.CalendarDetailFragment
import io.grandlabs.ift.news.NewsDetailFragment
import io.grandlabs.ift.news.NewsListFragment
import io.grandlabs.ift.search.SearchFragment
import io.grandlabs.ift.settings.SettingsFragment
import javax.inject.Singleton

@Module
class AndroidModule(private val application: IftApp) {
    @Provides
    fun provideIftClient(): IftClient = ApiServiceCreator.createService(IftClient::class.java)

    @Provides
    fun provideContext(): Context = application

    @Provides
    @Singleton
    fun provideNewsFragment(): NewsListFragment = NewsListFragment()

    @Provides
    @Singleton
    fun provideNewsDetailFragment(): NewsDetailFragment = NewsDetailFragment()

    @Provides
    @Singleton
    fun provideCalendarFragment(): CalendarFragment = CalendarFragment()

    @Provides
    @Singleton
    fun provideCalendarDetailFragment(): CalendarDetailFragment = CalendarDetailFragment()

    @Provides
    @Singleton
    fun provideAdvocacyFragment(): AdvocacyFragment = AdvocacyFragment()

    @Provides
    @Singleton
    fun provideAdvocacyDetailFragment():AdvocacyDetailFragment = AdvocacyDetailFragment()

    @Provides
    @Singleton
    fun provideContactFragment(): ContactFragment = ContactFragment()

    @Provides
    @Singleton
    fun provideInviteFragment(): InviteFragment = InviteFragment()

    @Provides
    @Singleton
    fun provideSettingsFragment(): SettingsFragment = SettingsFragment()

    @Provides
    @Singleton
    fun provideSearchFragment(): SearchFragment = SearchFragment()

    @Provides
    @Singleton
    fun provideFavoritesFragment(): FavoritesFragment = FavoritesFragment()

    @Provides
    @Singleton
    fun provideNavigationController(): NavigationController = NavigationControllerImpl()
}
