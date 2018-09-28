package io.grandlabs.ift.settings

import com.google.gson.annotations.SerializedName

data class FieldServiceDirector (
        @SerializedName("FirstName") val firstName: String,
        @SerializedName("LastName") val lastName: String
) {
    val fullName get() = "$firstName $lastName"
}
