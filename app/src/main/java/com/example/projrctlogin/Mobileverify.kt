package com.example.projrctlogin

import android.content.Intent
import android.icu.util.TimeUnit
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider


class Mobileverify : AppCompatActivity() {

    lateinit var mobileno: EditText
    lateinit var getotp: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mobileverify)
        val bundle: Bundle? = intent.extras
        val mobile = bundle!!.getString("mobile")
        mobileno = findViewById(R.id.mobileno)
        getotp = findViewById(R.id.otpbtn)
        mobileno.setText(mobile)
        getotp.setOnClickListener {
            val mobile:String="+91"+mobileno.text.toString()
            val intent = Intent(applicationContext, Mobileotp::class.java)
            intent.putExtra("mobile",mobile)
            startActivity(intent)
            finish()
        }
    }

}