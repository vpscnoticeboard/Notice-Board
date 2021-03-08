package com.example.projrctlogin

import android.app.ProgressDialog
import com.example.projrctlogin.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.CollapsibleActionView
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.projrctlogin.MainActivity
import com.example.projrctlogin.Model.User
import com.example.projrctlogin.Register
import com.github.ybq.android.spinkit.SpinKitView
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import kotlinx.android.synthetic.main.activity_loginpage.*
import kotlinx.android.synthetic.main.activity_loginpage.view.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.progressbar

class Loginpage : AppCompatActivity() {

    lateinit var emailv : EditText
    lateinit var passwordv: TextView
    lateinit var handler: Handler
    lateinit var progressBar : SpinKitView
    lateinit var btnlogin:Button
    lateinit var btnsignup:Button
    lateinit var forgatpass:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_loginpage)
        btnlogin=findViewById(R.id.btnlogin)
         btnsignup=findViewById(R.id.btnsignup)
        emailv=findViewById(R.id.loginid)
        passwordv=findViewById(R.id.passwordlogin)
        progressBar=findViewById(R.id.progressbar)
        forgatpass=findViewById(R.id.forgatpass)


        btnlogin.setOnClickListener {
            val emails:Boolean=emailcheck(emailv)
            val passwords:Boolean=passwordcheck(passwordv)
            if (emails and passwords) {
                progressBar.visibility=View.VISIBLE
                signinuser()
            }
        }

        btnsignup.setOnClickListener {
            progressBar.visibility=View.VISIBLE
            val intent=Intent(applicationContext, Register::class.java)
            startActivity(intent)//intent for sign up
        }


    }


    /*fun btnclick(view: View){
//        val emails:Boolean=emailcheck(emailv)
//        val passwords:Boolean=passwordcheck(passwordv)

        val btnid=view as Button
//            if(btnid.id == btnlogin.id) {
//
//                    if (emails and passwords) {
//                        progressBar.visibility=View.VISIBLE
//                        signinuser()
//                    }
//            }
        if(btnid.id == btnsignup.id)
        {
            progressBar.visibility=View.VISIBLE
           // handler= Handler()
            //handler.postDelayed({
                val intent=Intent(applicationContext, Register::class.java)
                startActivity(intent)//intent for sign up
           // },3000)
        }
    }*/

    private fun signinuser() {

        progressBar.visibility=View.VISIBLE
        val email=loginid.text.toString()
        val password=passwordlogin.text.toString()

        //progress Dialog
        val ProgressDialog = ProgressDialog(this)
        ProgressDialog.setTitle("Signup")
        ProgressDialog.setMessage("please wait this may take a while....")
        ProgressDialog.setCanceledOnTouchOutside(false)

        //firebase auth variable
        val maut : FirebaseAuth = FirebaseAuth.getInstance()

        maut.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    ProgressDialog.dismiss()
                    Toast.makeText(this,"Suscessfully Logged In...",Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else {
                    val message=task.exception.toString()
                    Log.d(message,message)
                    Toast.makeText(this,"Error : $message",Toast.LENGTH_LONG).show()
                     handler= Handler()
                    handler.postDelayed({
                        //Toast.makeText(this,"PLEASE SIGN_UP FIRST",Toast.LENGTH_LONG).show()
                        FirebaseAuth.getInstance().signOut()
                    ProgressDialog.dismiss()
                    forgatpass.visibility=View.VISIBLE
                    },5000)
                    progressbar.visibility=View.INVISIBLE
                }
            }

    }





    override fun onStart() {
        super.onStart()
        progressbar.visibility=View.INVISIBLE

        //for checking that current user is login or not
        if (FirebaseAuth.getInstance().currentUser !=null)
        {
            val intent = Intent(this@Loginpage, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

    }

    override fun onResume() {
        super.onResume()
        progressbar.visibility=View.INVISIBLE
    }



//e-mail check validation
    fun emailcheck(email: TextView): Boolean {
        var emailcheck: Boolean = false
        email.validator()
            .nonEmpty()
            .validEmail()
            .addErrorCallback {
                // Invalid email
                email.error = it
            }
            .addSuccessCallback {
                // call Login webservice here or anything else for success usecase
                emailcheck = true
            }
            .check()
        return emailcheck
    }

//password check validation
    fun passwordcheck(password: TextView): Boolean {
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

    fun Resetpass(view: View) {
        progressbar.visibility=View.VISIBLE
        val intent = Intent(applicationContext, ResetPassword::class.java)
        startActivity(intent)
        finish()

    }


}


