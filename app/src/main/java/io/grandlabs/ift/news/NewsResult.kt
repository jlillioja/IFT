package io.grandlabs.ift.news

import com.google.gson.annotations.SerializedName

data class NewsResult(
        @SerializedName("CurrentPage") val currentPage: Int,
        @SerializedName("TotalPages") val totalPages: Int,
        @SerializedName("TotalItems") val totalItems: Int,
        @SerializedName("ItemsPerPage") val itemsPerPage: Int,
        @SerializedName("Items") val items: List<NewsItem>
)