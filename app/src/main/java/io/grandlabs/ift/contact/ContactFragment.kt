package io.grandlabs.ift.contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import io.grandlabs.ift.IftApp
import io.grandlabs.ift.IftFragment

import io.grandlabs.ift.R
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_contact.view.*
import javax.inject.Inject

class ContactFragment : IftFragment() {

    @Inject
    lateinit var contactListAdapter: ContactListAdapter

    @Inject lateinit var contactProvider: ContactProvider

    val loadingSpinner: ProgressBar?
        get() = view?.loadingSpinner

    init {
        IftApp.graph.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_contact, container, false)

        listener?.setCurrentlySelectedFragment(this)

        view.contactListView.adapter = contactListAdapter

        view.swipeRefresh?.setOnRefreshListener {
            refresh()
        }

        refresh()

        return view
    }

    override fun getActionBarTitle(): String = "contact"

    private fun refresh() {
        contactProvider
                .getOfficeItems()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    loadingSpinner?.visibility = View.GONE
                    view?.swipeRefresh?.isRefreshing = false
                    contactListAdapter.clear()
                    contactListAdapter.addAll(it)
                    contactListAdapter.notifyDataSetChanged()
                }, {
                    loadingSpinner?.visibility = View.GONE
                    view?.swipeRefresh?.isRefreshing = false
                    Toast.makeText(context, "Failed to load.", Toast.LENGTH_SHORT).show()
                }, {})
    }

}
