package io.grandlabs.ift.advocate

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import io.grandlabs.ift.R
import io.grandlabs.ift.layoutInflater
import kotlinx.android.synthetic.main.list_item.view.*
import javax.inject.Inject

class AdvocacyListAdapter
@Inject constructor(context: Context) : ArrayAdapter<AdvocacyItem>(context, R.layout.list_item) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: context.layoutInflater.inflate(R.layout.list_item, parent, false)

        val advocacyItem = getItem(position)

        view.titleText.text = advocacyItem.title
        view.detailText.text = advocacyItem.summary

        view.image.setImageDrawable(advocacyItem.associatedImage(context))

        return view
    }
}