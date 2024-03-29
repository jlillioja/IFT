package io.grandlabs.ift.network

import com.google.gson.annotations.SerializedName
import io.grandlabs.ift.advocate.AdvocacyItem
import io.grandlabs.ift.advocate.AdvocacyProvider
import io.grandlabs.ift.calendar.AddEventRequest
import io.grandlabs.ift.calendar.CalendarItem
import io.grandlabs.ift.calendar.CalendarManager
import io.grandlabs.ift.calendar.CalendarResult
import io.grandlabs.ift.contact.OfficeResult
import io.grandlabs.ift.login.CheckRegistrationResult
import io.grandlabs.ift.login.LoginSuccessResult
import io.grandlabs.ift.login.RegistrationRequest
import io.grandlabs.ift.login.RegistrationResult
import io.grandlabs.ift.news.NewsItem
import io.grandlabs.ift.news.NewsProvider
import io.grandlabs.ift.news.NewsResult
import io.grandlabs.ift.settings.*
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

    @POST("calendarevent")
    fun addEvent(
        @Header("Authorization") token: String,
        @Body params: AddEventRequest
    ): Observable<Response<Void>>

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

    @PUT("member/{id}")
    fun putAlertPreferences(
            @Path("id") memberId: Int,
            @Header("Authorization") token: String,
            @Body params: PutAlertPreferencesRequest
    ): Observable<Response<Void>>

    @GET("local/{localNum}")
    fun local(
            @Path("localNum") localNum: Int,
            @Header("Authorization") token: String
    ): Observable<Response<List<LocalOffice>>>

    @GET("local/{localNum}/president")
    fun localPresident(
            @Path("localNum") localNum: Int,
            @Header("Authorization") token: String
    ): Observable<Response<List<President>>>

    @GET("local/{localNum}/vicepresident")
    fun localVicePresident(
            @Path("localNum") localNum: Int,
            @Header("Authorization") token: String
    ): Observable<Response<List<VicePresident>>>

    @GET("local/{localNum}/fieldservicedirector")
    fun localFieldServiceDirector(
            @Path("localNum") localNum: Int,
            @Header("Authorization") token: String
    ): Observable<Response<FieldServiceDirector>>

    @GET("member_categorynews")
    fun newsPreferences(
            @Header("Authorization") token: String
    ): Observable<Response<List<Preference>>>

    @POST("member_categorynews/{categoryId}")
    fun addNewsPreference(
            @Path("categoryId") categoryId: Int,
            @Header("Authorization") token: String
    ): Observable<Response<Void>>

    @DELETE("member_categorynews/{categoryId}")
    fun removeNewsPreference(
            @Path("categoryId") categoryId: Int,
            @Header("Authorization") token: String
    ): Observable<Response<Void>>

    @GET("member_categoryadvocacy")
    fun advocacyPreferences(
            @Header("Authorization") token: String
    ): Observable<Response<List<Preference>>>

    @POST("member_categoryadvocacy/{categoryId}")
    fun addAdvocacyPreference(
            @Path("categoryId") categoryId: Int,
            @Header("Authorization") token: String
    ): Observable<Response<Void>>

    @DELETE("member_categoryadvocacy/{categoryId}")
    fun removeAdvocacyPreference(
            @Path("categoryId") categoryId: Int,
            @Header("Authorization") token: String
    ): Observable<Response<Void>>


    @GET("register")
    fun checkRegistration(
            @Query("HomeEmail") email: String,
            @Query("FirstName") firstName: String?,
            @Query("LastName") lastName: String?
    ): Observable<Response<CheckRegistrationResult>>

    @POST("register")
    fun register(
            @Body params: RegistrationRequest
    ): Observable<Response<RegistrationResult>>

    @GET(NewsProvider.favoritesUrl)
    fun favoriteNews(
            @Header("Authorization") token: String
    ): Observable<Response<List<NewsItem>>>

    @GET(CalendarManager.favoritesUrl)
    fun favoritesCalendarEvents(
            @Header("Authorization") token: String
    ): Observable<Response<List<CalendarItem>>>

    @GET(AdvocacyProvider.favoritesUrl)
    fun favoritesAdvocacy(
            @Header("Authorization") token: String
    ): Observable<Response<List<AdvocacyItem>>>

    @DELETE("{favoriteUrl}/{contentId}")
    fun deleteFavorite(
            @Header("Authorization") token: String,
            @Path("favoriteUrl") favoriteUrl: String,
            @Path("contentId") contentId: String
    ): Observable<Response<Void>>

    @POST("{favoriteUrl}/{contentId}")
    fun postFavorite(
            @Header("Authorization") token: String,
            @Path("favoriteUrl") favoriteUrl: String,
            @Path("contentId") contentId: String
    ): Observable<Response<Void>>

    @POST("member_devicetoken")
    fun postToken(
            @Header("Authorization") token: String,
            @Body params: TokenRequest
    ): Observable<Response<Void>>
}

data class TokenRequest(
        @SerializedName("DeviceTokenTypeID") val deviceTokenTypeId: Int,
        @SerializedName("DeviceToken") val deviceToken: String?
)