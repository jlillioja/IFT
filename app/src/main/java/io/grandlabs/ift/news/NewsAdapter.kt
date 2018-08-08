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
import javax.inject.Inject


class NewsAdapter
@Inject constructor(context: Context) : ArrayAdapter<NewsItem>(context, R.layout.news_list_item) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.layoutInflater
        val view = convertView ?: inflater.inflate(R.layout.news_list_item, parent, false)

        val imageView = view.findViewById<ImageView>(R.id.newsImage)
        val titleTextView = view.findViewById<TextView>(R.id.newsTitle)
        val descriptionTextView = view.findViewById<TextView>(R.id.newsDescription)

        val newsItem = getItem(position)

        fetchImageFromUrl(newsItem.thumbnailImage)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    imageView.setImageDrawable(it)
                }, {

                })
        titleTextView.text = newsItem.title
        descriptionTextView.text = newsItem.summary

        return view
    }

    private val LOG_TAG = this::class.simpleName
}
