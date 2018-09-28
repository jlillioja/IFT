package io.grandlabs.ift.settings

import io.grandlabs.ift.login.SessionManager
import io.grandlabs.ift.network.IftClient
import io.grandlabs.ift.network.PutAlertPreferencesRequest
import io.reactivex.Observable
import io.reactivex.rxkotlin.combineLatest
import javax.inject.Inject

class AccountInformationManager @Inject constructor(
        val iftClient: IftClient,
        val sessionManager: SessionManager
) {

    fun getMember(): Observable<IftMember> =
            iftClient.member(
                    sessionManager.memberId,
                    sessionManager.authorizationHeader)
                    .share()
                    .map {
                        it.body()
                    }

    fun getLocalOffice(): Observable<LocalOffice> =
            getMember()
                    .flatMap {
                        iftClient.local(
                                it.localNum,
                                sessionManager.authorizationHeader
                        )
                    }
                    .map { it.body()?.get(0) }

    fun getPresident(): Observable<President> = getMember()
            .flatMap { iftClient.localPresident(it.localNum, sessionManager.authorizationHeader) }
            .map { it.body()?.first() }

    fun getVicePresident(): Observable<VicePresident> = getMember()
            .flatMap { iftClient.localVicePresident(it.localNum, sessionManager.authorizationHeader) }
            .map { it.body()?.first() }

    fun getFieldServiceDirector(): Observable<FieldServiceDirector> {

        return getMember()
                .flatMap { iftClient.localFieldServiceDirector(it.localNum, sessionManager.authorizationHeader) }
                .map { it.body() }

    }

    fun getNewsPreferences(): Observable<List<Preference>> {
        return iftClient
                .newsPreferences(sessionManager.authorizationHeader)
                .map { it.body() }
    }

    fun setAlertPreferences(emailAlerts: Boolean, pushNotifications: Boolean): Observable<Boolean> {
        return iftClient
                .putAlertPreferences(
                        sessionManager.memberId,
                        sessionManager.authorizationHeader,
                        PutAlertPreferencesRequest(if (emailAlerts) 1 else 0, if (pushNotifications) 1 else 0))
                .map { true }
                .onErrorReturnItem(false)
    }

    fun addNewsAlertCategoryPreferences(preferences: List<Preference>): Observable<Boolean> {
        return if(preferences.isEmpty()) Observable.just(true) else preferences
                .map { iftClient.addNewsPreference(it.id, sessionManager.authorizationHeader) }
                .combineLatest { true }
                .onErrorReturnItem(false)
    }

    fun removeNewsAlertCategoryPreferences(preferences: List<Preference>): Observable<Boolean> {
        return if(preferences.isEmpty()) Observable.just(true) else preferences
                .map { iftClient.removeNewsPreference(it.id, sessionManager.authorizationHeader) }
                .combineLatest { true }
                .onErrorReturnItem(false)
    }

    fun addAdvocacyAlertCategoryPreferences(preferences: List<Preference>): Observable<Boolean> {
        return if(preferences.isEmpty()) Observable.just(true) else preferences
                .map { iftClient.addAdvocacyPreference(it.id, sessionManager.authorizationHeader) }
                .combineLatest { true }
                .onErrorReturnItem(false)
    }

    fun removeAdvocacyAlertCategoryPreferences(preferences: List<Preference>): Observable<Boolean> {
        return if(preferences.isEmpty()) Observable.just(true) else preferences
                .map { iftClient.removeAdvocacyPreference(it.id, sessionManager.authorizationHeader) }
                .combineLatest { true }
                .onErrorReturnItem(false)
    }

//    - (IBAction) doSave {
//
//        [self showActivityIndicator];
//
//        numEndPointsCalled = 0;
//        numEndPointsReturned = 0;
//
//        NSMutableDictionary *parameters = [[NSMutableDictionary alloc] init];
//
//        for (int row = 0; row < 3; row++) {
//
//            SettingsSwitchTableViewCell *cell = [self.tableView cellForRowAtIndexPath:[NSIndexPath indexPathForRow:row inSection:2]];
//
//            BOOL newValue = cell.switcher.on;
//            NSString *paramName;
//
//            switch (row) {
//                case 0:
//                paramName = @"EmailAlerts";
//                break;
//                case 1:
//                paramName = @"PushNotifications";
//                break;
//            }
//            if (newValue != [userData[paramName] boolValue]) {
//                [parameters setObject:[NSNumber numberWithBool:newValue] forKey:paramName];
//            }
//
//        }
//
//        for (NSDictionary *prefDict in categories) {
//
//            NSArray *oldValues = prefDict[@"oldValuesArray"];
//            NSArray *newValues = prefDict[@"curValuesArray"];
//
//            for (NSDictionary *category in prefDict[@"categories"]) {
//
//            NSNumber *catid = category[@"id"];
//            NSString *url = [NSString stringWithFormat:@"%@/%ld", prefDict[@"url"], [catid integerValue]];
//
//            BOOL wasIncluded = [oldValues containsObject:catid];
//            BOOL nowIncluded = [newValues containsObject:catid];
//
//            if (wasIncluded && !nowIncluded) {
//                numEndPointsCalled++;
//                [[IFTHTTPClient sharedIFTHTTPClient] delete:url parameters:nil success:^(NSURLSessionDataTask *operation, id responseObject) {
//                    NSLog(@"Preferences deleted.");
//                    numEndPointsReturned++;
//                } failure:^(NSURLSessionDataTask *task, NSError *error) {
//                    numEndPointsReturned++;
//                }];
//            } else if (!wasIncluded && nowIncluded) {
//                numEndPointsCalled++;
//                [[IFTHTTPClient sharedIFTHTTPClient] post:url parameters:nil success:^(NSURLSessionDataTask *operation, id responseObject) {
//                    numEndPointsReturned++;
//                    NSLog(@"Preference added.");
//                } failure:^(NSURLSessionDataTask *task, NSError *error) {
//                    numEndPointsReturned++;
//                    NSLog(@"Error adding preference.");
//                }];
//            }
//        }
//        }
//
//        if ([parameters count] > 0) {
//
//            NSDictionary *dict = [[IFTUtils sharedManager] dataDictFromJWTToken];
//            NSString *loggedInMemberID = dict[@"id"];
//
//            [self showActivityIndicator];
//
//            numEndPointsCalled++;
//            [[IFTHTTPClient sharedIFTHTTPClient] put:[NSString stringWithFormat:@"member/%@", loggedInMemberID] parameters:parameters success:^(NSURLSessionDataTask *operation, id responseObject) {
//                numEndPointsReturned++;
//            } failure:^(NSURLSessionDataTask *task, NSError *error) {
//                numEndPointsReturned++;
//            }];
//        }

    fun getAdvocacyPreferences(): Observable<List<Preference>> {
        return iftClient
                .advocacyPreferences(sessionManager.authorizationHeader)
                .map { it.body() }
    }
}