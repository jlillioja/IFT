package io.grandlabs.ift.network

import io.grandlabs.ift.advocate.AdvocacyItem
import io.grandlabs.ift.calendar.CalendarResult
import io.grandlabs.ift.contact.OfficeResult
import io.grandlabs.ift.login.LoginSuccessResult
import io.grandlabs.ift.news.NewsItem
import io.grandlabs.ift.news.NewsResult
import io.grandlabs.ift.settings.IftMember
import io.grandlabs.ift.settings.LocalOffice
import io.grandlabs.ift.settings.Preference
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*


interface IftClient {

    @POST("https://sso.ift-aft.org/connect/token")
    @FormUrlEncoded
    fun login(@FieldMap params: Map<String, String>): Observable<Response<LoginSuccessResult>>

    @GET("news")
    fun news(
            @Header("Authorization") token: String,
            @Query("Page") page: Int,
            @Query("PageSize") pageSize: Int
    ): Observable<Response<NewsResult>>

    @GET("news")
    fun filteredNews(
            @Header("Authorization") token: String,
            @Query("filter") filter: String
    ): Observable<Response<List<NewsItem>>>

    @GET("calendarevent")
    fun calendar(
            @Header("Authorization") token: String,
            @Query("Page") page: Int? = null,
            @Query("PageSize") pageSize: Int? = null,
            @Query("filter") filter: String? = null,
            @Query("sorts") sort: String? = null
    ): Observable<Response<CalendarResult>>

    @GET("advocacy")
    fun advocacy(
            @Header("Authorization") token: String,
            @Query("filter") filter: String? = null
    ): Observable<Response<List<AdvocacyItem>>>

    @GET("office")
    fun contact(
            @Header("Authorization") token: String,
            @Query("Page") page: Int,
            @Query("PageSize") pageSize: Int
    ): Observable<Response<OfficeResult>>

    @GET("member/{memberId}")
    fun member(
            @Path("memberId") memberId: Int,
            @Header("Authorization") token: String
    ): Observable<Response<IftMember>>

    @GET("local/{localNum}")
    fun local(
            @Path("localNum") localNum: Int,
            @Header("Authorization") token: String
    ): Observable<Response<List<LocalOffice>>>

    @GET("member_categorynews")
    fun newsPreferences(
            @Header("Authorization") token: String
    ): Observable<Response<List<Preference>>>

    @GET("member_categoryadvocacy")
    fun advocacyPreferences(
            @Header("Authorization") token: String
    ): Observable<Response<List<Preference>>>
}