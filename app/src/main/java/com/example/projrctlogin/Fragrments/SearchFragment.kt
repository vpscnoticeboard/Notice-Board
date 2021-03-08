package com.example.projrctlogin.Fragrments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projrctlogin.Adpater.UserAdpater
import com.example.projrctlogin.Model.User
import com.example.projrctlogin.R
import com.google.android.material.bottomnavigation.BottomNavigationItemView
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

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var recyclerView: RecyclerView? = null
    private var userAdpater: UserAdpater? = null
    private var mUser: MutableList<User>? = null

    lateinit var add: BottomNavigationItemView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        add = requireActivity().findViewById(R.id.navigation_add)
        add.visibility = View.GONE
        userInfo()

        recyclerView = view.findViewById(R.id.recycler_view_search)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)

        mUser = ArrayList()
        userAdpater = context?.let { UserAdpater(it,mUser as ArrayList<User>, true) }
        recyclerView?.adapter = userAdpater

        view.search_edit_text.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (view.search_edit_text.text.toString() == "")
                {

                }
                else
                {
                    recyclerView?.visibility = View.VISIBLE

                    retriveuser()
                    serarchuser(s.toString().toLowerCase())
                }
            }
        })

        return view
    }

    private fun serarchuser(input: String) {
        val query = FirebaseDatabase.getInstance().getReference()
            .child("user")
            .orderByChild("fname")
            .startAt(input)
            .endAt(input + "\uf8ff")

        query.addValueEventListener(object  : ValueEventListener{
            override fun onDataChange(datasnapshot: DataSnapshot) {
                mUser?.clear()
                for (snapshot in datasnapshot.children)
                {
                    val user = snapshot.getValue(User::class.java)
                    if (user !=null)
                    {
                        mUser?.add(user)
                    }
                }

                userAdpater?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun retriveuser() {

        val userref = FirebaseDatabase.getInstance().getReference().child("user")
        userref.addValueEventListener(object  : ValueEventListener{
            override fun onDataChange(datasnapshot: DataSnapshot) {
                if (view?.search_edit_text?.text.toString() == "")
                {
                    mUser?.clear()
                    for (snapshot in datasnapshot.children)
                    {
                        val user = snapshot.getValue(User::class.java)
                        if (user !=null)
                        {
                            mUser?.add(user)
                        }
                    }

                    userAdpater?.notifyDataSetChanged()

                }
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
                    if(typeofuser == "admin")
                    {
                        add.visibility = View.VISIBLE
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}