package io.grandlabs.ift

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import io.grandlabs.ift.favorites.FavoritesManager
import io.grandlabs.ift.sharing.LinkHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_web_item.view.*

abstract class DetailFragment : IftFragment() {

    abstract fun getItem(): WebItem
    abstract fun getTitle(): String?
    abstract fun getRedirectUrl(): String?
    abstract fun getBodyHtml(): String?
    abstract fun fetchImage(): Observable<Drawable>

    abstract var linkHelper: LinkHelper
    abstract var favoritesManager: FavoritesManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_web_item, container, false)

        listener?.setCurrentlySelectedFragment(this)

        view.favoriteStar.setOnClickListener {
            toggleItemIsFavorite()
        }

        view.titleText.text = getTitle()
        fetchImage().observeOn(AndroidSchedulers.mainThread()).subscribe({
            if (it != null) {
                view.headerImage.visibility = View.VISIBLE
                view.headerImage.setImageDrawable(it)
            } else {
                view.headerImage.visibility = View.GONE
            }
        }, {
            view.headerImage.visibility = View.GONE
        }, {})

        if (getRedirectUrl().isNullOrBlank()) {
            val css = "<head>" +
                    "<meta name=\"viewport\" content=\"initial-scale=1.0\" />" +
                    "<link rel=\"stylesheet\" type=\"text/css\" href=\"webview.css\">" +
                    "</head>"

            val content = "<body>${getBodyHtml()}</body>"

            view.contentWebView.loadData(css + content, "text/html; charset=UTF-8", null)
        } else {
            view.contentWebView.loadUrl(getRedirectUrl())
        }

        view?.shareByFacebook?.setOnClickListener {
            linkHelper.shareLinkByFacebook(getItem().contentUrl, this)
        }

        view?.shareByTwitter?.setOnClickListener {
            linkHelper.shareLinkByTwitter("${getItem().title}\n", getItem().contentUrl)
        }

        view?.shareByEmail?.setOnClickListener {
            linkHelper.shareLinkByEmail(getItem().title, getItem().contentUrl)
        }

        view?.shareBySms?.setOnClickListener {
            linkHelper.shareLinkBySms(getItem().title, getItem().contentUrl)
        }

        return view
    }

    private fun toggleItemIsFavorite() {
        favoritesManager.setFavorite(getItem(), !getItem().isFavorite)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            getItem().isFavorite = it
                            updateStar()
                        },
                        onError = {},
                        onComplete = {}
                )
    }

    fun updateStar() {
        view?.favoriteStar?.setTint(getItem().isFavorite)
    }

    private fun ImageView.setTint(isOn: Boolean) {
        if (isOn) setTintOn() else setTintOff()
    }

    private fun ImageView.setTintOff() {
        colorFilter = null
    }

    private fun ImageView.setTintOn() {
        setColorFilter(ContextCompat.getColor(context, R.color.ift_teal), android.graphics.PorterDuff.Mode.MULTIPLY)
    }

}