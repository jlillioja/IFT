package io.grandlabs.ift.login

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import io.grandlabs.ift.R
import io.grandlabs.ift.network.LoginManager
import kotlinx.android.synthetic.main.activity_sign_in.*
import android.app.ProgressDialog
import android.content.Intent
import io.grandlabs.ift.IftApp
import io.grandlabs.ift.MainActivity
import io.grandlabs.ift.network.LoginResult
import io.reactivex.rxkotlin.subscribeBy
import java.util.logging.LogManager
import javax.inject.Inject


//import javax.inject.Inject

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
        val progressDialog = showProgressDialog()
        // To dismiss the dialog
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

    private fun showProgressDialog(): ProgressDialog {
        val progress = ProgressDialog(this)
        progress.setTitle("Signing In")
//        progress.setMessage("Wait while loading...")
        progress.setCancelable(false) // disable dismiss by tapping outside of the dialog
        progress.show()
        return progress
    }
}
