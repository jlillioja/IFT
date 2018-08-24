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
    abstract val favoriteUrl: String
    abstract val contentUrl: String?
    abstract val redirectUrl: String?
    abstract val isFavorite: Boolean
    abstract val contentId: String

    abstract fun associatedImage(context: Context): Observable<Drawable>?
}