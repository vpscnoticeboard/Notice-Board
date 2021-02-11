package com.example.projrctlogin

import android.content.Intent
import android.icu.util.TimeUnit
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator


class Mobileverify : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId:String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var mobileno: EditText
    lateinit var getotp: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mobileverify)

        auth=FirebaseAuth.getInstance()
        val bundle: Bundle? = intent.extras
        val mobile = bundle!!.getString("mobile")
        mobileno = findViewById(R.id.phoneNumber)
        getotp = findViewById(R.id.otpBtn)
        mobileno.setText(mobile)


        getotp.setOnClickListener {
            if (mobilecheck(mobileno))
            {
                sentotp()
            }

        }

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                startActivity(Intent(applicationContext, Loginpage::class.java))
                finish()
            }

            override fun onVerificationFailed(e: FirebaseException) {
               mobileno.setError("PROBLEM TO VERIFY")
                Toast.makeText(applicationContext, "Failed", Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {

                Log.d("TAG","onCodeSent:$verificationId")
                storedVerificationId = verificationId
                resendToken = token

                var intent = Intent(applicationContext,Mobileotp::class.java)
                intent.putExtra("storedVerificationId",storedVerificationId)
                startActivity(intent)
                finish()
            }
        }

    }
    private fun sentotp() {
        //val mobileNumber=findViewById<EditText>(R.id.phoneNumber)
        var number=mobileno.text.toString().trim()

        if(!number.isEmpty()){
            number="+91"+number
            sendVerificationcode (number)
        }else{
            Toast.makeText(this,"Enter mobile number",Toast.LENGTH_SHORT).show()
        }
    }
    private fun sendVerificationcode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, java.util.concurrent.TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
    //FUNCTION CHECK FOR MOBILENUMBER
    fun mobilecheck(mobile: EditText): Boolean {
        var mobilecheck: Boolean = false
        mobile.validator()
            .validNumber()
            .nonEmpty()
            .addErrorCallback { mobile.error = it }
            .addSuccessCallback {
                mobilecheck = true
            }
            .check()
        return mobilecheck
    }

}