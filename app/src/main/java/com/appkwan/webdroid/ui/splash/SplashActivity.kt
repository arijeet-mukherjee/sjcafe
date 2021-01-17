package com.appkwan.webdroid.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.appkwan.webdroid.R
import com.appkwan.webdroid.ui.MainActivity
import com.github.loadingview.LoadingDialog
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()


        val dialog = LoadingDialog.get(this).show()
        Handler().postDelayed({
            //goto homepage

            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
            dialog.hide()
        }, 3000)
    }
}
