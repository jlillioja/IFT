package io.grandlabs.ift.advocate

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import io.grandlabs.ift.IftApp

import io.grandlabs.ift.R
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_advocate.view.*
import javax.inject.Inject

class AdvocateFragment : Fragment() {

    @Inject lateinit var advocacyProvider: AdvocacyProvider
    @Inject lateinit var advocacyListAdapter: AdvocacyListAdapter

    val loadingSpinner: ProgressBar?
        get() = view?.loadingSpinner

    init {
        IftApp.graph.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_advocate, container, false)

        view.advocacyListView.adapter = advocacyListAdapter

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
