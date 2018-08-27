package io.grandlabs.ift.contact

import android.content.Context
import android.telephony.PhoneNumberUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import io.grandlabs.ift.R
import io.grandlabs.ift.layoutInflater
import io.grandlabs.ift.sharing.LinkHelper
import kotlinx.android.synthetic.main.contact_list_item.view.*
import javax.inject.Inject


class ContactListAdapter
@Inject constructor(
        context: Context,
        private val linkHelper: LinkHelper
) : ArrayAdapter<OfficeItem>(context, R.layout.contact_list_item) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView
                ?: context.layoutInflater.inflate(R.layout.contact_list_item, parent, false)

        val item = getItem(position)

        view.titleText.text = item.name
        view.addressLine1.text = item.address
        view.addressLine2.text = "${item.city}, ${item.state} {${item.zip}"
        view.phoneNumber.text = PhoneNumberUtils.formatNumber(item.phone)
        view.phoneNumber.setOnClickListener { linkHelper.openDialerForNumber(item.phone) }

        view.directionsImage.setOnClickListener {
            linkHelper.openMaps("${item.address}, ${item.city} ${item.state} ${item.zip}")
        }

        return view
    }
}