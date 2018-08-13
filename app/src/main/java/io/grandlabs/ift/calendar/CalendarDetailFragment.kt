package io.grandlabs.ift.news

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import io.grandlabs.ift.IftApp
import io.grandlabs.ift.NavigationController
import io.grandlabs.ift.NavigationState
import io.grandlabs.ift.R
import io.grandlabs.ift.network.CalendarItem
import kotlinx.android.synthetic.main.fragment_web_item.view.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CalendarDetailFragment: Fragment() {

    @Inject
    lateinit var navigationController: NavigationController

    var item: CalendarItem? = null

    init {
        IftApp.graph.inject(this)
        navigationController.navigation
                .subscribe { if (it is NavigationState.CalendarDetail) this.item = it.item }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_web_item, container, false)

        val title = view.titleText
        title.text = item?.title

        val date = item?.dateFrom ?: item?.dateTo
        if (date != null) {
            view.subtitleText.visibility = View.VISIBLE
            view.subtitleText.text = SimpleDateFormat("MMMM dd, yyyy", Locale.US)
                    .format(date)
        }

        val contentWebView = view.findViewById<WebView>(R.id.contentWebView)

        val css = "<head>" +
                "<meta name=\"viewport\" content=\"initial-scale=1.0\" />" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"webview.css\">" +
                "</head>"

        val content = "<body>${item?.description}</body>"

        Log.d(LOG_TAG, "$css+$content")
        contentWebView.loadData(css + content, "text/html; charset=UTF-8", null)

        return view
    }

    private val LOG_TAG = this::class.simpleName

}