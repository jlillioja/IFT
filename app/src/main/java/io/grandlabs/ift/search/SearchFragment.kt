package io.grandlabs.ift.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import io.grandlabs.ift.*
import io.grandlabs.ift.advocate.AdvocacyItem
import io.grandlabs.ift.news.NewsItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.android.synthetic.main.list_item.view.*
import javax.inject.Inject

class SearchFragment : IftFragment() {
    override fun getActionBarTitle() = "Search"

    @Inject
    lateinit var searchProvider: SearchProvider

    @Inject
    lateinit var navigationController: NavigationController

    @Inject
    lateinit var searchResultsAdapter: SearchResultsAdapter

    init {
        IftApp.graph.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        listener?.setCurrentlySelectedFragment(this)

        view.searchResultsListView.adapter = searchResultsAdapter
        view.searchResultsListView.setOnItemClickListener { _, _, position, _ ->
            val item = searchResultsAdapter.getItem(position)
            when (item) {
                is NewsItem -> navigationController.navigateTo(NavigationState.NewsDetail(item))
                is AdvocacyItem -> navigationController.navigateTo(NavigationState.AdvocacyDetail(item))
            }
        }

        view.searchButton.setOnClickListener {
            loadResults(view.searchBar.query.toString())
        }

        return view
    }

    private fun loadResults(query: String) {
        view?.loadingSpinner?.visibility = View.VISIBLE
        Observables.combineLatest(
                searchProvider.getFilteredNewsItems(query),
                searchProvider.getFilteredAdvocacyItems(query)
        ) { newsList, advocacyList -> newsList.plus(advocacyList) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            view?.loadingSpinner?.visibility = View.GONE
                            searchResultsAdapter.clear()
                            searchResultsAdapter.addAll(it)
                            searchResultsAdapter.notifyDataSetChanged()
                        },
                        onError = {
                            view?.loadingSpinner?.visibility = View.GONE
                            Toast.makeText(context, "Failed to load.", Toast.LENGTH_SHORT).show()
                            Log.d(LOG_TAG, it.localizedMessage)
                        }
                )
    }

    private val LOG_TAG = this::class.simpleName

    class SearchResultsAdapter @Inject constructor(context: Context) : ArrayAdapter<WebItem>(context, R.layout.list_item) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val inflater = context.layoutInflater

            val view = convertView ?: inflater.inflate(R.layout.list_item, parent, false)

            val item = getItem(position)

            view.titleText.text = item.title
            view.detailText.text = item.summary
            val associatedImage = item.associatedImage(context)
            if (associatedImage != null) {
                associatedImage
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(
                                onNext = {
                                    Log.d(LOG_TAG, "Setting associated image for item ${item.title} on item $position")
                                    view.image.setImageDrawable(it)
                                },
                                onError = {
                                    Log.d(LOG_TAG, it.localizedMessage)
                                    view.image.visibility = View.GONE
                                },
                                onComplete = {}
                        )
            } else {
                view.image.visibility = View.GONE
            }

            return view
        }

        private val LOG_TAG = this::class.simpleName
    }
}