import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v4.content.ContextCompat
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.InputStream
import java.net.URL

fun fetchImageFromUrl(url: String): Observable<Drawable> {
    return Observable.fromCallable {
        val imageStream = URL(url).content as InputStream
        Drawable.createFromStream(imageStream, "src name")
    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun openMapsAtAddress(address: String, context: Context) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=$address"))
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
    intent.setPackage("com.google.android.apps.maps")
    ContextCompat.startActivity(context, intent, null)
}

fun openDialerForNumber(number: String, context: Context) {
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
    ContextCompat.startActivity(context, intent, null)
}
