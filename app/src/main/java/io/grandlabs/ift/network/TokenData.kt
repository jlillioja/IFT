package io.grandlabs.ift.network

import com.google.gson.annotations.SerializedName

data class TokenData(
        @SerializedName("nbf") val nbf: Int,
        @SerializedName("exp") val exp: Int,
        @SerializedName("iss") val iss: String,
        @SerializedName("aud") val aud: List<String>,
        @SerializedName("client_id") val clientId: String,
        @SerializedName("sub") val sub: String,
        @SerializedName("auth_time") val authTime: Int,
        @SerializedName("idp") val idp: String,
        @SerializedName("id") val id: String,
        @SerializedName("email") val email: String,
        @SerializedName("name") val name: String,
        @SerializedName("given_name") val firstName: String,
        @SerializedName("family_name") val lastName: String,
        @SerializedName("updated_at") val updatedAt: String,
        @SerializedName("localNum") val localNum: String,
        @SerializedName("councilID") val councilId: String,
        @SerializedName("scope") val scope: List<String>,
        @SerializedName("amr") val amr: List<String>
)