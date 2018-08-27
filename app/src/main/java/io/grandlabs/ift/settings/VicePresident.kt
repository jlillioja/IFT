package io.grandlabs.ift.settings

data class VicePresident (
        val firstName: String,
        val lastName: String
) {
    val fullName get() = "$firstName $lastName"
}
