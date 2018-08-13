package io.grandlabs.ift.login

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import io.grandlabs.ift.IftApp
import io.grandlabs.ift.MainActivity
import io.grandlabs.ift.R
import io.grandlabs.ift.network.LoginManager
import io.grandlabs.ift.network.LoginResult
import io.grandlabs.ift.showProgressDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_sign_in.*
import javax.inject.Inject

class SignInActivity
    : AppCompatActivity() {

    val LOG_TAG = SignInActivity::class.simpleName

    @Inject
    lateinit var loginManager: LoginManager

    var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        IftApp.graph.inject(this)
    }

    override fun onResume() {
        super.onResume()

        loginManager.silentLogin()?.let {
            progressDialog = showProgressDialog("Signing in...")
            it.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            this::onLoginResult,
                            { this.onLoginResult(LoginResult.Failure) }
                    )
        }
    }

    fun login(view: View) {
        progressDialog = showProgressDialog("Signing in...")
        loginManager
                .login(usernameEntry.text.toString(), passwordEntry.text.toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::onLoginResult,
                        { this.onLoginResult(LoginResult.Failure) }
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
}
