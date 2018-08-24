package io.grandlabs.ift

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_web_item.view.*

abstract class DetailFragment : IftFragment() {

    abstract override fun getActionBarTitle(): String
    abstract fun getTitle(): String?
    abstract fun getRedirectUrl(): String?
    abstract fun getBodyHtml(): String?
    abstract fun fetchImage(): Observable<Drawable>

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

        return view
    }

}