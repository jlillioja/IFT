package io.grandlabs.ift.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import io.grandlabs.ift.IftApp
import io.grandlabs.ift.network.IftClient
import io.grandlabs.ift.network.RxServiceCreator

@Module
class AndroidModule(private val application: IftApp) {
    @Provides
    fun provideIftClient(): IftClient = RxServiceCreator.createService(IftClient::class.java)

    @Provides
    fun provideContext(): Context = application

}
