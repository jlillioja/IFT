package io.grandlabs.ift.search

import io.grandlabs.ift.advocate.AdvocacyItem
import io.grandlabs.ift.calendar.CalendarItem
import io.grandlabs.ift.calendar.CalendarItemType
import io.grandlabs.ift.login.SessionManager
import io.grandlabs.ift.network.IftClient
import io.grandlabs.ift.news.NewsItem
import io.reactivex.Observable
import javax.inject.Inject

class SearchProvider
@Inject constructor(val iftClient: IftClient,
                    val sessionManager: SessionManager) {

    /*
        [@{@"itemClass" : @"NewsItem",
         @"getFavoritesURL" : @"member_favoritenews",
          @"getItemsURL" : @"news",
           @"titleFieldName" : @"title_",
            @"contentIDFieldName" : @"content_id",
             @"descriptionFieldName" : @"content_",
              @"useSiteInfinitySearch" : @(YES)}];
     */

    fun getFilteredNewsItems(filter: String): Observable<List<NewsItem>> {
        return iftClient
                .filteredNews(token = sessionManager.authorizationHeader, filter = filter)
                .map { it.body() }
    }


    /*
    [itemTypes addObject:@{@"itemClass" : @"AdvocacyItem",
     @"getFavoritesURL" : @"member_favoriteadvocacy",
      @"getItemsURL" : @"advocacy",
       @"titleFieldName" : @"title",
        @"contentIDFieldName" : @"base_id",
         @"descriptionFieldName" : @"content",
          @"useSiteInfinitySearch" : @(YES)}];
     */
    fun getFilteredAdvocacyItems(filter: String): Observable<List<AdvocacyItem>> {
        return iftClient
                .advocacy(sessionManager.authorizationHeader, filter)
                .map { it.body() }
    }


    /*
        [itemTypes addObject:@{@"itemClass" : @"CalendarItem",
         @"getFavoritesURL" : @"member_favoritecalendarevent",
          @"getItemsURL" : @"calendarevent",
           @"titleFieldName" : @"Title",
            @"contentIDFieldName" : @"ID",
             @"descriptionFieldName" : @"Description",
              @"useSiteInfinitySearch" : @(NO)}];
     */
    fun getFilteredCalendarItems(filter: String): Observable<List<CalendarItem>> {
        return iftClient
                .calendar(
                        sessionManager.authorizationHeader,
                        filter = "Title LIKE '%$filter%' OR Description LIKE '%$filter%'")
                .map {
                    it.body()
                            ?.items
                            ?.filter {
                                listOf(
                                        CalendarItemType.LOCAL,
                                        CalendarItemType.COUNCIL,
                                        CalendarItemType.IFT,
                                        CalendarItemType.NATIONAL
                                ).contains(it.calendarType)
                            }
                }
    }

}