package io.grandlabs.ift.advocate

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import io.grandlabs.ift.IftApp
import io.grandlabs.ift.NavigationController
import io.grandlabs.ift.NavigationState

import io.grandlabs.ift.R
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_advocate.view.*
import javax.inject.Inject

class AdvocacyFragment : Fragment() {

    @Inject lateinit var advocacyProvider: AdvocacyProvider
    @Inject lateinit var advocacyListAdapter: AdvocacyListAdapter
    @Inject lateinit var navigationController: NavigationController

    val loadingSpinner: ProgressBar?
        get() = view?.loadingSpinner

    init {
        IftApp.graph.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_advocate, container, false)

        view.advocacyListView.adapter = advocacyListAdapter
        view.advocacyListView.setOnItemClickListener { _, _, position, _ ->
            val item = advocacyListAdapter.getItem(position)
            navigationController.navigateTo(NavigationState.AdvocacyDetail(item))
        }

        view.swipeRefresh?.setOnRefreshListener {
            refresh()
        }

        refresh()

        return view
    }

    private fun refresh() {
        advocacyProvider
                .getAdvocacyItems()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    loadingSpinner?.visibility = View.GONE
                    view?.swipeRefresh?.isRefreshing = false
                    advocacyListAdapter.clear()
                    advocacyListAdapter.addAll(it)
                    advocacyListAdapter.notifyDataSetChanged()
                }, {
                    loadingSpinner?.visibility = View.GONE
                    view?.swipeRefresh?.isRefreshing = false
                    Toast.makeText(context, "Failed to load.", Toast.LENGTH_SHORT).show()
                }, {})
    }

}