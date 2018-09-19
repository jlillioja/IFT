package io.grandlabs.ift.news

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import fetchImageFromUrl
import io.grandlabs.ift.R
import io.grandlabs.ift.layoutInflater
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.list_item.view.*


class NewsAdapter(context: Context) : ArrayAdapter<NewsItem>(context, R.layout.list_item) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView
                ?: context.layoutInflater.inflate(R.layout.list_item, parent, false)

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
