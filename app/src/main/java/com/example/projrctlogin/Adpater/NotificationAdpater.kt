package com.example.projrctlogin.Adpater

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.projrctlogin.Fragrments.PostDetailsFragment
import com.example.projrctlogin.Fragrments.ProfileFragment
import com.example.projrctlogin.Model.Notification
import com.example.projrctlogin.Model.Post
import com.example.projrctlogin.Model.User
import com.example.projrctlogin.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_comments.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import org.jetbrains.annotations.NotNull

class NotificationAdpater(private val mContext: Context,
                          private val mNotification: List<Notification>): RecyclerView.Adapter<NotificationAdpater.ViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.notification_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notification =  mNotification[position]

        userInfo(holder.ProfileImage, holder.UserName, notification.getUserId())

        if (notification.getText().equals("liked your post"))
        {
            holder.text.text = "liked your post"
        }
        else if (notification.getText().contains("commented:"))
        {
            holder.text.text = notification.getText().replace("commented:", "commented: ")
        }
        else
        {
            holder.text.text = notification.getText()
        }

        if (notification.getIspost())
        {
            holder.PostImage.visibility = View.VISIBLE
            getpostimage(holder.PostImage, notification.getPostId())
        }
        else
        {
            holder.PostImage.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            if (notification.getIspost())
            {
                val editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
                editor.putString("postid", notification.getPostId())
                editor.apply()
                (mContext as FragmentActivity).getSupportFragmentManager().beginTransaction().replace(R.id.fragrment_container, PostDetailsFragment()).commit()
            }
            else
            {
                val editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
                editor.putString("profileId", notification.getPostId())
                editor.apply()
                (mContext as FragmentActivity).getSupportFragmentManager().beginTransaction().replace(R.id.fragrment_container, ProfileFragment()).commit()
            }
        }

    }

    override fun getItemCount(): Int {
        return mNotification.size
    }

    inner class ViewHolder(@NotNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var PostImage: ImageView
        var ProfileImage: CircleImageView
        var UserName: TextView
        var text: TextView

        init {
            PostImage = itemView.findViewById(R.id.notification_post_image)
            ProfileImage = itemView.findViewById(R.id.notification_profile_image)
            UserName = itemView.findViewById(R.id.username_notification)
            text = itemView.findViewById(R.id.comment_notification)
        }

    }


    private fun userInfo(imageView: ImageView, userName: TextView, publisherId: String)
    {
        val userRef = FirebaseDatabase.getInstance().getReference().child("user").child(publisherId)

        userRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    val user = snapshot.getValue<User>(User::class.java)
                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile).into(imageView)
                    userName.text = user!!.getEmail()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


    private fun getpostimage(imageView: ImageView, postId: String)
    {
        val postRef = FirebaseDatabase.getInstance().getReference()
            .child("posts")
            .child(postId)

        postRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    val post = snapshot.getValue<Post>(Post::class.java)
                    Picasso.get().load(post!!.getpostimage()).placeholder(R.drawable.profile).into(imageView)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}