package io.grandlabs.ift.news

import android.content.Context
import com.google.gson.annotations.SerializedName
import fetchImageFromUrl
import io.grandlabs.ift.WebItem

data class NewsItem(
        @SerializedName("title_") override val title: String,
        @SerializedName("summary_") override val summary: String,
        @SerializedName("content_") override val content: String,
        @SerializedName("redirect_u_r_l") override val redirectUrl: String,
        @SerializedName("content_id") override val contentId: String,
        @SerializedName("url_name_") override val contentUrl: String,
        @SerializedName("publication_date") val publicationDate: String,
        @SerializedName("thumbnail_image") val thumbnailImage: String
): WebItem() {
    override val attributedTitle: String?
        get() = null
    override val actionBarTitle: String
        get() = "News"
    override val favoriteUrl: String
        get() = "member_favoritenews"
    override val isFavorite: Boolean
        get() = false

    override fun associatedImage(context: Context) = fetchImageFromUrl(thumbnailImage)
}