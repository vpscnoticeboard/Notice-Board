package com.example.projrctlogin

import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.alpha
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.noticeboardclone.Fragrments.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    internal var selectedfragrment: Fragment? = null

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                moveToFragment(HomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_add -> {
                moveToFragment(AddFragment())
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

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        moveToFragment(HomeFragment())
    }

        private fun moveToFragment(fragment: Fragment)
        {
            val fragmentTrans = supportFragmentManager.beginTransaction()
            fragmentTrans.replace(R.id.fragrment_container,fragment)
            fragmentTrans.commit()

        }

}