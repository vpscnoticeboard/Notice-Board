package com.example.projrctlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.github.ybq.android.spinkit.SpinKitView
import com.google.firebase.auth.FirebaseAuth

class ResetPassword : AppCompatActivity() {
    lateinit var progressbar:SpinKitView
    lateinit var button_reset_password:Button
    lateinit var text_email:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "RESET PASSWORD"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        //R.id set

        progressbar=findViewById(R.id.progressbar)
        button_reset_password=findViewById(R.id.update_password)
        text_email=findViewById(R.id.newpassword)

        // on click of password reset
        button_reset_password.setOnClickListener {
            val email = text_email.text.toString().trim()

            if (email.isEmpty()) {
                text_email.error = "Email Required"
                text_email.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                text_email.error = "Valid Email Required"
                text_email.requestFocus()
                return@setOnClickListener
            }

            progressbar.visibility = View.VISIBLE

            FirebaseAuth.getInstance()
                .sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    progressbar.visibility = View.GONE
                    if (task.isSuccessful) {
                        Toast.makeText(this,"CHECK YOUR E-MAIL FOR RESET PASSWORD LINK",Toast.LENGTH_LONG).show()
                        startActivity(Intent(applicationContext, Loginpage::class.java))
                        finish()
                    } else {
                        progressbar.visibility = View.INVISIBLE
                        Toast.makeText(this,task.exception?.message!!,Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}