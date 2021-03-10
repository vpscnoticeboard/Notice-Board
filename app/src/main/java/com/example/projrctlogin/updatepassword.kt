package com.example.projrctlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.github.ybq.android.spinkit.SpinKitView
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator

class updatepassword : AppCompatActivity() {
    lateinit var username:TextView
   lateinit var currentpassword:EditText
   lateinit var newpassword:EditText
   lateinit var confirmpassword:EditText
    private lateinit var auth: FirebaseAuth
    lateinit var updatepass:Button
    lateinit var progressbar: SpinKitView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_updatepassword)
        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Change Password"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        auth = FirebaseAuth.getInstance()
       currentpassword=findViewById(R.id.current_password)
        newpassword=findViewById(R.id.new_password)
        confirmpassword=findViewById(R.id.confirm_password)
        updatepass=findViewById(R.id.update_password)
        progressbar=findViewById(R.id.progressbar)
        username=findViewById(R.id.username2)
        val usertitle = auth.currentUser
        //username.setText("hey! Welcome, $usertitle.email")
        if (usertitle != null) {
            username.text=usertitle.email
        }

        //btn on click
        updatepass.setOnClickListener {
            progressbar.visibility= View.VISIBLE
           var curentpass:Boolean= passwordcheck1(currentpassword)
            var newpass:Boolean=passwordcheck(newpassword,confirmpassword)
            if(curentpass and newpass) {
                changePassword()
            }
        }



    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        //return true
        (Intent(this, MainActivity::class.java))
        finish()
        return true
    }

    private fun changePassword() {
        val user = auth.currentUser
        if (user != null && user.email != null) {
            val credential = EmailAuthProvider
                .getCredential(user.email!!, currentpassword.text.toString())

// Prompt the user to re-provide their sign-in credentials
            user?.reauthenticate(credential)
                ?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Re-Authentication success.", Toast.LENGTH_SHORT).show()
                        user?.updatePassword(newpassword.text.toString())
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this, "Password changed successfully.", Toast.LENGTH_LONG).show()
                                    auth.signOut()
                                    startActivity(Intent(this, Loginpage::class.java))
                                    finish()
                                }
                            }

                    } else {
                        Toast.makeText(this, "current password not  match.", Toast.LENGTH_LONG).show()
                        currentpassword.setError("currenrpassword not match")
                    }
                }
        } else {
            startActivity(Intent(this, Loginpage::class.java))
            finish()
        }

    }
    fun passwordcheck(password: TextView, confirmpassword: TextView): Boolean {
        var pcheck: Boolean = false
        password.validator()
            .nonEmpty()
            .minLength(8)
            .atleastOneUpperCase()
            .atleastOneSpecialCharacters()
            .atleastOneNumber()
            .addErrorCallback {
                // Invalid password
                password.error = it
            }
            .addSuccessCallback {

                val pass = password.text.toString()
                val cpass = confirmpassword.text.toString()

                if (pass == cpass) {
                    pcheck = true
                } else {
                    confirmpassword.error = "Password Not Match"
                }
            }
            .check()
        return pcheck

    }
    fun passwordcheck1(password: TextView): Boolean {
        var pcheck: Boolean = false
        password.validator()
            .nonEmpty()
            .minLength(8)
            .atleastOneUpperCase()
            .atleastOneSpecialCharacters()
            .atleastOneNumber()
            .addErrorCallback {
                // Invalid password
                password.error = it
            }
            .addSuccessCallback {
                pcheck=true
            }
            .check()
        return pcheck

    }

    override fun onStart() {
        super.onStart()
        progressbar.visibility=View.INVISIBLE
    }
    override fun onResume() {
        super.onResume()
        progressbar.visibility=View.INVISIBLE
    }

}