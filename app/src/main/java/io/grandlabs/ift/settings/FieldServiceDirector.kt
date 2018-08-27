package io.grandlabs.ift.settings

data class FieldServiceDirector (
        val firstName: String,
        val lastName: String
) {
    val fullName get() = "$firstName $lastName"
}
