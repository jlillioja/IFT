package io.grandlabs.ift.calendar

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import io.grandlabs.ift.R
import io.grandlabs.ift.layoutInflater
import kotlinx.android.synthetic.main.calendar_list_item.view.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class CalendarAdapter
@Inject constructor(context: Context) : ArrayAdapter<CalendarItem>(context, R.layout.list_item) {
    private val LOG_TAG = this::class.simpleName

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.layoutInflater
        val view = convertView ?: inflater.inflate(R.layout.calendar_list_item, parent, false)

        val calendarItem = getItem(position)
        val dateFormat = SimpleDateFormat("MMM'\n'dd", Locale.US)

        view.titleText.text = calendarItem.title
        view.detailText.text = calendarItem.summary
        if (calendarItem.dateFrom != null) {
            view.date.text = dateFormat.format(calendarItem.dateFrom)
        } else {
            view.date.visibility = View.GONE
        }

        return view
    }

}
