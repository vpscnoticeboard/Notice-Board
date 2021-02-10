package com.example.projrctlogin


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class Mobileotp : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    lateinit var mCallbacks:PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mobileotp)
        mAuth = FirebaseAuth.getInstance();
        val bundle: Bundle? = intent.extras
        val phoneNumber = bundle!!.getString("mobile")
        verify(phoneNumber.toString())
    }
    private fun verify(phonoNumber:String){
        verificationCallbacks()
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phonoNumber,60,TimeUnit.SECONDS,this,mCallbacks)

    }
    private fun verificationCallbacks(){
        mCallbacks=object :PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                TODO("Not yet implemented")
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                TODO("Not yet implemented")
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
            }
        }

    }
}