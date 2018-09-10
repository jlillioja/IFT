package io.grandlabs.ift.advocate

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.grandlabs.ift.*
import io.grandlabs.ift.favorites.FavoritesManager
import io.grandlabs.ift.sharing.LinkHelper
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_web_item.view.*
import javax.inject.Inject

class AdvocacyDetailFragment: DetailFragment() {
    @Inject
    lateinit var navigationController: NavigationController

    @Inject
    override lateinit var linkHelper: LinkHelper

    @Inject
    override lateinit var favoritesManager: FavoritesManager

    lateinit var item: AdvocacyItem

    init {
        IftApp.graph.inject(this)
        navigationController.navigation.subscribe { if (it is NavigationState.AdvocacyDetail) this.item = it.item }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        view?.takeActionSection?.visibility = View.VISIBLE
        view?.takeActionButton?.setOnClickListener { item.takeAction(linkHelper) }

        return view
    }

    override fun getItem(): WebItem {
        return item
    }

    override fun getActionBarTitle(): String = "advocacy center"

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