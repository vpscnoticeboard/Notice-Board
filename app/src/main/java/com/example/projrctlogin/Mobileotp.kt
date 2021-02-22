package com.example.projrctlogin


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class Mobileotp : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var otpGiven:com.chaos.view.PinView
    lateinit var verify:Button
    lateinit var progressbar:com.github.ybq.android.spinkit.SpinKitView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mobileotp)

        auth=FirebaseAuth.getInstance()
        progressbar=findViewById(R.id.progressbar)
        val storedVerificationId=intent.getStringExtra("storedVerificationId")

         verify=findViewById(R.id.verifyBtn)
         otpGiven=findViewById(R.id.id_otp)


        verify.setOnClickListener{
            progressbar.visibility= View.VISIBLE
            var otp=otpGiven.text.toString().trim()
            if(!otp.isEmpty()){
                val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(
                    storedVerificationId.toString(), otp)
                signInWithPhoneAuthCredential(credential)
                FirebaseAuth.getInstance().signOut()
            }else{
                progressbar.visibility= View.INVISIBLE
                otpGiven.setError("FIRST ENTER OTP")
                Toast.makeText(this,"Enter OTP", Toast.LENGTH_LONG).show()
            }
        }

    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this@Mobileotp, Loginpage::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
// ...
                } else {
                    progressbar.visibility= View.INVISIBLE
// Sign in failed, display a message and update the UI
                    //Toast.makeText(this,"Make sure sim in the same Mobile Phone", Toast.LENGTH_SHORT).show()
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
// The verification code entered was invalid
                        otpGiven.setError("Invalid OTP")
                        Toast.makeText(this,"Invalid OTP", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}