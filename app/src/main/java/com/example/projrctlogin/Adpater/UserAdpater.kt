package com.example.projrctlogin.Adpater

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.projrctlogin.Model.User
import com.example.projrctlogin.R
import com.google.android.gms.dynamic.IFragmentWrapper
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.user_item_layout.view.*
import java.math.MathContext

class UserAdpater (private var mContext: Context,
                   private  var mUser : List<User>,
                   private var isFragment: Boolean = false) : RecyclerView.Adapter<UserAdpater.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdpater.ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.user_item_layout, parent,false)
        return UserAdpater.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mUser.size
    }

    override fun onBindViewHolder(holder: UserAdpater.ViewHolder, position: Int) {

        val user = mUser[position]

        holder.usernametextview.text = user.getUsername()
        holder.lastnametextview.text = user.getLastname()
        holder.emailtextview.text = user.getEmailame()
        holder.streamtextview.text = user.getStreamname()
        holder.typeofacctextview.text = user.getToaname()
        Picasso.get().load(user.getImage()).placeholder(R.drawable.profile).into(holder.userprofileimage)
    }

    class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var usernametextview : TextView = itemView.findViewById(R.id.user_name_search)
        var lastnametextview : TextView = itemView.findViewById(R.id.user_last_name_search)
        var emailtextview : TextView = itemView.findViewById(R.id.user_email_search)
        var streamtextview : TextView = itemView.findViewById(R.id.user_stream_search)
        var typeofacctextview : TextView = itemView.findViewById(R.id.user_typeofaccount_search)
        var userprofileimage : CircleImageView = itemView.findViewById(R.id.user_name_search)
    }

}