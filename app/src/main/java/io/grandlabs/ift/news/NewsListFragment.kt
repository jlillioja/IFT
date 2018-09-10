package io.grandlabs.ift.news

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.grandlabs.ift.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_news.view.*
import javax.inject.Inject

class NewsListFragment : IftFragment() {

    @Inject
    lateinit var newsProvider: NewsProvider

    @Inject
    lateinit var newsAdapter: NewsAdapter

    @Inject
    lateinit var navigationController: NavigationController

    val disposables: CompositeDisposable = CompositeDisposable()

    val LOG_TAG: String = this::class.simpleName!!

    init {
        IftApp.graph.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_news, container, false)

        listener?.setCurrentlySelectedFragment(this)

        view.newsListView.setOnItemClickListener { _, _, position, _ ->
            navigationController.navigateTo(NavigationState.NewsDetail(newsAdapter.getItem(position)))
        }

        view.swipeRefresh?.setOnRefreshListener {
            refresh()
        }

        view.newsListView.adapter = newsAdapter

        refresh()

        return view
    }

    override fun onDestroy() {
        disposables.dispose()

        super.onDestroy()
    }

    override fun getActionBarTitle(): String = "news"

    private fun refresh() {
        newsProvider.getNews()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.loadingSpinner?.visibility = View.GONE
                    view?.swipeRefresh?.isRefreshing = false
                    newsAdapter.clear()
                    newsAdapter.addAll(it.items)
                    newsAdapter.notifyDataSetChanged()
                }, {
                    view?.loadingSpinner?.visibility = View.GONE
                    view?.swipeRefresh?.isRefreshing = false
                    Toast.makeText(context, "Failed to load.", Toast.LENGTH_SHORT).show()
                    Log.d(LOG_TAG, it.localizedMessage)
                }, {}).addTo(disposables)
    }
}
