package com.example.projrctlogin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.github.ybq.android.spinkit.SpinKitView
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_account_setting2.*
import kotlinx.android.synthetic.main.activity_add_post.*
import java.util.HashMap

class AddPostActivity : AppCompatActivity() {



    private var myurl = ""
    private var imageuri: Uri?= null
    private var storageStoryPicref: StorageReference? = null

    lateinit var progressBar : SpinKitView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        storageStoryPicref = FirebaseStorage.getInstance().reference.child("posts pictures")
        progressBar=findViewById(R.id.progressbar)

        add_post_btn.setOnClickListener {
            uploadImage()
        }

        CropImage.activity()
            .setAspectRatio(20,20)
            .start(this@AddPostActivity)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null)
        {
            val result = CropImage.getActivityResult(data)
            imageuri = result.uri
            image_post.setImageURI(imageuri)
        }

    }

    private fun uploadImage() {
        when
        {

            imageuri == null -> Toast.makeText(this,"Please select image first",Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(discription_post.text.toString()) -> Toast.makeText(this,"Discripton Is Empty",Toast.LENGTH_LONG).show()


            else ->{

                progressBar.visibility= View.VISIBLE

                val fileref = storageStoryPicref!!.child(System.currentTimeMillis().toString() + ".jpg")

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
                })
                    .addOnCompleteListener (OnCompleteListener<Uri> { task ->
                        if (task.isSuccessful)
                        {
                            val downloadUrl = task.result
                            myurl = downloadUrl.toString()

                            var ref = FirebaseDatabase.getInstance().reference.child("posts")
                            var postid = ref.push().key

                            val postmap = HashMap<String , Any>()
                            postmap["postid"]= postid!!
                            postmap["discription"]=discription_post.text.toString().toLowerCase()
                            postmap["publisher"]=FirebaseAuth.getInstance().currentUser!!.uid
                            postmap["postimage"]=myurl

                            ref.child(postid).updateChildren(postmap)

                            Toast.makeText(this,"Post Added Succesfully",Toast.LENGTH_SHORT).show()

                            val intent = Intent(this@AddPostActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else
                        {
                            progressBar.visibility= View.INVISIBLE
                        }
                    }
                    )
            }
        }
    }
}