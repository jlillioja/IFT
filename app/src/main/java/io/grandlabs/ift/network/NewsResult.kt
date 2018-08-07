package io.grandlabs.ift.network

import com.google.gson.annotations.SerializedName

data class NewsItem(
    @SerializedName("url_name_") val url: String,
    @SerializedName("title_") val title: String,
    @SerializedName("publication_date") val publicationDate: String,
    @SerializedName("content_id") val contentId: String,
    @SerializedName("summary_") val summary: String,
    @SerializedName("content_") val content: String,
    @SerializedName("redirect_u_r_l") val redirectUrl: String,
    @SerializedName("thumbnail_image") val thumbnailImage: String
)

data class NewsResult(
        @SerializedName("CurrentPage") val currentPage: Int,
        @SerializedName("TotalPages") val totalPages: Int,
        @SerializedName("TotalItems") val totalItems: Int,
        @SerializedName("ItemsPerPage") val itemsPerPage: Int,
        @SerializedName("Items") val items: List<NewsItem>
)