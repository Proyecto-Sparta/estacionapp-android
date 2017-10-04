package com.sparta.estacionapp.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView

import com.sparta.estacionapp.R
import com.sparta.estacionapp.rest.DriverService

import kotlinx.android.synthetic.main.activity_login.*

/**
 * A login screen that offers login via email/password.
 */
class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        if (loggedIn()) {
            logIn()
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // Set up the login form.

        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })

        sign_in_button.setOnClickListener { attemptLogin() }
    }

    private fun loggedIn(): Boolean {
        return getSharedPreferences(getString(R.string.shared_fike), Context.MODE_PRIVATE).contains("jwt")
    }

    private fun saveToken(token: String) {
        getSharedPreferences(getString(R.string.shared_fike), Context.MODE_PRIVATE)
                .edit()
                .putString("jwt", token)
                .commit()
    }

    private fun logIn() {
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
        finish()
    }


    private fun attemptLogin() {

        // Reset errors.
        username.error = null
        password.error = null

        // Store values at the time of the login attempt.
        val emailStr = username.text.toString()
        val passwordStr = password.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(passwordStr) && !isPasswordValid(passwordStr)) {
            password.error = getString(R.string.error_invalid_password)
            focusView = password
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(emailStr)) {
            username.error = getString(R.string.error_field_required)
            focusView = username
            cancel = true
        } else if (!isEmailValid(emailStr)) {
            username.error = getString(R.string.error_invalid_email)
            focusView = username
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true)
            val loginDigest = "Basic Q2hyaXMgTWNDb3JkOnBhc3N3b3Jk"
            DriverService(this).login(loginDigest, { token ->
                saveToken(token)
                logIn()
            }, { _ -> showProgress(false) })
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return email.length > 5
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.

        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        login_form.visibility = if (show) View.GONE else View.VISIBLE
        login_form.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 0 else 1).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        login_form.visibility = if (show) View.GONE else View.VISIBLE
                    }
                })

        login_progress.visibility = if (show) View.VISIBLE else View.GONE
        login_progress.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        login_progress.visibility = if (show) View.VISIBLE else View.GONE
                    }
                })

    }
}
