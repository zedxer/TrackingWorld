package com.cornixtech.trackingworld.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.cornixtech.trackingworld.R
import com.cornixtech.trackingworld.utils.Constants
import com.cornixtech.trackingworld.utils.UserManager
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_sign_up)
        setListeners()
    }

    private fun setListeners() {
        ivBackButton.setOnClickListener { onBackPressed() }
        fabNextButton.setOnClickListener {
            if (isDataValid()) {
                signUpTheUser(
                    etNameField.text.toString(),
                    etEmailField.text.toString(),
                    etPassword.text.toString(),
                    System.currentTimeMillis()
                )
            }
        }
    }

    private fun signUpTheUser(
        userName: String,
        userEmail: String,
        userPassword: String,
        userCreationTime: Long
    ) {
        val userId = UserManager.instance.addObjectInDatabase(
            userName,
            userEmail,
            userPassword,
            userCreationTime
        )
        if (userId.trim().isNotEmpty()) {
            UserManager.instance.setLoggedInUserId(userId ?: "")
            Toast.makeText(this@SignUpActivity, "User successfully created", Toast.LENGTH_SHORT)
                .show()
            startActivity(Intent(this, MapsActivity::class.java))
            finish()
        }
    }

    private fun isDataValid(): Boolean {

        if (etNameField.text.toString().trim().isEmpty() && etNameField.text.trim().length <= 2) {
            nameInputLayout.error = "Please enter name"
            return false
        } else {
            nameInputLayout.error = null
        }
        if (etEmailField.text.toString().trim().isEmpty()) {
            emailInputLayout.error = "Please enter email"
            return false
        } else if (!Constants.isEmailValid(etEmailField.text.toString())) {
            emailInputLayout.error = "Please enter valid email"
            return false
        } else {
            emailInputLayout.error = null
        }
        if (etPassword.text.trim().isEmpty()) {
            passwordInputLayout.error = "Please enter name"
            return false
        } else if (etPassword.text.length <= 5) {
            passwordInputLayout.error = "Password must be greater than 5 characters"
            return false
        } else {
            passwordInputLayout.error = null
        }

        return true
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }
}
