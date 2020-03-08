package com.cornixtech.trackingworld

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import io.realm.Realm

/**CREATED BY NAQI HASSAN 3/9/2020**/
class TrackingWorld : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/spartan-regular.ttf")
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}