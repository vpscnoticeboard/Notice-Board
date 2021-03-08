package com.example.projrctlogin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.alpha
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.projrctlogin.Fragrments.*
import com.example.projrctlogin.Model.User
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_profile.view.*

class MainActivity : AppCompatActivity() {

    internal var selectedfragrment: Fragment? = null

    lateinit var add: BottomNavigationItemView


    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                moveToFragment(HomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_add -> {
               startActivity(Intent(this@MainActivity, AddPostActivity::class.java))
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                moveToFragment(ProfileFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notification -> {
                moveToFragment(NotificationFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_search -> {
                moveToFragment(SearchFragment())
                return@OnNavigationItemSelectedListener true
            }
        }

        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        add = findViewById(R.id.navigation_add)
        add.visibility = View.GONE

        userInfo()

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        moveToFragment(HomeFragment())
    }

        private fun moveToFragment(fragment: Fragment)
        {
            val fragmentTrans = supportFragmentManager.beginTransaction()
            fragmentTrans.replace(R.id.fragrment_container,fragment)
            fragmentTrans.commit()

        }

    private fun userInfo()
    {
        val userRef = FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().currentUser!!.uid)

        userRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    val user = snapshot.getValue<User>(User::class.java)
                    val typeofuser = user!!.getTypeofaccount()
                    if(typeofuser == "admin")
                    {
                        add.visibility = View.VISIBLE
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}