package com.cornixtech.trackingworld.utils

import java.util.regex.Matcher
import java.util.regex.Pattern
/**CREATED BY NAQI HASSAN 3/9/2020**/

object Constants {
    const val USER_SETTING = "APP_SETTINGS"
    const val USER_LATITUDE = "USER_LATITUDE"
    const val USER_LONGITUDE = "USER_LONGITUDE"
    const val API_KEY = "AIzaSyCc3H5Lakfig-VbqFKQ60z4mMu71HBjCHQ"


    fun isEmailValid(email: String?): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val EMAIL_PATTERN = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        pattern = Pattern.compile(EMAIL_PATTERN)
        matcher = pattern.matcher(email)
        return matcher.matches()
    }
}
