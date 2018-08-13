package io.grandlabs.ift

import android.app.Application
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import io.grandlabs.ift.dagger.AndroidModule
import io.grandlabs.ift.dagger.ApplicationComponent
import io.grandlabs.ift.dagger.DaggerApplicationComponent

class IftApp : Application() {

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
    }

}