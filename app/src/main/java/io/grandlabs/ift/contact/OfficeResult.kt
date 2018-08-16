package io.grandlabs.ift.contact

import com.google.gson.annotations.SerializedName

data class OfficeResult(
        @SerializedName("Items") val items: List<OfficeItem>
)
