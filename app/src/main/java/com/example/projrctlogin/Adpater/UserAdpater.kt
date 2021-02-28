package com.example.projrctlogin.Adpater

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.projrctlogin.Fragrments.ProfileFragment
import com.example.projrctlogin.Model.User
import com.example.projrctlogin.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class UserAdpater(private var mContext: Context,
                  private var mUser: ArrayList<User>,
                  private var isFragment: Boolean = false) : RecyclerView.Adapter<UserAdpater.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdpater.ViewHolder {
        val view =LayoutInflater.from(mContext).inflate(R.layout.user_item_layout, parent,false)

        return UserAdpater.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mUser.size
    }

    override fun onBindViewHolder(holder: UserAdpater.ViewHolder, position: Int) {

        val user = mUser[position]

        holder.fnameTextView.text = user.getFname()
        holder.lnameTextView.text = user.getLname()
        holder.emailTextView.text = user.getEmail()
        holder.streamTextView.text = user.getStream()
        holder.typeofaccountTextView.text = user.getTypeofaccount()
        Picasso.get().load(user.getImage()).placeholder(R.drawable.profile).into(holder.imageTextView )

        holder.itemView.setOnClickListener(View.OnClickListener {
            val pref = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
            pref.putString("profileId", user.getUid())
            pref.apply()
            (mContext as FragmentActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragrment_container, ProfileFragment()).commit()
        })

    }

    class ViewHolder (@NonNull itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var fnameTextView: TextView = itemView.findViewById(R.id.user_name_search)
        var lnameTextView: TextView = itemView.findViewById(R.id.user_last_name_search)
        var emailTextView: TextView = itemView.findViewById(R.id.user_email_search)
        var streamTextView: TextView = itemView.findViewById(R.id.user_stream_search)
        var typeofaccountTextView: TextView = itemView.findViewById(R.id.user_typeofaccount_search)
        var imageTextView: CircleImageView = itemView.findViewById(R.id.user_profile_image_search)
    }
}