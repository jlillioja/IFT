package io.grandlabs.ift

import android.content.Context
import android.graphics.drawable.Drawable
import io.reactivex.Observable

abstract class WebItem {
    abstract val title: String
    abstract val attributedTitle: String?
    abstract val actionBarTitle: String
    abstract val content: String
    abstract val summary: String
    abstract val contentUrl: String?
    abstract val redirectUrl: String?
    abstract val contentId: String
    abstract val favoriteUrl: String
    abstract var isFavorite: Boolean

    abstract fun associatedImage(context: Context): Observable<Drawable>?

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WebItem

        if (contentId != other.contentId) return false

        return true
    }

    override fun hashCode(): Int {
        return contentId.hashCode()
    }


}