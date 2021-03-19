package com.example.projrctlogin.Fragrments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.security.identity.AccessControlProfileId
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projrctlogin.AccountSettingActivity2
import com.example.projrctlogin.Adpater.MyimagesAdpater
import com.example.projrctlogin.Model.Post
import com.example.projrctlogin.Model.User
import com.example.projrctlogin.R
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*

import kotlinx.android.synthetic.main.fragment_profile.view.*
import java.lang.Integer.reverse
import java.security.Key
import java.util.*
import java.util.Collections.checkedCollection
import java.util.Collections.reverse
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    private lateinit var profileId: String
    private lateinit var firebaseUser : FirebaseUser
    lateinit var imagesavebtn : ImageButton
    lateinit var editbtn : Button

    lateinit var add: BottomNavigationItemView

    var postlist: List<Post>? = null
    var myimagesAdpater: MyimagesAdpater? = null

    var myimagesAdpaterSavedImag: MyimagesAdpater? = null
    var postlistsaved: List<Post>? = null
    var mySavesImg: List<String>? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        add = requireActivity().findViewById(R.id.navigation_add)
        userdtls()

        editbtn = view.findViewById(R.id.edit_account_setting_btn)
        imagesavebtn=view.findViewById(R.id.image_save_btn)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)

        if (pref !=null)
            {
                this.profileId = pref.getString("profileId","none").toString()
            }

        if (profileId == firebaseUser.uid)
            {
                view.edit_account_setting_btn.text = "Edit Profile"
            }

            else if (profileId != firebaseUser.uid)
            {
                editbtn?.visibility = View.GONE
                imagesavebtn.visibility = View.GONE
            }


        //recyclerview for upload Images
        var recyclerViewuploadimages: RecyclerView
        recyclerViewuploadimages = view.findViewById(R.id.recycler_upload_pic)
        recyclerViewuploadimages.setHasFixedSize(true)
        val linearLayoutManager: LinearLayoutManager = GridLayoutManager(context, 3)
        recyclerViewuploadimages.layoutManager = linearLayoutManager

        postlist = ArrayList()
        myimagesAdpater = context?.let { MyimagesAdpater(it, postlist as ArrayList<Post>) }
        recyclerViewuploadimages.adapter = myimagesAdpater


        //recyclerview for saved Images
        var recyclerViewsaveimages: RecyclerView
        recyclerViewsaveimages = view.findViewById(R.id.recycler_saved_pic)
        recyclerViewsaveimages.setHasFixedSize(true)
        val linearLayoutManager2: LinearLayoutManager = GridLayoutManager(context, 3)
        recyclerViewsaveimages.layoutManager = linearLayoutManager2

        postlistsaved = ArrayList()
        myimagesAdpaterSavedImag = context?.let { MyimagesAdpater(it, postlistsaved as ArrayList<Post>) }
        recyclerViewsaveimages.adapter = myimagesAdpaterSavedImag

        var uploadedimagebtn: ImageButton
        uploadedimagebtn =view.findViewById(R.id.image_grid_view_btn)
        uploadedimagebtn.setOnClickListener {
            recyclerViewsaveimages.visibility = View.GONE
            recyclerViewuploadimages.visibility = View.VISIBLE
        }


        var savedimagebtn: ImageButton
        savedimagebtn =view.findViewById(R.id.image_save_btn)
        savedimagebtn.setOnClickListener {
            recyclerViewsaveimages.visibility = View.VISIBLE
            recyclerViewuploadimages.visibility = View.GONE
        }




        view.edit_account_setting_btn.setOnClickListener {
            startActivity(Intent(context, AccountSettingActivity2::class.java))
        }
        userInfo()
        myphotos()
        getTotalNumberOfPosts()
        mysaves()

        return view

    }



    private fun myphotos()
    {
        val photoRef = FirebaseDatabase.getInstance().getReference()
            .child("posts")

        photoRef.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    (postlist as ArrayList<Post>).clear()

                    for (datasnapshot in snapshot.children)
                    {
                        val post = datasnapshot.getValue(Post::class.java)
                        if (post!!.getpublisher().equals(profileId))
                        {
                            (postlist as ArrayList<Post>).add(post)
                        }
                        Collections.reverse(postlist)
                        myimagesAdpater!!.notifyDataSetChanged()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun userInfo()
    {
        val userRef = FirebaseDatabase.getInstance().getReference().child("user").child(profileId)

        userRef.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    val user = snapshot.getValue<User>(User::class.java)
                   Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile).into(view?.pro_image_profile_frag)
                    view?.profile_fragrement_username?.text = user!!.getEmail()
                    view?.profile_fragrement_name?.text = user!!.getFname()
                    view?.profile_fragrement_stream?.text = user!!.getStream()
                    view?.profile_fragrement_typeofaccount?.text = user!!.getTypeofaccount()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onStop() {
        super.onStop()
        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
        pref?.putString("profileId", firebaseUser.uid)
        pref?.apply()
    }

    override fun onPause() {
        super.onPause()
        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
        pref?.putString("profileId", firebaseUser.uid)
        pref?.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
        pref?.putString("profileId", firebaseUser.uid)
        pref?.apply()
    }

    private fun getTotalNumberOfPosts()
    {
        val photoRef = FirebaseDatabase.getInstance().getReference()
            .child("posts")

        photoRef.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists())
                {
                    var postcounter = 0

                    for (snapshot in dataSnapshot.children)
                    {
                        val post = snapshot.getValue(Post::class.java)
                        if (post!!.getpublisher() == profileId)
                        {
                            postcounter++
                        }
                    }
                    profile_fragrement_post_no.text = "Total Posts : " + postcounter
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun mysaves() {
        mySavesImg = ArrayList()

        val savedRef = FirebaseDatabase.getInstance().getReference()
            .child("saves")
            .child(firebaseUser.uid)

        savedRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists())
                {
                    for (snapshot in dataSnapshot.children)
                    {
                        (mySavesImg as ArrayList<String>).add(snapshot.key!!)
                    }
                    readSvaedimagesdata()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

    private fun readSvaedimagesdata() {
        val photoRef = FirebaseDatabase.getInstance().getReference()
            .child("posts")


            photoRef.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists())
                    {
                        (postlistsaved as ArrayList<Post>).clear()
                        for (snapshot in dataSnapshot.children)
                        {
                            val post = snapshot.getValue(Post::class.java)

                            for (key in mySavesImg!!)
                            {
                                 if(post!!.getpostid() == key)
                                 {
                                     (postlistsaved as ArrayList<Post>).add(post!!)
                                 }
                            }
                            myimagesAdpaterSavedImag!!.notifyDataSetChanged()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    private fun userdtls()
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