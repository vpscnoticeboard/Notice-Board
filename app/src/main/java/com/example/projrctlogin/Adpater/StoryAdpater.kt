package com.example.projrctlogin.Adpater

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.projrctlogin.AddStoryActivity
import com.example.projrctlogin.MainActivity
import com.example.projrctlogin.Model.Story
import com.example.projrctlogin.Model.User
import com.example.projrctlogin.R
import com.example.projrctlogin.StoryActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile.view.*

class StoryAdpater (private val mcontext: Context, private val mStory: List<Story>):
RecyclerView.Adapter<StoryAdpater.ViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == 0)
        {
            val view = LayoutInflater.from(mcontext).inflate(R.layout.add_story_items, parent,false)
            ViewHolder(view)
        }
        else
        {
            val view = LayoutInflater.from(mcontext).inflate(R.layout.story_items, parent,false)
            ViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return mStory.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = mStory[position]

        userInfo(holder, story.getUserId(), position)

        if (holder.adapterPosition !== 0)
        {
            seenstory(holder, story.getUserId())
        }

        if (holder.adapterPosition === 0)
        {
            myStories(holder.addstory_text!!, holder.story_plus_btn!!, false)
        }

        holder.itemView.setOnClickListener {
            if (holder.adapterPosition === 0)
            {
                myStories(holder.addstory_text!!, holder.story_plus_btn!!, true )
            }
            else
            {
                val intent = Intent(mcontext, StoryActivity::class.java)
                intent.putExtra("userid", story.getUserId())
                mcontext.startActivity(intent)
            }

        }

    }

    inner class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        //storyitem
        var story_image_seen: ImageView? = null
        var story_image: ImageView? = null
        var story_username: TextView? = null

        //add story item
        var story_plus_btn: ImageView? = null
        var addstory_text: TextView? = null

        init {
            story_image_seen = itemView.findViewById(R.id.story_image_seen)
            story_image = itemView.findViewById(R.id.story_image)
            story_username = itemView.findViewById(R.id.story_username)

            //add story item
            story_plus_btn = itemView.findViewById(R.id.story_add)
            addstory_text = itemView.findViewById(R.id.add_story_text)
        }

    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0)
        {
            return 0
        }
        return 1
    }

    private fun userInfo(viewHolder: ViewHolder, userId: String, position: Int)
    {
        val userRef = FirebaseDatabase.getInstance().getReference().child("user").child(userId)

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    val user = snapshot.getValue<User>(User::class.java)
                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile).into(viewHolder.story_image)

                    if (position!=0)
                    {
                        Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile).into(viewHolder.story_image_seen)
                        viewHolder.story_username!!.text = user.getEmail()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun myStories(textView: TextView, imageView: ImageView, click: Boolean)
    {
        val storyref = FirebaseDatabase.getInstance().reference
            .child("story")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)

        storyref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var counter = 0
                val timecurrent = System.currentTimeMillis()
                for (snapshot in dataSnapshot.children)
                {
                   var story = snapshot.getValue(Story::class.java)

                    if (timecurrent>story!!.getTimeStart() && timecurrent<story!!.getTimeEnd())
                    {
                        counter++
                    }
                }
                if (click) {
                    if (counter > 0) {
                        val alertDialog = AlertDialog.Builder(mcontext).create()

                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "View Story")
                        { dialogInterface, which ->
                            val intent = Intent(mcontext, StoryActivity::class.java)
                            intent.putExtra("userId", FirebaseAuth.getInstance().currentUser!!.uid)
                            mcontext.startActivity(intent)
                            dialogInterface.dismiss()
                        }

                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Add Story")
                        { dialogInterface, which ->
                            val intent = Intent(mcontext, AddStoryActivity::class.java)
                            intent.putExtra("userId", FirebaseAuth.getInstance().currentUser!!.uid)
                            mcontext.startActivity(intent)
                            dialogInterface.dismiss()
                        }
                        alertDialog.show()
                    }
                    else
                    {
                        val intent = Intent(mcontext, AddStoryActivity::class.java)
                        intent.putExtra("userId", FirebaseAuth.getInstance().currentUser!!.uid)
                        mcontext.startActivity(intent)
                    }
                }
                else {
                    if (counter > 0) {
                        textView.text = "My Story"
                        imageView.visibility = View.GONE
                    } else {
                        textView.text = "Add Story"
                        imageView.visibility = View.VISIBLE
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun seenstory(viewHolder: ViewHolder,userId: String)
    {
        val storyref = FirebaseDatabase.getInstance().reference
            .child("story")
            .child(userId)

        storyref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var i = 0
                for (datasnapshot in snapshot.children)
                {
                    if (!datasnapshot.child("views").child(FirebaseAuth.getInstance().currentUser!!.uid).exists() && System.currentTimeMillis() < datasnapshot.getValue(Story::class.java)!!.getTimeEnd())
                    {
                        i = i+1
                    }
                }
                if (i > 0)
                {
                    viewHolder.story_image!!.visibility = View.VISIBLE
                    viewHolder.story_image_seen!!.visibility = View.GONE
                }
                else
                {
                    viewHolder.story_image!!.visibility = View.GONE
                    viewHolder.story_image_seen!!.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}