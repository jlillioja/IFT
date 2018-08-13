package io.grandlabs.ift.news

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import fetchImageFromUrl
import io.grandlabs.ift.R
import io.grandlabs.ift.layoutInflater
import io.grandlabs.ift.network.NewsItem
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.list_item.view.*
import javax.inject.Inject


class NewsAdapter
@Inject constructor(context: Context) : ArrayAdapter<NewsItem>(context, R.layout.list_item) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.layoutInflater
        val view = convertView ?: inflater.inflate(R.layout.list_item, parent, false)

        val newsItem = getItem(position)

        view.titleText.text = newsItem.title
        view.detailText.text = newsItem.summary

        fetchImageFromUrl(newsItem.thumbnailImage)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.image.setImageDrawable(it)
                }, {

                })

        return view
    }

    private val LOG_TAG = this::class.simpleName
}
