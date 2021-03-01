package com.example.projrctlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projrctlogin.Adpater.CommentsAdpater
import com.example.projrctlogin.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_account_setting2.*
import kotlinx.android.synthetic.main.activity_comments.*
import kotlinx.android.synthetic.main.posts_layout.*
import org.w3c.dom.Comment
import java.lang.NullPointerException

class CommentsActivity : AppCompatActivity() {

    private var postid = ""
    private var getpublisher = ""
    private var firebaseUser: FirebaseUser? = null
    private var commentAdpater: CommentsAdpater? = null
    private var commentList: MutableList<com.example.projrctlogin.Model.Comment>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)

        val intent = intent
        postid = intent.getStringExtra("postid")!!
        getpublisher = intent.getStringExtra("getpublisher")!!


        firebaseUser = FirebaseAuth.getInstance().currentUser

        var recyclerView: RecyclerView
        recyclerView = findViewById(R.id.recycler_view_comments)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.reverseLayout = true
        recyclerView.layoutManager = linearLayoutManager

        commentList = ArrayList()
        commentAdpater = CommentsAdpater(this, commentList)
        recyclerView.adapter = commentAdpater


        userInfo()
        readcomment()
        getpostimage()

        post_comment.setOnClickListener(View.OnClickListener {
            if (add_comment.text.toString() == "")
            {
                Toast.makeText(this,"Please Write Some Thing",Toast.LENGTH_LONG).show()
            }
            else
            {
                addcomment()
            }
        })


    }

    private fun addcomment() {
        val commentsRef = FirebaseDatabase.getInstance().getReference()
            .child("comments")
            .child(postid!!)

        val commentsMap = HashMap<String, Any>()
        commentsMap["comment"] = add_comment.text.toString()
        commentsMap["publisher"] = firebaseUser!!.uid

        commentsRef.push().setValue(commentsMap)

        addNotification()

        add_comment!!.text.clear()
    }


    private fun userInfo()
    {
        val userRef = FirebaseDatabase.getInstance().getReference().child("user").child(firebaseUser!!.uid)

        userRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    val user = snapshot.getValue<User>(User::class.java)
                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile).into(profile_image_comment)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


    private fun getpostimage()
    {
        val postRef = FirebaseDatabase.getInstance().getReference()
            .child("posts")
            .child(postid!!).child("postimage")

        postRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    val image = snapshot.value.toString()
                    Picasso.get().load(image).placeholder(R.drawable.profile).into(post_image_comment)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun readcomment()
    {
        val commentsRef = FirebaseDatabase.getInstance().getReference()
            .child("comments")
            .child(postid!!)

        commentsRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    commentList!!.clear()
                }

                for (datasnapshot in snapshot.children)
                {
                    val comment = datasnapshot.getValue(com.example.projrctlogin.Model.Comment::class.java)
                    commentList!!.add(comment!!)

                }
                commentAdpater!!.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

    private fun addNotification()
    {
        val notiref = FirebaseDatabase.getInstance().getReference()
            .child("notifications")
            .child(getpublisher!!)

        val notiMap = HashMap<String, Any>()
        notiMap["userid"] = firebaseUser!!.uid
        notiMap["text"] = "Commented: " + add_comment!!.text.toString()
        notiMap["postid"] = postid
        notiMap["ispost"] = true

        notiref.push().setValue(notiMap)

    }

}