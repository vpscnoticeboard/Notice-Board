package com.example.projrctlogin.Fragrments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projrctlogin.Adpater.NotificationAdpater
import com.example.projrctlogin.Model.Notification
import com.example.projrctlogin.Model.User
import com.example.projrctlogin.R
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList


class NotificationFragment : Fragment() {

    private var notificationlist: List<Notification>? = null
    private var notificationAdpater: NotificationAdpater? = null

    lateinit var add: BottomNavigationItemView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view =  inflater.inflate(R.layout.fragment_notification, container, false)

        add = requireActivity().findViewById(R.id.navigation_add)
        add.visibility = View.GONE
        userInfo()

        var recyclerView: RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_notification)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)

        notificationlist = ArrayList()

        notificationAdpater = context?.let { NotificationAdpater(it, notificationlist as ArrayList<Notification>) }
        recyclerView.adapter = notificationAdpater

        readnotifications()

        return view
    }

    private fun readnotifications() {
        val notiref = FirebaseDatabase.getInstance().getReference()
            .child("notifications")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)

        notiref.addValueEventListener(object: ValueEventListener{

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists())
                {
                    (notificationlist as ArrayList<Notification>).clear()

                    for(snapshot in dataSnapshot.children)
                    {
                        val notification = snapshot.getValue(Notification::class.java)

                        (notificationlist as ArrayList<Notification>).add(notification!!)

                    }
                    Collections.reverse(notificationlist)
                    notificationAdpater!!.notifyDataSetChanged()
                }

            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
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