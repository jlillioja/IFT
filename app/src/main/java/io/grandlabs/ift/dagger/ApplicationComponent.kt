package io.grandlabs.ift.dagger

import dagger.Component
import io.grandlabs.ift.IftApp
import io.grandlabs.ift.MainActivity
import io.grandlabs.ift.login.SignInActivity
import io.grandlabs.ift.news.NewsFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidModule::class])
interface ApplicationComponent {
    fun inject(signInActivity: SignInActivity)
    fun inject(application: IftApp)
    fun inject(mainActivity: MainActivity)
    fun inject(newsFragment: NewsFragment)
}