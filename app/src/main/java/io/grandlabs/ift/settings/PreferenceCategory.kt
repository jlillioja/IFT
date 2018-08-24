package io.grandlabs.ift.settings

import com.google.gson.annotations.SerializedName

sealed class PreferenceCategory(
        val name: String,
        val url: String,
        val preferences: List<Preference>
) {

    object NewsPreferences : PreferenceCategory(
            "News",
            "member_categorynews",
            listOf(
                    Preference("Public Education", 1),
                    Preference("Higher Education", 2),
                    Preference("Public Services", 3),
                    Preference("Economy", 4),
                    Preference("Retirement Security", 5),
                    Preference("Civil and Workplace Rights", 6)
            )
    )

    object AdvocacyPreferences : PreferenceCategory(
            "Advocacy",
            "member_categoryadvocacy",
            listOf(
                    Preference("Public Education", 1),
                    Preference("Higher Education", 2),
                    Preference("Public Services", 3),
                    Preference("Economy", 4),
                    Preference("Retirement Security", 5),
                    Preference("Civil and Workplace Rights", 6)
            )
    )
}

data class Preference(
        @SerializedName("Name") val name: String,
        @SerializedName("ID") val id: Int
)