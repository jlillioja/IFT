package io.grandlabs.ift.network

import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*


interface IftClient {

    @POST("https://sso.ift-aft.org/connect/token")
    @FormUrlEncoded
    fun login(@FieldMap params: Map<String, String>): Observable<Response<LoginSuccessResult>>

    @GET("news")
    fun news(
            @Query("Page") page: Int,
            @Query("PageSize") pageSize: Int,
            @Header("Authorization") token: String
    ): Observable<Response<NewsResult>>

}