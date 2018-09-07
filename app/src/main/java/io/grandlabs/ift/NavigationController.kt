package io.grandlabs.ift

import io.grandlabs.ift.advocate.AdvocacyItem
import io.grandlabs.ift.calendar.CalendarItem
import io.grandlabs.ift.news.NewsItem
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

interface NavigationController {
    fun navigateTo(navigationState: NavigationState)
    val navigation: Observable<NavigationState>
}

class NavigationControllerImpl: NavigationController {

    override fun navigateTo(navigationState: NavigationState) {
        navigation.onNext(navigationState)
    }

    override val navigation = BehaviorSubject.createDefault<NavigationState>(NavigationState.Login)

}

sealed class NavigationState {
    object Login: NavigationState()
    object SignUp: NavigationState()
    object NewsList: NavigationState()
    class NewsDetail(val item: NewsItem): NavigationState()
    object Calendar: NavigationState()
    class CalendarDetail(val item: CalendarItem) : NavigationState()
    object AddEvent: NavigationState()
    object Advocacy: NavigationState()
    class AdvocacyDetail(val item: AdvocacyItem): NavigationState()
    object Contact: NavigationState()
    object Invite: NavigationState()
    object Settings: NavigationState()
}
