package com.example.projrctlogin

import android.app.ProgressDialog
import com.example.projrctlogin.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.CollapsibleActionView
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.projrctlogin.MainActivity
import com.example.projrctlogin.Register
import com.github.ybq.android.spinkit.SpinKitView
import com.google.firebase.auth.FirebaseAuth
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_loginpage)

        emailv=findViewById(R.id.loginid)
        passwordv=findViewById(R.id.passwordlogin)
        progressBar=findViewById(R.id.progressbar)


        btnlogin.setOnClickListener {
            val emails:Boolean=emailcheck(emailv)
            val passwords:Boolean=passwordcheck(passwordv)

           if(emails and passwords) {

                signinuser()
            }
        }

    }

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
                    Toast.makeText(this,"Error : $message",Toast.LENGTH_SHORT).show()
                    FirebaseAuth.getInstance().signOut()
                    ProgressDialog.dismiss()
                    progressbar.visibility=View.INVISIBLE
                }
            }

    }


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


    override fun onStart() {
        super.onStart()
        progressbar.visibility=View.INVISIBLE

        //for checking that current user is login or not
        if (FirebaseAuth.getInstance().currentUser !=null)
        {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    override fun onResume() {
        super.onResume()
        progressbar.visibility=View.INVISIBLE
    }


    fun btnclick(view: View){
       val btnlogin:Button?=findViewById(R.id.btnlogin)
        val btnsignup:Button?=findViewById(R.id.btnsignup)
        val email:TextView?=findViewById(R.id.loginid)
        val progressBar:SpinKitView=findViewById(R.id.progressbar)
        val password:TextView?=findViewById(R.id.password)
            val btnid=view as Button
                if(btnid.id == btnsignup!!.id)
                {
                    progressBar.visibility=View.VISIBLE
                    handler= Handler()
                    handler.postDelayed({
                    var intent=Intent(applicationContext, Register::class.java)
                    startActivity(intent)//intent for sign up
                    },3000) }
    }

}


