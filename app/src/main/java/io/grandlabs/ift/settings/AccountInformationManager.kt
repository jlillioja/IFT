package io.grandlabs.ift.settings

import io.grandlabs.ift.login.SessionManager
import io.grandlabs.ift.network.IftClient
import io.reactivex.Observable
import javax.inject.Inject

class AccountInformationManager @Inject constructor(
        val iftClient: IftClient,
        val sessionManager: SessionManager
) {

    fun getMember(): Observable<IftMember> {
        return iftClient.member(
                sessionManager.memberId,
                sessionManager.authorizationHeader)
                .share()
                .map {
                    sessionManager.member = it.body()
                    it.body()
                }

    }

    fun getLocalOffice(): Observable<LocalOffice> {
        return getMember()
                .flatMap {
                    iftClient.local(
                            it.localNum,
                            sessionManager.authorizationHeader
                    )
                }
                .map { it.body()?.get(0) }
    }

    fun getPresident(): Observable<President> {
        val localNum = sessionManager.member?.localNum
        if (localNum != null) {
            return iftClient.localPresident(localNum, sessionManager.authorizationHeader)
                    .map { it.body()?.first() }
        } else {
            return getMember()
                    .flatMap { iftClient.localPresident(it.localNum, sessionManager.authorizationHeader) }
                    .map { it.body()?.first() }
        }
    }

    fun getVicePresident(): Observable<VicePresident> {
        val localNum = sessionManager.member?.localNum
        if (localNum != null) {
            return iftClient.localVicePresident(localNum, sessionManager.authorizationHeader)
                    .map { it.body()?.first() }
        } else {
            return getMember()
                    .flatMap { iftClient.localVicePresident(it.localNum, sessionManager.authorizationHeader) }
                    .map { it.body()?.first() }
        }
    }

    fun getFieldServiceDirector(): Observable<FieldServiceDirector> {
        val localNum = sessionManager.member?.localNum
        if (localNum != null) {
            return iftClient.localFieldServiceDirector(localNum, sessionManager.authorizationHeader)
                    .map { it.body() }
        } else {
            return getMember()
                    .flatMap { iftClient.localFieldServiceDirector(it.localNum, sessionManager.authorizationHeader) }
                    .map { it.body() }
        }
    }
//
//    fun getLocalOfficers(): Observable<LocalOfficers> {
//        return getMember().flatMap {
//            Observables.combineLatest(
//                    iftClient.localPresident(it.localNum, sessionManager.authorizationHeader),
//                    iftClient.localVicePresident(it.localNum, sessionManager.authorizationHeader),
//                    iftClient.localFieldServiceDirector(it.localNum, sessionManager.authorizationHeader)
//            ) { presidentResponse, vicePresidentResponse, fieldServiceDirectorResponse ->
//                Log.d("AccountInformationManager", )
//                LocalOfficers(
//                        presidentResponse.body()?.firstOrNull(),
//                        vicePresidentResponse.body()?.firstOrNull(),
//                        fieldServiceDirectorResponse.body()
//                )
//            }
//        }
//    }

    fun getNewsPreferences(): Observable<List<Preference>> {
        return iftClient
                .newsPreferences(sessionManager.authorizationHeader)
                .map { it.body() }
    }

//    fun setEmailAlerts(active: Boolean): Observable<Void> {
//
//    }
//
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