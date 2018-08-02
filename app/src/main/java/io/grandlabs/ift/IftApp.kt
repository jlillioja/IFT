package io.grandlabs.ift

import android.app.Application
import io.grandlabs.ift.dagger.AndroidModule
import io.grandlabs.ift.dagger.ApplicationComponent
import io.grandlabs.ift.dagger.DaggerApplicationComponent

//import io.grandlabs.ift.dagger.AndroidModule
//import io.grandlabs.ift.dagger.ApplicationComponent

//import io.grandlabs.ift.dagger.AndroidModule
//import io.grandlabs.ift.dagger.ApplicationComponent

class IftApp : Application() {

    companion object {
        //platformStatic allow access it from java code
        @JvmStatic lateinit var graph: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()
        graph = DaggerApplicationComponent.builder()
                .androidModule(AndroidModule(this))
                .build()
        graph.inject(this)
    }

}