package io.grandlabs.ift.settings

import android.content.Context
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleExpandableListAdapter
import io.grandlabs.ift.IftApp
import io.grandlabs.ift.IftFragment
import io.grandlabs.ift.R
import io.grandlabs.ift.login.SessionManager
import io.grandlabs.ift.settings.PreferenceCategory.AdvocacyPreferences
import io.grandlabs.ift.settings.PreferenceCategory.NewsPreferences
import io.grandlabs.ift.sharing.LinkHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.view.*
import kotlinx.android.synthetic.main.preference_list_child_item.view.*

import javax.inject.Inject

class SettingsFragment : IftFragment() {

    val LOG_TAG = this::class.simpleName

    @Inject
    lateinit var accountInformationManager: AccountInformationManager

    @Inject
    lateinit var sessionManager: SessionManager

    @Inject lateinit var linkHelper: LinkHelper

    init {
        IftApp.graph.inject(this)
    }

    override fun getActionBarTitle(): String = "settings"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        listener?.setCurrentlySelectedFragment(this)

        view?.logoutButton?.setOnClickListener {
            sessionManager.logout()
            activity?.finish()
        }

        accountInformationManager.getMember()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            if (!sessionManager.isUserAMember()) {
                                view.name.text = "Non Member"
                                view.userAddressLine1.visibility = View.GONE
                                view.userAddressLine2.visibility = View.GONE
                                view.phone.visibility = View.GONE
                            } else {
                                view.name.text = "${it.firstName} ${it.lastName}"
                                view.userAddressLine1.text = it.address
                                view.userAddressLine2.text = "${it.city} ${it.state} ${it.zip}"
                                view.phone.text = it.displayedPhone
                            }
                            view.email.text = it.homeEmail
                            view.memberId.text = it.memberId

//                            view.accountInformation.visibility = View.VISIBLE

                            view.pushNotificationsSwitch.isChecked = it.isPushNotificationsEnabled
                            view.emailAlertsSwitch.isChecked = it.isEmailAlertsEnabled
                            // TODO: switch listeners
                        },
                        onError = {
//                            view.accountInformation.visibility = View.VISIBLE
                        },
                        onComplete = {}
                )

        accountInformationManager.getLocalOffice()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            view.localChapterName.text = it.name
                            view.localChapterAddress1.text = it.address
                            view.localChapterAddress2.text = "${it.city} ${it.state} ${it.zip}"
                            view.localChapterPhoneNumber.text = PhoneNumberUtils.formatNumber(it.phone)
                            view.localChapterPhoneNumber.setOnClickListener { _ ->
                                linkHelper.openDialerForNumber(it.phone)
                            }
                            view.localChapterDirections.setOnClickListener { _ ->
                                linkHelper.openMaps("${it.address}, ${it.city} ${it.state} ${it.zip}")
                            }

                            view.localChapterInformation.visibility = View.VISIBLE
                        },
                        onError = {
                            Log.d(LOG_TAG, it.localizedMessage)
                        },
                        onComplete = {}
                )

        accountInformationManager.getPresident()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            localChapterPresident.text = it?.fullName ?: ""
                        },
                        onError = {},
                        onComplete = {}
                )

        accountInformationManager.getVicePresident()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            localChapterVicePresident.text = it?.fullName ?: ""
                        },
                        onError = {},
                        onComplete = {}
                )

        accountInformationManager.getFieldServiceDirector()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            localChapterFieldServiceDirector.text = it?.fullName ?: ""
                        },
                        onError = {},
                        onComplete = {}
                )

        Observables.combineLatest(
                accountInformationManager.getNewsPreferences(),
                accountInformationManager.getAdvocacyPreferences()
        ) { newsPreferences, advocacyPreferences ->
            (newsPreferences to advocacyPreferences)
        }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            view.preferencesList.setAdapter(PreferencesListAdapter(context!!, it.first, it.second))
                            view.containerScrollView.fullScroll(View.FOCUS_UP)
                        },
                        onError = {
                            Log.d(LOG_TAG, it.localizedMessage)
                        },
                        onComplete = {}
                )


        return view
    }

    class PreferencesListAdapter(context: Context, val oldNewsPreferences: List<Preference>, val oldAdvocacyPreferences: List<Preference>) : SimpleExpandableListAdapter(
            context,

            listOf(
                    mapOf(groupKey to "News"),
                    mapOf(groupKey to "Advocacy")
            ),
            R.layout.preference_group_item,
            arrayOf(groupKey),
            intArrayOf(R.id.preferenceGroupName),

            listOf(
                    NewsPreferences.preferences.map {
                        mapOf(childKey to it.name)
                    },
                    AdvocacyPreferences.preferences.map {
                        mapOf(childKey to it.name)
                    }
            ),
            R.layout.preference_list_child_item,
            arrayOf(childKey),
            intArrayOf(R.id.preferenceName)
    ) {
        companion object {
            private const val groupKey = "Group Item"
            private const val childKey = "Child Item"
            private const val newsGroupPosition = 0
            private const val advocacyGroupPosition = 1
        }

        val userNewsPreferences: MutableList<Preference> = oldNewsPreferences.toMutableList()
        val userAdvocacyPreferences: MutableList<Preference> = oldAdvocacyPreferences.toMutableList()

        override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
            val view = super.getChildView(groupPosition, childPosition, isLastChild, convertView, parent)

            view.preferenceCheckBox.isChecked = isPreferenceAtPositionEnabled(groupPosition, childPosition)
            view.preferenceCheckBox.setOnCheckedChangeListener { _, isChecked ->
                val preference = preferenceForPosition(groupPosition, childPosition)
                when (groupPosition) {
                    newsGroupPosition -> {
                        if (isChecked) {
                            userNewsPreferences.add(preference)
                        } else {
                            userNewsPreferences.remove(preference)
                        }
                    }
                    advocacyGroupPosition -> {
                        if (isChecked) {
                            userNewsPreferences.add(preference)
                        } else {
                            userNewsPreferences.remove(preference)
                        }
                    }
                }
            }

            return view
        }

        private fun userPreferencesForGroup(groupPosition: Int): List<Preference> = when (groupPosition) {
            newsGroupPosition -> userNewsPreferences
            advocacyGroupPosition -> userAdvocacyPreferences
            else -> emptyList()
        }

        private fun allPreferencesForGroup(groupPosition: Int) = when (groupPosition) {
            newsGroupPosition -> NewsPreferences.preferences
            advocacyGroupPosition -> AdvocacyPreferences.preferences
            else -> emptyList()
        }

        private fun preferenceForPosition(groupPosition: Int, position: Int) = allPreferencesForGroup(groupPosition)[position]
        private fun isPreferenceAtPositionEnabled(groupPosition: Int, position: Int) = userPreferencesForGroup(groupPosition).contains(preferenceForPosition(groupPosition, position))

        val LOG_TAG = this::class.simpleName
    }
}
