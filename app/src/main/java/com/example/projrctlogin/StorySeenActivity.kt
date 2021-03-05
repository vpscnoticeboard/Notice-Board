package com.example.projrctlogin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projrctlogin.Adpater.UserAdpater
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.auth.User

class StorySeenActivity : AppCompatActivity() {

    var id: String = ""
    var title: String = ""

    var userAdpater: UserAdpater? = null
    var userlist: List<User>? = null
    var idlist: List<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story_seen)

        val intent = intent
        id = intent.getStringExtra("id")!!
        title = intent.getStringExtra("title")!!


        var recyclerView: RecyclerView
        recyclerView = findViewById(R.id.recycler_view1)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        userlist = ArrayList()
        userAdpater = UserAdpater(this,userlist as ArrayList<com.example.projrctlogin.Model.User>, false)
        recyclerView.adapter = userAdpater
        idlist = ArrayList()


        getlikes()
    }

    private fun getlikes() {
        val likeref = FirebaseDatabase.getInstance().reference
            .child("story")
            .child(id!!)
            .child(intent.getStringExtra("storyid")!!)
            .child("views")

        likeref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    (idlist as ArrayList<String>).clear()

                    for (datasnapshot in snapshot.children)
                    {
                        (idlist as ArrayList<String>).add(datasnapshot.key!!)
                    }
                    showusers()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun showusers() {
        val userref = FirebaseDatabase.getInstance().getReference().child("user")
        userref.addValueEventListener(object  : ValueEventListener{
            override fun onDataChange(datasnapshot: DataSnapshot) {
                (userlist as ArrayList<com.example.projrctlogin.Model.User>).clear()
                for (snapshot in datasnapshot.children)
                {
                    val user = snapshot.getValue(com.example.projrctlogin.Model.User::class.java)
                    for (id in idlist!!)
                    {
                        if (user!!.getUid() == id)
                        {
                            (userlist as ArrayList<com.example.projrctlogin.Model.User>).add(user!!)
                        }
                    }
                }

                userAdpater?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}