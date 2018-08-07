package io.grandlabs.ift.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import io.grandlabs.ift.IftApp
import io.grandlabs.ift.MainActivity
import io.grandlabs.ift.R
import io.grandlabs.ift.network.LoginManager
import io.grandlabs.ift.network.LoginResult
import io.grandlabs.ift.showProgressDialog
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_sign_in.*
import javax.inject.Inject

class SignInActivity
    : AppCompatActivity() {

    val LOG_TAG = SignInActivity::class.simpleName

    @Inject
    lateinit var loginManager: LoginManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        IftApp.graph.inject(this)
    }

    fun login(view: View) {
        val progressDialog = showProgressDialog("Signing in...")
        loginManager.login(usernameEntry.text.toString(), passwordEntry.text.toString())
                .subscribeBy(onNext = {
                    progressDialog.dismiss()
                    when (it) {
                        LoginResult.Success -> {
                            intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }
                        LoginResult.Failure -> {}
                    }
                })
    }


}
