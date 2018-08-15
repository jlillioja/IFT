package io.grandlabs.ift.advocate

import android.graphics.drawable.Drawable
import io.grandlabs.ift.DetailFragment
import io.grandlabs.ift.IftApp
import io.grandlabs.ift.NavigationController
import io.grandlabs.ift.NavigationState
import io.reactivex.Observable
import javax.inject.Inject

class AdvocacyDetailFragment: DetailFragment() {
    @Inject
    lateinit var navigationController: NavigationController

    var item: AdvocacyItem? = null

    init {
        IftApp.graph.inject(this)
        navigationController.navigation.subscribe { if (it is NavigationState.AdvocacyDetail) this.item = it.item }
    }

    override fun getTitle(): String? {
        return item?.title
    }

    override fun getRedirectUrl(): String? {
        return item?.url
    }

    override fun getBodyHtml(): String? {
        return item?.content
    }

    override fun fetchImage(): Observable<Drawable> {
        return Observable.error(Throwable("Doesn't have image"))
    }
}