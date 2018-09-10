package io.grandlabs.ift.favorites

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
import io.grandlabs.ift.calendar.CalendarItem
import io.grandlabs.ift.news.NewsItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.calendar_list_item.view.date
import kotlinx.android.synthetic.main.fragment_favorites.view.*
import kotlinx.android.synthetic.main.list_item.view.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class FavoritesFragment : IftFragment() {
    override fun getActionBarTitle() = "favorites"

    @Inject
    lateinit var favoritesManager: FavoritesManager

    @Inject
    lateinit var navigationController: NavigationController

    @Inject
    lateinit var searchResultsAdapter: SearchResultsAdapter

    init {
        IftApp.graph.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)

        listener?.setCurrentlySelectedFragment(this)

        view.favoritesResultsListView.adapter = searchResultsAdapter
        view.favoritesResultsListView.setOnItemClickListener { _, _, position, _ ->
            val item = searchResultsAdapter.getItem(position)
            when (item) {
                is NewsItem -> navigationController.navigateTo(NavigationState.NewsDetail(item))
                is AdvocacyItem -> navigationController.navigateTo(NavigationState.AdvocacyDetail(item))
                is CalendarItem -> navigationController.navigateTo(NavigationState.CalendarDetail(item))
            }
        }

        view.swipeRefresh.setOnRefreshListener {
            loadResults()
        }

        return view
    }

    override fun onResume() {
        super.onResume()

        loadResults()
    }

    private fun loadResults() {
        favoritesManager
                .getAllFavorites()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = this::displayItems,
                        onError = {
                            displayErrorRetrievingResults()
                            Log.d(LOG_TAG, it.localizedMessage)
                        }
                )
    }

    private fun displayItems(items: List<WebItem>) {
        view?.loadingSpinner?.visibility = View.GONE
        view?.swipeRefresh?.isRefreshing = false
        if (items.isEmpty()) {
            view?.noResults?.visibility = View.VISIBLE
        } else {
            view?.noResults?.visibility = View.GONE
        }
        searchResultsAdapter.clear()
        searchResultsAdapter.addAll(items)
        searchResultsAdapter.notifyDataSetChanged()
    }

    private fun displayErrorRetrievingResults() {
        view?.loadingSpinner?.visibility = View.GONE
        view?.swipeRefresh?.isRefreshing = false
        view?.noResults?.visibility = View.VISIBLE
        Toast.makeText(context, "Failed to load.", Toast.LENGTH_SHORT).show()
    }

    private val LOG_TAG = this::class.simpleName

    class SearchResultsAdapter @Inject constructor(context: Context) : ArrayAdapter<WebItem>(context, R.layout.list_item) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val inflater = context.layoutInflater

            val item = getItem(position)

            val view = if (item is CalendarItem) {
                inflater.inflate(R.layout.calendar_list_item, parent, false)
            } else {
                inflater.inflate(R.layout.list_item, parent, false)
            }

            view.titleText.text = item.title
            view.detailText.text = item.summary
            val associatedImage = item.associatedImage(context)
            if (associatedImage != null) {
                associatedImage
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(
                                onNext = {
                                    Log.d(LOG_TAG+"associatedImage", "Setting associated image for item ${item.title} on item $position")
                                    view.image.setImageDrawable(it)
                                },
                                onError = {
                                    Log.d(LOG_TAG+"associatedImage", it.localizedMessage)
                                    view.image.visibility = View.GONE
                                },
                                onComplete = {}
                        )
            } else {
                view.image?.visibility = View.GONE
            }

            if (item is CalendarItem) {
                val dateFormat = SimpleDateFormat("MMM'\n'dd", Locale.US)

                view.titleText.text = item.title
                view.detailText.text = item.summary
                if (item.dateFrom != null) {
                    view.date.text = dateFormat.format(item.dateFrom)
                } else {
                    view.date.visibility = View.GONE
                }
            }

            return view
        }

        private val LOG_TAG = this::class.simpleName
    }
}