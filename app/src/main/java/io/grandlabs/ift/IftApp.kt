package io.grandlabs.ift

import android.support.multidex.MultiDexApplication
import android.util.Log
import com.crashlytics.android.Crashlytics
import com.twitter.sdk.android.core.DefaultLogger
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterConfig
import io.fabric.sdk.android.Fabric
import io.grandlabs.ift.dagger.AndroidModule
import io.grandlabs.ift.dagger.ApplicationComponent
import io.grandlabs.ift.dagger.DaggerApplicationComponent


class IftApp : MultiDexApplication() {

    companion object {
        @JvmStatic lateinit var graph: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()
        graph = DaggerApplicationComponent.builder()
                .androidModule(AndroidModule(this))
                .build()
        graph.inject(this)

        if (!BuildConfig.DEBUG) {
            Fabric.with(this, Crashlytics())
        }

        val config = TwitterConfig.Builder(this)
                .logger(DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(TwitterAuthConfig("1kZAl4092FLenpEzMH8OyQ", "q3dmTVk8NLtsQ2FQLKcBiNuCG64wb8usYEFGQk12g"))
                .debug(BuildConfig.DEBUG)
                .build()
        Twitter.initialize(config)
    }

}