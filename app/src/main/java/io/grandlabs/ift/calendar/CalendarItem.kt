package io.grandlabs.ift.calendar

import android.content.Context
import android.graphics.drawable.Drawable
import com.google.gson.annotations.SerializedName
import io.grandlabs.ift.WebItem
import io.reactivex.Observable
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

data class CalendarResult(
        @SerializedName("Items") val items: List<CalendarItem>
)

data class CalendarItem(
        @SerializedName("Title") override val title: String,
        @SerializedName("Summary") override val summary: String,
        @SerializedName("ID") override val contentId: String,
        @SerializedName("Description") val description: String,
        @SerializedName("DateFrom") val dateFromString: String,
        @SerializedName("DateTo") val dateToString: String,
        @SerializedName("CalendarID") val calenderID: Int,
        @SerializedName("Address") val address: String,
        @SerializedName("City") val city: String,
        @SerializedName("State") val state: String,
        @SerializedName("Zip") val zip: String,
        @SerializedName("URL") val url: String,
        @SerializedName("MemberID") val ownerMemberID: Int,
        @SerializedName("AllDay") val allDay: Boolean
) : WebItem() {
    /*
        Here's a major bummer. The JSON deserialization library we're using (GSON)
        uses unsafe magic Java classes to instantiate the data class we're using to hold the
        data. The magic class skips initialization, so the class will only have the data
        from the JSON. No computed properties. So, we make the call, the deserialization to
        the data class happens in the background, and we get partially instantiated objects
        at the end. To workaround, we the computed properties are computed on demand, rather than
        on instantiation.
    */

    override val content: String
        get() = description

    override val attributedTitle: String?
        get() = null
    override val actionBarTitle: String
        get() = "Calendar"
    override val favoriteUrl: String
        get() = CalendarProvider.favoritesUrl
    override val contentUrl: String
        get() = url
    override val redirectUrl: String?
        get() = null
    override var isFavorite: Boolean = false
    val dateFormat: DateFormat
        get() = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)

    val calendarType: CalendarItemType
        get() = CalendarItemType.fromCode(calenderID)

    val dateFrom: Date?
        get() = dateFormat.parse(dateFromString)

    val dateTo: Date?
        get() = dateFormat.parse(dateToString)

    override fun associatedImage(context: Context): Observable<Drawable>? {
        return null
    }
}

enum class CalendarItemType(val code: Int) {
    IFT(1),
    LOCAL(2),
    NATIONAL(3),
    HOLIDAY(4),
    COUNCIL(5);

    companion object {
        fun fromCode(code: Int): CalendarItemType = when (code) {
            1 -> IFT
            2 -> LOCAL
            3 -> NATIONAL
            4 -> HOLIDAY
            else -> COUNCIL
        }
    }
}