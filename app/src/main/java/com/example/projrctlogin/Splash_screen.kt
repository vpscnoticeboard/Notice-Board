package com.example.projrctlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.ImageView
import com.airbnb.lottie.LottieAnimationView
import kotlinx.android.synthetic.main.activity_splash_screen.*


class Splash_screen : AppCompatActivity() {

    lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash_screen)
        var logo: ImageView =findViewById(R.id.logo)
        var backimage: ImageView =findViewById(R.id.backimage)
        var animation_view: LottieAnimationView =findViewById(R.id.animation_view)
        logo.animate().translationX(1400F).setDuration(1000).setStartDelay(3000)
        animation_view.animate().translationY(1400F).setDuration(1000).setStartDelay(3000)
        backimage.animate().translationY(-1600f).setDuration(1000).setStartDelay(4000)
        handler= Handler()
        handler.postDelayed({

            var intent = Intent(applicationContext,Loginpage::class.java)
            startActivity(intent)
            finish()

        },5000 )

    }
}