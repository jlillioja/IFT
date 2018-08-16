package io.grandlabs.ift.network

import io.grandlabs.ift.advocate.AdvocacyItem
import io.grandlabs.ift.calendar.CalendarResult
import io.grandlabs.ift.contact.OfficeResult
import io.grandlabs.ift.login.LoginSuccessResult
import io.grandlabs.ift.news.NewsResult
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

    @GET("calendarevent")
    fun calendar(
            @Query("sorts") sort: String?,
            @Query("filter") filter: String?,
            @Query("Page") page: Int,
            @Query("PageSize") pageSize: Int,
            @Header("Authorization") token: String
    ): Observable<Response<CalendarResult>>

    @GET("advocacy")
    fun advocacy(
            @Header("Authorization") token: String
    ): Observable<Response<List<AdvocacyItem>>>

    @GET("office")
    fun contact(
            @Header("Authorization") token: String,
            @Query("Page") page: Int,
            @Query("PageSize") pageSize: Int
    ): Observable<Response<OfficeResult>>
}