package com.example.projrctlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.example.projrctlogin.Adpater.StoryAdpater
import com.example.projrctlogin.Model.Story
import com.example.projrctlogin.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import jp.shts.android.storiesprogressview.StoriesProgressView
import kotlinx.android.synthetic.main.activity_story.*
import kotlin.properties.Delegates

class StoryActivity : AppCompatActivity(), StoriesProgressView.StoriesListener {

    var currentuserid: String = ""
    var userId: String = ""
    var counter = 0
    var pressTime = 0L
    var limit = 500L


    var imageList: List<String>? = null
    var storyIdsList: List<String>? = null

    var storiesProgressView: StoriesProgressView? = null

    private val onTouchListener = View.OnTouchListener { view, motionEvent ->

        when(motionEvent.action)
        {
            MotionEvent.ACTION_DOWN ->
            {
                pressTime = System.currentTimeMillis()
                storiesProgressView!!.pause()
                return@OnTouchListener false
            }

            MotionEvent.ACTION_UP ->
            {
                val now = System.currentTimeMillis()
                storiesProgressView!!.resume()
                return@OnTouchListener limit < now - pressTime
            }
        }

        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story)


        currentuserid = FirebaseAuth.getInstance().currentUser!!.uid
        userId = intent.getStringExtra("userId").toString()
       // counter = intent.getStringExtra("userId")!!.toInt()




        storiesProgressView = findViewById(R.id.stories_progress)

        layout_seen.visibility = View.GONE
        story_delete.visibility = View.GONE

        if (userId == currentuserid)
        {
            layout_seen.visibility = View.VISIBLE
            story_delete.visibility = View.VISIBLE
        }

        getStories(userId!!)
        userInfo(userId!!)

        val reverse: View = findViewById(R.id.reverse)
        reverse.setOnClickListener{storiesProgressView!!.reverse()}
        reverse.setOnTouchListener(onTouchListener)

        val skip: View = findViewById(R.id.skip)
        skip.setOnClickListener{storiesProgressView!!.skip()}
        skip.setOnTouchListener(onTouchListener)

        seen_number.setOnClickListener {
            val intent = Intent(this@StoryActivity, StorySeenActivity::class.java)
            intent.putExtra("id", userId)
            intent.putExtra("storyid", storyIdsList!![counter])
            intent.putExtra("title","views")
            startActivity(intent)
        }

        story_delete.setOnClickListener {
            val likeref = FirebaseDatabase.getInstance().reference
                .child("story")
                .child(userId!!)
                .child(storyIdsList!![counter])

            likeref.removeValue().addOnCompleteListener {task ->
            if (task.isSuccessful)
            {
                Toast.makeText(this,"Deleated....",Toast.LENGTH_LONG).show()
            }

            }
        }

    }


    private fun getStories(userId: String)
    {
        imageList = ArrayList()
        storyIdsList = ArrayList()


        val ref = FirebaseDatabase.getInstance().reference
            .child("story")
            .child(userId!!)

        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                (imageList as ArrayList<String>).clear()
                (storyIdsList as ArrayList<String>).clear()

                for (datasnapshot in snapshot.children)
                {
                    val story: Story? = datasnapshot.getValue<Story>(Story::class.java)
                    val timecurrent = System.currentTimeMillis()

                    if (timecurrent>story!!.getTimeStart() && timecurrent<story!!.getTimeEnd())
                    {
                        (imageList as ArrayList<String>).add(story.getImageUrl())
                        (storyIdsList as ArrayList<String>).add(story.getStoryId())
                    }
                }

                storiesProgressView!!.setStoriesCount((imageList as ArrayList<String>).size)
                storiesProgressView!!.setStoryDuration(6000L)
                storiesProgressView!!.setStoriesListener(this@StoryActivity)
                storiesProgressView!!.startStories(counter)
                Picasso.get().load(imageList!!.get(counter)).placeholder(R.drawable.profile).into(image_story)

                addViewToStory(storyIdsList!!.get(counter))
                seenNumber(storyIdsList!!.get(counter))

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun userInfo(userId: String)
    {
        val userRef = FirebaseDatabase.getInstance().getReference()
            .child("user").child(userId)

        userRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    val user = snapshot.getValue<User>(User::class.java)
                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile).into(stori_profile_image)

                    story_username.text = user.getEmail()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun addViewToStory(storyId: String)
    {
        FirebaseDatabase.getInstance().reference
            .child("story")
            .child(userId!!)
            .child(storyId)
            .child("views")
            .child(currentuserid)
            .setValue(true)
    }

    private fun seenNumber(storyId: String)
    {
        val ref = FirebaseDatabase.getInstance().reference
            .child("story")
            .child(userId!!)
            .child(storyId)
            .child("views")

        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                seen_number.text = "" + snapshot.childrenCount
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onNext() {

        Picasso.get().load(imageList!![++counter]).placeholder(R.drawable.profile).into(image_story)

        addViewToStory(storyIdsList!![counter])
        seenNumber(storyIdsList!![counter])

    }

    override fun onPrev() {
        if (counter - 1 < 0) return
        Picasso.get().load(imageList!![--counter]).placeholder(R.drawable.profile).into(image_story)
        seenNumber(storyIdsList!![counter])

    }

    override fun onDestroy() {
        super.onDestroy()
        storiesProgressView!!.destroy()
    }

    override fun onComplete() {
        finish()
    }

    override fun onResume() {
        super.onResume()
        storiesProgressView!!.resume()
    }

    override fun onPause() {
        super.onPause()
        storiesProgressView!!.pause()
    }

}