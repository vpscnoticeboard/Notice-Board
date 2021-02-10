package com.example.projrctlogin

import com.example.projrctlogin.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.CollapsibleActionView
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.projrctlogin.MainActivity
import com.example.projrctlogin.Register
import com.github.ybq.android.spinkit.SpinKitView
import kotlinx.android.synthetic.main.activity_loginpage.view.*
import kotlinx.android.synthetic.main.activity_register.*

class Loginpage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_loginpage)
    }
    override fun onStart() {
        super.onStart()
        progressbar.visibility=View.INVISIBLE
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
                if(btnid.id == btnlogin!!.id)
                {
                        if(TextUtils.isEmpty(email.toString()))
                        {
                            email?.setError("Enterd E-mail must required")
                            return
                        }

                        if(TextUtils.isEmpty(password.toString()))
                        {
                            password?.setError("Enterd Password must required")
                            return
                        }
                    progressBar.visibility=View.VISIBLE
                    var intent=Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                if(btnid.id == btnsignup!!.id)
                {
                    progressBar.visibility=View.VISIBLE
                    var intent=Intent(applicationContext, Register::class.java)
                    startActivity(intent)//intent for sign up
                }
    }

}


