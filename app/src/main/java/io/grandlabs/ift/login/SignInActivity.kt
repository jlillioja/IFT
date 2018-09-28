package io.grandlabs.ift.login

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import io.grandlabs.ift.IftApp
import io.grandlabs.ift.MainActivity
import io.grandlabs.ift.R
import io.grandlabs.ift.login.RegistrationStatus.*
import io.grandlabs.ift.settings.AccountInformationManager
import io.grandlabs.ift.showProgressDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_sign_in.*
import javax.inject.Inject

class SignInActivity
    : AppCompatActivity() {

    val LOG_TAG = SignInActivity::class.simpleName

    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var accountInformationManager: AccountInformationManager

    var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        forgotPassword.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ift-aft.org/account/reset-password"))
            startActivity(browserIntent)
        }

        switchToLogin.setOnClickListener {
            switchToLogin()
        }

        switchToSignUp.setOnClickListener {
            switchToSignUp()
        }

        submitRegistrationButton.setOnClickListener {
            submitRegistration()
        }

        IftApp.graph.inject(this)
    }

    override fun onResume() {
        super.onResume()

        sessionManager.silentLogin()?.let { observable ->
            progressDialog = showProgressDialog("Signing in...")
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                            onNext = this::onLoginResult,
                            onError = { this.onLoginResult(LoginResult.Failure) },
                            onComplete = {})
        }
    }

    fun login(view: View) {
        progressDialog = showProgressDialog("Signing in...")
        sessionManager
                .login(usernameEntry.text.toString(), passwordEntry.text.toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onLoginResult) {
                    this.onLoginResult(LoginResult.Failure)
                }
    }

    private fun submitRegistration() {
        if (passwordInput.text.toString() == passwordConfirmationInput.text.toString()) {
            sessionManager.submitRegistration(
                    emailInput.text.toString(),
                    passwordInput.text.toString()
            ).observeOn(AndroidSchedulers.mainThread()).subscribeBy(
                    onNext = {
                        AlertDialog.Builder(this)
                                .setTitle("IMPORTANT")
                                .setMessage("You will soon receive a confirmation email. Please use that email to confirm your email address, then log in.")
                                .setPositiveButton("OK") { dialog, _ ->
                                    dialog.dismiss()
                                    switchToLogin()
                                }
                                .show()
                    },
                    onError = {
                        Toast.makeText(this, "Something went wrong. Please check your information, or try again later.", Toast.LENGTH_LONG).show()
                    }
            )
        }
    }

    fun onClickSignUpNext(view: View) {
        progressDialog = showProgressDialog("Checking registration...")
        sessionManager
                .checkRegistration(
                        emailInput.text.toString(),
                        firstNameInput.text.toString(),
                        lastNameInput.text.toString()
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            progressDialog?.dismiss()
                            when (it) {
                                MemberFound -> {
                                }
                                AlreadyRegistered -> {
                                    Toast.makeText(
                                            this@SignInActivity,
                                            "You are already registered. Please log in.",
                                            Toast.LENGTH_LONG).show()
                                    switchToLogin()
                                }
                                NonMember -> {
                                    moveToNonMemberSignUp()
                                }
                            }
                        },
                        onError = {
                            progressDialog?.dismiss()
                        },
                        onComplete = {

                        }
                )
    }

    private fun onLoginResult(result: LoginResult) {
        progressDialog?.dismiss()
        when (result) {
            LoginResult.Success -> {
                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            LoginResult.Failure -> {
                Toast.makeText(this, "Failed to sign in.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun switchToLogin() = hideViewsExcept(loginView)

    private fun switchToSignUp() = hideViewsExcept(signUpView)

    private fun moveToNonMemberSignUp() = hideViewsExcept(nonMemberSignUpView)

    private fun hideViewsExcept(view: View) {
        listOf(
                loginView,
                signUpView,
                nonMemberSignUpView
        ).forEach { it.visibility = View.GONE }

        view.visibility = View.VISIBLE
    }
}
