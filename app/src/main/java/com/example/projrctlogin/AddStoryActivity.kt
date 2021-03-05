package com.example.projrctlogin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.github.ybq.android.spinkit.SpinKitView
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_add_post.*
import java.util.HashMap

class AddStoryActivity : AppCompatActivity()
{
    private var myurl = ""
    private var imageuri: Uri?= null
    private var storagestoryPicref: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_story)


        storagestoryPicref = FirebaseStorage.getInstance().reference.child("story pictures")


        CropImage.activity()
            .setAspectRatio(10,20)
            .start(this@AddStoryActivity)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val result = CropImage.getActivityResult(data)
            imageuri = result.uri

            uploadstory()
        }
    }

    private fun uploadstory() {
        when
        {

            imageuri == null -> Toast.makeText(this,"Please select image first", Toast.LENGTH_LONG).show()


            else ->{


                val fileref = storagestoryPicref!!.child(System.currentTimeMillis().toString() + ".jpg")

                val uploadTask: StorageTask<*>
                uploadTask = fileref.putFile(imageuri!!)

                uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }

                    }
                    return@Continuation fileref.downloadUrl
                })
                    .addOnCompleteListener (OnCompleteListener<Uri> { task ->
                        if (task.isSuccessful)
                        {
                            val downloadUrl = task.result
                            myurl = downloadUrl.toString()

                            var ref = FirebaseDatabase.getInstance().reference.child("story").child(FirebaseAuth.getInstance().currentUser!!.uid)
                            var storyid = (ref.push().key).toString()

                            val timeEnd = System.currentTimeMillis() + 86400000 //one day

                            val storymap = HashMap<String , Any>()
                            storymap["userid"] = FirebaseAuth.getInstance().currentUser!!.uid
                            storymap["timestart"] = ServerValue.TIMESTAMP
                            storymap["timeend"]= timeEnd
                            storymap["imageurl"]=myurl
                            storymap["storyid"] = storyid

                            ref.child(storyid).updateChildren(storymap)

                            Toast.makeText(this,"story Added Succesfully", Toast.LENGTH_SHORT).show()

                            val intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else
                        {
                            
                        }
                    }
                    )
            }
        }
    }

}