package io.grandlabs.ift.contact

import android.content.Context
import android.telephony.PhoneNumberUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import io.grandlabs.ift.R
import io.grandlabs.ift.layoutInflater
import kotlinx.android.synthetic.main.contact_list_item.view.*
import javax.inject.Inject
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.net.Uri


class ContactListAdapter
@Inject constructor(context: Context): ArrayAdapter<OfficeItem>(context, R.layout.contact_list_item) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: context.layoutInflater.inflate(R.layout.contact_list_item, parent, false)
        val item = getItem(position)

        view.titleText.text = item.name
        view.addressLine1.text = item.address
        view.addressLine2.text = "${item.city}, ${item.state} {${item.zip}"
        view.phoneNumber.text = PhoneNumberUtils.formatNumber(item.phone)

        view.directionsImage.setOnClickListener {
            openMapsAtAddress("${item.address}, ${item.city} ${item.state} ${item.zip}")
        }

        return view
    }

    private fun openMapsAtAddress(address: String) {
        val intentUri = Uri.parse("geo:0,0?q=$address")
        val mapIntent = Intent(Intent.ACTION_VIEW, intentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(context, mapIntent, null)
    }
}