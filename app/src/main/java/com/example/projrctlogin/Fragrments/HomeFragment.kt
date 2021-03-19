package com.example.projrctlogin.Fragrments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projrctlogin.Adpater.PostAdpater
import com.example.projrctlogin.Adpater.StoryAdpater
import com.example.projrctlogin.Model.Post
import com.example.projrctlogin.Model.Story
import com.example.projrctlogin.Model.User
import com.example.projrctlogin.R
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_search.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {

    private var postAdpater: PostAdpater? = null
    private var postlist: MutableList<Post>? = null
    private var followinglist: MutableList<String>? = null
    private var storyAdpater: StoryAdpater? = null
    private var storylist: MutableList<Story>? = null

    lateinit var add: BottomNavigationItemView
    lateinit var storyitem_layout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        add = requireActivity().findViewById(R.id.navigation_add)
        userInfo()

        var recyclerView: RecyclerView? = null
        var recyclerViewStory: RecyclerView? = null


        recyclerView = view.findViewById(R.id.recycler_view_home)
        val linearLayoutManager = LinearLayoutManager(context)

        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager

        postlist = ArrayList()
        postAdpater = context?.let { PostAdpater(it, postlist as ArrayList<Post>) }
        recyclerView.adapter = postAdpater



        recyclerViewStory = view.findViewById(R.id.recycler_view_story)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager2 = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewStory.layoutManager = linearLayoutManager2


        storylist = ArrayList()
        storyAdpater = context?.let { StoryAdpater(it, storylist as ArrayList<Story>) }
        recyclerViewStory.adapter = storyAdpater

        retriveuser()
        retriveposts()
        retrivestorys()

        return view
    }

    private fun retriveposts() {
        followinglist = ArrayList()
        val postref = FirebaseDatabase.getInstance().reference.child("posts")

        postref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(datasnapshot: DataSnapshot) {
                postlist?.clear()
                for (snapshot in datasnapshot.children)
                {
                    val post = snapshot.getValue(Post::class.java)
                    if (post !=null)
                    {
                        postlist?.add(post)
                    }
                }

                postAdpater?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }



    private fun retriveuser() {

        val userref = FirebaseDatabase.getInstance().getReference().child("user")
        userref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(datasnapshot: DataSnapshot) {
                if (datasnapshot.exists()) {
                    (followinglist as ArrayList<String>).clear()

                    for (snapshot in datasnapshot.children) {
                        snapshot.key?.let { (followinglist as ArrayList<String>).add(it) }

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }




    private fun retrivestorys() {

        val storyref = FirebaseDatabase.getInstance().reference.child("story")

        storyref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(datasnapshot: DataSnapshot) {
                val timeCurrent = System.currentTimeMillis()

                (storylist as ArrayList<Story>).clear()

                (storylist as ArrayList<Story>).add(
                    Story(
                        "",
                        0,
                        0,
                        "",
                        FirebaseAuth.getInstance().currentUser!!.uid!!
                    )
                )
                for (id in followinglist!!) {
                    var countstory = 0
                    var story: Story? = null
                    for (snapshot in datasnapshot.child(id).children) {
                        story = snapshot.getValue(Story::class.java)

                        if (timeCurrent > story!!.getTimeStart() && timeCurrent < story!!.getTimeEnd()) {
                            countstory++
                        }
                    }
                    if (countstory > 0) {
                        (storylist as ArrayList<Story>).add(story!!)
                    }
                }
                storyAdpater!!.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    private fun userInfo()
    {
        val userRef = FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().currentUser!!.uid)

        userRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    val user = snapshot.getValue<User>(User::class.java)
                    val typeofuser = user!!.getTypeofaccount()
                    if(typeofuser != "admin")
                    {
                        add.visibility = View.GONE
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}