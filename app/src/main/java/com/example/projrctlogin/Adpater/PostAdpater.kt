package com.example.projrctlogin.Adpater

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.projrctlogin.CommentsActivity
import com.example.projrctlogin.MainActivity
import com.example.projrctlogin.Model.Post
import com.example.projrctlogin.Model.User
import com.example.projrctlogin.R
import com.example.projrctlogin.LikesActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class PostAdpater(private val mContext : Context,
                  private val mPost : List<Post>) : RecyclerView.Adapter<PostAdpater.ViewHolder>()
{
            private var firebaseuser: FirebaseUser? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(mContext).inflate(R.layout.posts_layout, parent,false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            firebaseuser =FirebaseAuth.getInstance().currentUser

            val post = mPost[position]

            Picasso.get().load(post.getpostimage()).into(holder.postImage)

            if (post.getdiscription().equals(""))
            {
                holder.discription.visibility = View.GONE
            }
            else
            {
                holder.discription.visibility = View.VISIBLE
                holder.discription.setText(post.getdiscription())

            }

            publisherinfo(holder.profileImage, holder.userName, holder.publisher, post.getpublisher())

            islikes(post.getpostid(), holder.likeButton)
            numberoflikes(holder.likes, post.getpostid())
            gettotalcomments(holder.comments, post.getpostid())
            checkSavedStatus(post.getpostid(), holder.saveButton)

            holder.likes.setOnClickListener {
                val intent = Intent(mContext, LikesActivity::class.java)
                intent.putExtra("id", post.getpostid())
                intent.putExtra("title", "likes")
                mContext.startActivity(intent)
            }

            holder.likeButton.setOnClickListener {
                if (holder.likeButton.tag == "like")
                {
                    FirebaseDatabase.getInstance().reference
                        .child("likes")
                        .child(post.getpostid())
                        .child(firebaseuser!!.uid)
                        .setValue(true)
                }
                else
                {
                    FirebaseDatabase.getInstance().reference
                        .child("likes")
                        .child(post.getpostid())
                        .child(firebaseuser!!.uid)
                        .removeValue()

                    val intent = Intent(mContext, MainActivity::class.java)
                    mContext.startActivity(intent)
                }
            }

            holder.commentButton.setOnClickListener {

                val intentcomment = Intent(mContext, CommentsActivity::class.java)
                intentcomment.putExtra("postid", post.getpostid())
                intentcomment.putExtra("getpublisher", post.getpublisher())
                mContext.startActivity(intentcomment)

            }

            holder.comments.setOnClickListener {

                val intentcomment = Intent(mContext, CommentsActivity::class.java)
                intentcomment.putExtra("postid", post.getpostid())
                intentcomment.putExtra("getpublisher", post.getpublisher())
                mContext.startActivity(intentcomment)

            }

            holder.saveButton.setOnClickListener {
               if (holder.saveButton.tag == "save")
               {
                   FirebaseDatabase.getInstance().getReference()
                       .child("saves")
                       .child(firebaseuser!!.uid)
                       .child(post.getpostid())
                       .setValue(true)
               }
               else
               {
                   FirebaseDatabase.getInstance().getReference()
                       .child("saves")
                       .child(firebaseuser!!.uid)
                       .child(post.getpostid())
                       .removeValue()

               }
            }

        }

    private fun numberoflikes(likes: TextView, getpostid: String)
    {
        val likeref = FirebaseDatabase.getInstance().reference
            .child("likes").child(getpostid)

        likeref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    likes.text = snapshot.childrenCount.toString() + " likes"
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


    private fun gettotalcomments(comments: TextView, getpostid: String)
    {
        val commentsref = FirebaseDatabase.getInstance().reference
            .child("comments").child(getpostid)

        commentsref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    comments.text = "View All " + snapshot.childrenCount.toString() + " Comments"
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun islikes(getpostid: String, likeButton: ImageView)
    {
        val firebaseUser = FirebaseAuth.getInstance().currentUser

        val likeref = FirebaseDatabase.getInstance().reference
            .child("likes").child(getpostid)

        likeref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child(firebaseUser!!.uid).exists())
                    {
                        likeButton.setImageResource(R.drawable.heart_clicked)
                        likeButton.tag = "liked"
                    }
                else
                    {
                        likeButton.setImageResource(R.drawable.heart_not_clicked)
                        likeButton.tag = "like"
                    }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


    override fun getItemCount(): Int {
            return mPost.size
        }

   inner class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView)
   {
       var profileImage: CircleImageView
       var postImage: ImageView
       var likeButton: ImageView
       var commentButton: ImageView
       var saveButton: ImageView
       var userName: TextView
       var likes: TextView
       var publisher: TextView
       var discription: TextView
       var comments: TextView
       init {
           profileImage = itemView.findViewById(R.id.user_profile_image_post)
           postImage = itemView.findViewById(R.id.post_image_home)
           likeButton = itemView.findViewById(R.id.post_image_like_btn)
           commentButton = itemView.findViewById(R.id.post_image_comment_btn)
           saveButton = itemView.findViewById(R.id.post_save_comment_btn)
           userName = itemView.findViewById(R.id.user_name_post)
           likes = itemView.findViewById(R.id.likes)
           publisher = itemView.findViewById(R.id.publisher)
           discription = itemView.findViewById(R.id.description)
           comments = itemView.findViewById(R.id.comments)
       }

   }

    private fun publisherinfo(profileImage: CircleImageView, userName: TextView, publisher: TextView, getpublisher: String) {

        val useref = FirebaseDatabase.getInstance().getReference().child("user").child(getpublisher)

        useref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    val user = snapshot.getValue<User>(User::class.java)

                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile).into(profileImage)
                    userName.text = user!!.getEmail()
                    publisher.text = user!!.getFname()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun checkSavedStatus(postid: String, imageView: ImageView)
    {
        val saveref = FirebaseDatabase.getInstance().getReference()
            .child("saves")
            .child(firebaseuser!!.uid)

        saveref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(postid).exists())
                {
                    imageView.setImageResource(R.drawable.save_large_icon)
                    imageView.tag = "saved"
                }
                else
                {
                    imageView.setImageResource(R.drawable.save_unfilled_large_icon)
                    imageView.tag = "save"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

}
