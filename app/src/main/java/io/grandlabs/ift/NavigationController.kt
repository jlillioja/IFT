package io.grandlabs.ift

import io.grandlabs.ift.network.CalendarItem
import io.grandlabs.ift.network.NewsItem
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
    object Advocate: NavigationState()
    object Contact: NavigationState()
    object Invite: NavigationState()
    object Settings: NavigationState()
}
