package com.example.projrctlogin.Adpater

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projrctlogin.Model.Comment
import com.example.projrctlogin.Model.User
import com.example.projrctlogin.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import org.jetbrains.annotations.NotNull

class CommentsAdpater(private val mcontext: Context,
                        private val mcomment: MutableList<Comment>?
                        ): RecyclerView.Adapter<CommentsAdpater.ViewHolder>()
{

    private var firebaseUser: FirebaseUser? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsAdpater.ViewHolder {
        val view = LayoutInflater.from(mcontext).inflate(R.layout.comment_item_loyout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentsAdpater.ViewHolder, position: Int) {
            firebaseUser = FirebaseAuth.getInstance().currentUser

        val comment = mcomment!![position]
        holder.commentTv.text = comment.getcomment()
        getuserinfo(holder.imageProfile, holder.usernameTv, comment.getpublisher())
    }

    override fun getItemCount(): Int {
       return mcomment!!.size
    }

    inner class ViewHolder(@NotNull itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var imageProfile: CircleImageView
        var usernameTv: TextView
        var commentTv: TextView
        init {
            imageProfile = itemView.findViewById(R.id.user_profile_image_comment)
            usernameTv = itemView.findViewById(R.id.user_name_comment)
            commentTv = itemView.findViewById(R.id.comment_comment)
        }

    }

    private fun getuserinfo(imageProfile: CircleImageView, usernameTv: TextView, getpublisher: String)
    {
        val useref = FirebaseDatabase.getInstance().getReference()
            .child("user")
            .child(getpublisher)

        useref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    val user = snapshot.getValue<User>(User::class.java)

                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile).into(imageProfile)
                    usernameTv.text = user!!.getEmail()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}
