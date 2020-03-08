package com.cornixtech.trackingworld.activities

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Path
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.cornixtech.trackingworld.R
import com.cornixtech.trackingworld.utils.Constants
import com.cornixtech.trackingworld.utils.UserManager
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_login)

        Handler().postDelayed(
            {
                customLayout.visibility = View.VISIBLE
                val path = Path().apply {
                    arcTo(0f, 0f, 0f, 20000f, 0f, -90f, true)
                }
                val animator = ObjectAnimator.ofFloat(customLayout, View.X, View.Y, path).apply {
                    duration = 3000
                    start()
                }
            }, (2000).toLong()
        )
        setListeners()

    }

    private fun setListeners() {
        etEmailField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!Constants.isEmailValid(etEmailField.text.toString()))
                    emailInputLayout.error = "Please enter valid email address"
                else {
                    emailInputLayout.error = null
                    if (etPassword.text.toString()
                            .isNotEmpty() && etPassword.text.toString().length >= 5
                    )
                        fabNextButton.show()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!Constants.isEmailValid(etEmailField.text.toString()))
                    emailInputLayout.error = "Please enter valid email address"
                else {
                    emailInputLayout.error = null
                    if (etPassword.text.toString()
                            .isNotEmpty() && etPassword.text.toString().length >= 5
                    )
                        fabNextButton.show()
                }
            }
        })
        etPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (etPassword.text.toString().length < 5) {
                    passwordInputLayout.error = "Invalid password"
                    fabNextButton.hide()
                } else {
                    passwordInputLayout.error = null
                    if (Constants.isEmailValid(etEmailField.text.toString()))
                        fabNextButton.show()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (etPassword.text.toString().length < 5) {
                    passwordInputLayout.error = "Invalid password"
                    fabNextButton.hide()
                } else {
                    passwordInputLayout.error = null
                    if (Constants.isEmailValid(etEmailField.text.toString()))
                        fabNextButton.show()
                }
            }
        })
        signUpText.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        fabNextButton.setOnClickListener {
            loginUser(etEmailField.text.toString(), etPassword.text.toString())
        }
    }

    private fun loginUser(userEmail: String, userPassword: String) {
        val userId = UserManager.instance.doLoginUser(userEmail, userPassword)?.userId ?: ""
        if (userId.isNotEmpty()) {
            UserManager.instance.setLoggedInUserId(userId)
            startActivity(Intent(this@LoginActivity, MapsActivity::class.java))
        }else{
            Toast.makeText(this@LoginActivity, "Login credential not correct", Toast.LENGTH_SHORT).show()
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }


}
