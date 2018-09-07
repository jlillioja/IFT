package io.grandlabs.ift.calendar

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.grandlabs.ift.*
import io.grandlabs.ift.sharing.LinkHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_add_event.view.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class AddEventFragment : IftFragment() {

    @Inject
    lateinit var calendarManager: CalendarManager
    @Inject
    lateinit var navigationController: NavigationController

    @Inject lateinit var linkHelper: LinkHelper

    init {
        IftApp.graph.inject(this)
    }

    var startDate: Calendar? = null
    var endDate: Calendar? = null

    override fun getActionBarTitle(): String = "Create Event"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_event, container, false)

        view.allDaySwitch.setOnCheckedChangeListener { _, isChecked ->
            setFormattedStartDate()
            setFormattedEndDate()
        }

        view.startDate.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showStartDatePicker(DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    if (isAllDay) {
                        startDate = Calendar.getInstance().apply {
                            set(year, month, dayOfMonth)
                        }
                        setFormattedStartDate()
                    } else {
                        showTimePicker(TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                            startDate = Calendar.getInstance().apply {
                                set(year, month, dayOfMonth, hourOfDay, minute)
                            }
                            setFormattedStartDate()
                        })
                    }
                })
            }
        }

        view.endDate.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showEndDatePicker(DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    if (isAllDay) {
                        endDate = Calendar.getInstance().apply {
                            set(year, month, dayOfMonth)
                        }
                        setFormattedEndDate()
                    } else {
                        showTimePicker(TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                            endDate = Calendar.getInstance().apply {
                                set(year, month, dayOfMonth, hourOfDay, minute)
                            }
                            setFormattedEndDate()
                        })
                    }
                })
            }
        }

        view?.allDaySwitch?.isChecked = false

        view.addEventButton.setOnClickListener {
            if (view.inputIsValid()) {
                view.normalizeAddress()

                val addEventRequest = AddEventRequest(
                        view.eventName.text.toString(),
                        view.summary.text.toString(),
                        view.description.text.toString(),
                        view.eventLink.text.toString(),
                        startDate?.formatted() ?: "",
                        endDate?.formatted() ?: "",
                        view.address.text.toString(),
                        view.city.text.toString(),
                        view.state.text.toString(),
                        view.zip.text.toString(),
                        view.allDaySwitch.isChecked
                )

                calendarManager
                        .saveEvent(addEventRequest)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { success ->
                            if (success) {
                                navigationController.navigateTo(NavigationState.Calendar)
                            } else {
                                Toast.makeText(context, "Failed to add event.", Toast.LENGTH_SHORT).show()
                            }
                        }

            }
        }

        return view
    }

    private fun setFormattedStartDate() {
        val pattern = if (isAllDay) "EEE MMM d, yyyy" else "EEE MMM d, yyyy 'at' h:mm a"
        if (startDate != null) {
            val formattedDate = SimpleDateFormat(pattern, Locale.US).format(startDate?.time)
            view?.startDate?.setText(formattedDate)
        } else {
            view?.startDate?.setText("")
        }
    }

    private fun setFormattedEndDate() {
        val pattern = if (isAllDay) "EEE MMM d, yyyy" else "EEE MMM d, yyyy 'at' h:mm a"
        if (endDate != null) {
            val formattedDate = SimpleDateFormat(pattern, Locale.US).format(endDate?.time)
            view?.endDate?.setText(formattedDate)
        } else {
            view?.endDate?.setText("")
        }
    }

    private fun showStartDatePicker(listener: DatePickerDialog.OnDateSetListener) {
        showDatePicker(startDate ?: Calendar.getInstance(), listener)
    }

    private fun showEndDatePicker(listener: DatePickerDialog.OnDateSetListener) {
        showDatePicker(endDate ?: Calendar.getInstance(), listener)
    }

    private fun showDatePicker(calendar: Calendar, listener: DatePickerDialog.OnDateSetListener) {
        DatePickerDialog(context, listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun showTimePicker(listener: TimePickerDialog.OnTimeSetListener) {
        TimePickerDialog(context, listener, 0, 0, false).show()
    }

    private var isAllDay: Boolean
        get() = view?.allDaySwitch?.isChecked ?: false
        set(value) {
            view?.allDaySwitch?.isChecked = value
        }

    private fun View.inputIsValid(): Boolean {
        var result = true
        for (field in listOf(eventName, startDate, summary, description)) {
            if (field.text.isBlank()) {
                field.error = "Field is required."
                result = false
            } else {
                field.error = null
            }
        }

        return result
    }

    private val LOG_TAG = this::class.simpleName

    private fun View.normalizeAddress(): Boolean {

        val normalizedAddress = try {
            val location = "${address.text ?: ""}, ${city.text ?: ""}, ${state.text ?: ""}, ${zip.text ?: ""}"
            Geocoder(context, Locale.US).getFromLocationName(location, 1).firstOrNull()
        } catch (e: Exception) {
            Log.d(LOG_TAG, e.localizedMessage)
            null
        }

        return if (normalizedAddress != null) {
            address.setText("${normalizedAddress.subThoroughfare} ${normalizedAddress.thoroughfare}")
            city.setText(normalizedAddress.locality)
            state.setText(normalizedAddress.adminArea)
            zip.setText(normalizedAddress.postalCode)
            true
        } else {
            Toast.makeText(context, "Please enter a valid address, city, state, and zip code.", Toast.LENGTH_SHORT).show()
            false
        }

    }

    private fun Calendar.formatted() = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).format(this.time)

}