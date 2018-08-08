import android.graphics.drawable.Drawable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.InputStream
import java.net.URL

fun fetchImageFromUrl(url: String): Observable<Drawable?> {
    return Observable.fromCallable {
        try {
            val imageStream = URL(url).content as InputStream
            Drawable.createFromStream(imageStream, "src name")
        } catch (e: Exception) {
            null
        }
    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}
