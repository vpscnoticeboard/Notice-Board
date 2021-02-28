package com.example.projrctlogin.Adpater

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.projrctlogin.Fragrments.PostDetailsFragment
import com.example.projrctlogin.Model.Comment
import com.example.projrctlogin.Model.Post
import com.example.projrctlogin.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import org.jetbrains.annotations.NotNull

class MyimagesAdpater (private val mcontext: Context, mpost: List<Post>)
                    : RecyclerView.Adapter<MyimagesAdpater.ViewHolder?>()
{

    private var mpost: List<Post>? = null

    init {
        this.mpost = mpost
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mcontext).inflate(R.layout.images_items_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val post = mpost!![position]
        Picasso.get().load(post.getpostimage()).into(holder.postImage)

        holder.postImage.setOnClickListener {
            val editor = mcontext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
            editor.putString("postid", post.getpostid())
            editor.apply()
            (mcontext as FragmentActivity).getSupportFragmentManager().beginTransaction().replace(R.id.fragrment_container, PostDetailsFragment()).commit()
        }
    }

    override fun getItemCount(): Int {
        return mpost!!.size
    }

    inner class ViewHolder(@NotNull itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var postImage: ImageView
        init {
            postImage = itemView.findViewById(R.id.post_image)
        }

    }
}