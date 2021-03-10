package com.example.projrctlogin

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.projrctlogin.Model.User
import com.github.ybq.android.spinkit.SpinKitView
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import kotlinx.android.synthetic.main.activity_account_setting2.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class AccountSettingActivity2 : AppCompatActivity() {

    private var checker = ""
    private var myurl = ""
    private var imageuri: Uri?= null
    private var storageProfilePicref: StorageReference? = null

    lateinit var progressBar : SpinKitView


    var formate = SimpleDateFormat("dd MMM, YYYY",Locale.US)
    //variable for calender button
    lateinit var btn_show : ImageButton
    lateinit var calender_profile_frag:TextView
    //variable for firebase
    private lateinit var firebaseUser : FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_setting2)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        storageProfilePicref = FirebaseStorage.getInstance().reference.child("profile pictures")
        progressBar=findViewById(R.id.progressbar)






        btn_show=findViewById(R.id.caledner_icon_profile_frag)
        calender_profile_frag=findViewById(R.id.calender_profile_frag)

        btn_show.setOnClickListener {
            val now = Calendar.getInstance()
            try {
                val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(Calendar.YEAR,year)
                selectedDate.set(Calendar.MONTH,month)
                selectedDate.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                val date = formate.format(selectedDate.time)
                    calender_profile_frag.setText(date)
                Toast.makeText(this,"date : " + date, Toast.LENGTH_SHORT).show()
            },
                    now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
            }
            catch (e:Exception){

            }
        }

        logout_account_btn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@AccountSettingActivity2, Loginpage::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }


        save_info_profile_btn.setOnClickListener {
           if(checker == "clicked")
           {
                uploadImageAndUpdateInfo()
           }
            else
           {
               val fname:Boolean= namecheck(first_name_profile_frag)
               val lname:Boolean=namecheck(last_name_profile_frag)
               val dob:Boolean=dobcheck(calender_profile_frag)
               if(fname and lname and dob)
               {
                   updateuserinfoonly()
               }
           }
        }



        change_image_text_btn.setOnClickListener {

            checker="clicked"

            CropImage.activity()
                .setAspectRatio(1,1)
                .start(this@AccountSettingActivity2)
        }



        userInfo()

    }

    private fun uploadImageAndUpdateInfo() {
        progressBar.visibility= View.VISIBLE

        when
        {
            else ->{
                val fileref = storageProfilePicref!!.child(firebaseUser!!.uid + ".jpg")

                val uploadTask: StorageTask<*>
                uploadTask = fileref.putFile(imageuri!!)

                uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                            progressBar.visibility= View.INVISIBLE
                        }

                    }
                        return@Continuation fileref.downloadUrl
                }).addOnCompleteListener (OnCompleteListener<Uri> {task ->
                    if (task.isSuccessful)
                    {
                        val downloadUrl = task.result
                        myurl = downloadUrl.toString()

                        var ref = FirebaseDatabase.getInstance().reference.child("user")

                        val usermap = HashMap<String , Any>()
                        usermap["fname"]=first_name_profile_frag.text.toString().toLowerCase()
                        usermap["lname"]=last_name_profile_frag.text.toString().toLowerCase()
                        usermap["dateofbirth"]=calender_profile_frag.text.toString().toLowerCase()
                        usermap["image"]=myurl

                        ref.child(firebaseUser.uid).updateChildren(usermap)

                        Toast.makeText(this,"Account information is updated",Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@AccountSettingActivity2, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else
                    {
                        progressBar.visibility= View.INVISIBLE
                    }
                }
                ) }
            }

    }

    private fun updateuserinfoonly() {
        val userRef = FirebaseDatabase.getInstance().reference.child("user")

        val usermap = HashMap<String , Any>()
        usermap["fname"]=first_name_profile_frag.text.toString().toLowerCase()
        usermap["lname"]=last_name_profile_frag.text.toString().toLowerCase()
        usermap["dateofbirth"]=calender_profile_frag.text.toString().toLowerCase()

        userRef.child(firebaseUser.uid).updateChildren(usermap)

        Toast.makeText(this,"Account information is updated",Toast.LENGTH_SHORT).show()

        val intent = Intent(this@AccountSettingActivity2, MainActivity::class.java)
        startActivity(intent)
        finish()

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null)
        {
            val result = CropImage.getActivityResult(data)
            imageuri = result.uri
            profile_image_view_profile_frag.setImageURI(imageuri)

        }



    }









    private fun userInfo()
    {
        val userRef = FirebaseDatabase.getInstance().getReference().child("user").child(firebaseUser.uid)

        userRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
//
                if (snapshot.exists())
                {
                    val user = snapshot.getValue<User>(User::class.java)
                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile).into(profile_image_view_profile_frag)
                    first_name_profile_frag.setText(user.getFname())
                    last_name_profile_frag.setText(user.getLname())
                    calender_profile_frag.setText(user.getDob())
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }



    private fun namecheck(name: TextView): Boolean {
        var name_check: Boolean = false
        name.validator()
            .nonEmpty()
            .noNumbers()
            .noSpecialCharacters()
            .addErrorCallback {
                name.error = it
            }
            .addSuccessCallback {
                name_check = true
            }

            .check()
        return name_check
    }

    private fun dobcheck(name: TextView): Boolean {
        var name_check: Boolean = false
        name.validator()
            .nonEmpty()
            .addErrorCallback {
                name.error = it
            }
            .addSuccessCallback {
                name_check = true
            }

            .check()
        return name_check
    }

}