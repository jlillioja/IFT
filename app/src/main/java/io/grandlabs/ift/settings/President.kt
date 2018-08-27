package io.grandlabs.ift.settings

data class President(
    val firstName: String,
    val lastName: String
) {
    val fullName get() = "$firstName $lastName"
}
