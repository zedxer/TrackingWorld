package com.cornixtech.trackingworld

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.cornixtech.trackingworld.activities.LoginActivity
import com.cornixtech.trackingworld.activities.MapsActivity

class MainActivity : AppCompatActivity() {
/**THIS IS JUST A SAMPLE ACTIVITY ALL THE WORK IS DONE IN THE ACTIVITIES PACKAGE**/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler().postDelayed(
            {
                //changed here
                startActivity(Intent(this, LoginActivity::class.java))
                this.finish()
            }, (50).toLong()
        )
    }



}
