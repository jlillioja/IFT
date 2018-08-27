package io.grandlabs.ift.news

import android.graphics.drawable.Drawable
import fetchImageFromUrl
import io.grandlabs.ift.*
import io.grandlabs.ift.favorites.FavoritesManager
import io.grandlabs.ift.sharing.LinkHelper
import io.reactivex.Observable
import javax.inject.Inject

class NewsDetailFragment : DetailFragment() {

    @Inject
    lateinit var navigationController: NavigationController

    @Inject
    override lateinit var linkHelper: LinkHelper

    @Inject
    override lateinit var favoritesManager: FavoritesManager

    lateinit var item: NewsItem

    init {
        IftApp.graph.inject(this)
        navigationController.navigation.subscribe { if (it is NavigationState.NewsDetail) this.item = it.item }
    }

    override fun getItem(): WebItem = item


    override fun getActionBarTitle(): String = "News"

    override fun getTitle(): String? = item.title

    override fun getRedirectUrl(): String? = item.redirectUrl

    override fun getBodyHtml(): String? = item.content

    override fun fetchImage(): Observable<Drawable> = if (item.thumbnailImage != null) {
        fetchImageFromUrl(item.thumbnailImage)
    } else {
        Observable.error(Throwable("No image"))
    }

//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        val view = inflater.inflate(R.layout.fragment_web_item, container, false)
//
//        val title = view!!.findViewById<TextView>(R.id.titleText)
//        title.text = item?.title
//        val imageView = view!!.findViewById<ImageView>(R.id.headerImage)
//        if (item?.thumbnailImage.isNullOrBlank()) {
//            imageView.visibility = View.GONE
//        } else {
//            fetchImageFromUrl(item?.thumbnailImage!!).subscribe {
//                imageView.visibility = View.VISIBLE
//                imageView.setImageDrawable(it)
//            }
//        }
//        val contentWebView = view.findViewById<WebView>(R.id.contentWebView)
//
//        if (!item?.redirectUrl.isNullOrBlank()) {
//            contentWebView.loadUrl(item?.redirectUrl)
//        } else {
//
//            /*
//                        NSString *css = [NSString stringWithFormat:@"<head>"
//                         "<meta name=\"viewport\" content=\"initial-scale=1.0\" />"
//                         "<link rel=\"stylesheet\" type=\"text/css\" href=\"webview.css\">"
//                         "</head>"];
//        NSString *content = [NSString stringWithFormat:@"<body>%@</body>", self.item.content];
//             */
//
//            val css = "<head>" +
//                    "<meta name=\"viewport\" content=\"initial-scale=1.0\" />" +
//                    "<link rel=\"stylesheet\" type=\"text/css\" href=\"webview.css\">" +
//                    "</head>"
//
//            val content = "<body>${item?.content}</body>"
//
//            Log.d(LOG_TAG, "$css+$content")
//            contentWebView.loadData(css+content, "text/html; charset=UTF-8", null)
//        }
//
//        return view
//    }

    private val LOG_TAG = this::class.simpleName

//    private fun loadNewItem() {
//        if (view != null && item != null) {
//            val title = view!!.findViewById<TextView>(R.id.titleText)
//            title.text = item?.title
//            val contentWebView = view!!.findViewById<WebView>(R.id.contentWebView)
//
//            if (!item?.redirectUrl.isNullOrBlank()) {
//                contentWebView.loadUrl(item?.redirectUrl)
//            } else {
//
//                /*
//                            NSString *css = [NSString stringWithFormat:@"<head>"
//                             "<meta name=\"viewport\" content=\"initial-scale=1.0\" />"
//                             "<link rel=\"stylesheet\" type=\"text/css\" href=\"webview.css\">"
//                             "</head>"];
//            NSString *content = [NSString stringWithFormat:@"<body>%@</body>", self.item.content];
//                 */
//
//                val css = "<head>" +
//                "<meta name=\"viewport\" content=\"initial-scale=1.0\" />" +
//                "<link rel=\"stylesheet\" type=\"text/css\" href=\"webview.css\">" +
//                "</head>"
//
//                val content = "<body>${item?.content}</body>"
//
//                Log.d(LOG_TAG, "$css+$content")
//                contentWebView.loadData(css+content, "text/html; charset=UTF-8", null)
//            }
//        }
//
//
//    }
}