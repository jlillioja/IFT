package io.grandlabs.ift

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.grandlabs.ift.sharing.LinkHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_web_item.view.*

abstract class DetailFragment : IftFragment() {

    abstract fun getItem(): WebItem
    abstract fun getTitle(): String?
    abstract fun getRedirectUrl(): String?
    abstract fun getBodyHtml(): String?
    abstract fun fetchImage(): Observable<Drawable>

    abstract fun getLinkHelper(): LinkHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_web_item, container, false)

        listener?.setCurrentlySelectedFragment(this)

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
            getLinkHelper().shareLinkByFacebook(getItem().contentUrl, this)
        }

        view?.shareByTwitter?.setOnClickListener {
            getLinkHelper().shareLinkByTwitter("${getItem().title}\n", getItem().contentUrl)
        }

        view?.shareByEmail?.setOnClickListener {
            getLinkHelper().shareLinkByEmail(getItem().title, getItem().contentUrl)
        }

        view?.shareBySms?.setOnClickListener {
            getLinkHelper().shareLinkBySms(getItem().title, getItem().contentUrl)
        }

        return view
    }

}