package io.grandlabs.ift.network

import io.grandlabs.ift.BuildConfig
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*


interface IftClient {

    @POST("https://sso.ift-aft.org/connect/token")
    @FormUrlEncoded
    fun login(@FieldMap params: Map<String, String>): Observable<Response<LoginSuccessResult>>

}