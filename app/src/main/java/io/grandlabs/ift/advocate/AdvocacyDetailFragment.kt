package io.grandlabs.ift.advocate

import android.graphics.drawable.Drawable
import io.grandlabs.ift.*
import io.grandlabs.ift.sharing.SharingHelper
import io.reactivex.Observable
import javax.inject.Inject

class AdvocacyDetailFragment: DetailFragment() {
    @Inject
    lateinit var navigationController: NavigationController

    @Inject lateinit var mSharingHelper: SharingHelper

    lateinit var item: AdvocacyItem

    init {
        IftApp.graph.inject(this)
        navigationController.navigation.subscribe { if (it is NavigationState.AdvocacyDetail) this.item = it.item }
    }

    override fun getItem(): WebItem {
        return item
    }

    override fun getSharingHelper() = mSharingHelper

    override fun getActionBarTitle(): String = "Advocacy Center"

    override fun getTitle(): String? {
        return item.title
    }

    override fun getRedirectUrl(): String? {
        return item.contentUrl
    }

    override fun getBodyHtml(): String? {
        return item.content
    }

    override fun fetchImage(): Observable<Drawable> {
        return Observable.error(Throwable("Doesn't have image"))
    }
}