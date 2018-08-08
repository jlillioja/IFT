package io.grandlabs.ift.news

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.ProgressBar
import io.grandlabs.ift.IftApp
import io.grandlabs.ift.NavigationController
import io.grandlabs.ift.NavigationState
import io.grandlabs.ift.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class NewsListFragment : Fragment() {

    @Inject
    lateinit var newsProvider: NewsProvider

    @Inject
    lateinit var newsAdapter: NewsAdapter

    @Inject
    lateinit var navigationController: NavigationController

    val LOG_TAG: String = this::class.simpleName!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        IftApp.graph.inject(this)

        val view = inflater.inflate(R.layout.fragment_news, container, false)
        val newsListView = view.findViewById<ListView>(R.id.newsListView)
        newsListView.setOnItemClickListener { _, _, position, _ ->
            navigationController.navigateTo(NavigationState.NewsDetail(newsAdapter.getItem(position)))
        }
        val spinner = view.findViewById<ProgressBar>(R.id.loadingSpinner)

        newsProvider.getNews()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    spinner.visibility = View.GONE
                    newsAdapter.clear()
                    newsAdapter.addAll(it.items)
                    newsAdapter.notifyDataSetChanged()
                }

        newsListView.adapter = newsAdapter

        return view
    }
}
