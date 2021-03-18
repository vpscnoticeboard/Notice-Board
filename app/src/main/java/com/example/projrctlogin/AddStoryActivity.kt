package com.example.projrctlogin

import android.app.*
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.github.ybq.android.spinkit.SpinKitView
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_add_post.*
import java.util.HashMap

class AddStoryActivity : AppCompatActivity()
{

    private val CHANNEL_ID = "channel_id_example_01"
    private val notificatioid = 101


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

//        realtimenotification()

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
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Adding new story")
                progressDialog.setMessage("Please wait, we are adding your story...")
                progressDialog.show()

                val fileref = storagestoryPicref!!.child(System.currentTimeMillis().toString() + ".jpg")

                val uploadTask: StorageTask<*>
                uploadTask = fileref.putFile(imageuri!!)

                uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                            progressDialog.dismiss()
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


                            val intent = Intent(this, MainActivity::class.java)
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

//    private fun realtimenotification()
//    {
//        var ref = FirebaseDatabase.getInstance().reference.child("posts")
//        ref.addChildEventListener(object : ChildEventListener {
//            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//                if (snapshot.exists()) {
//                    createnotification()
//                    sendnotification()
//                }
//            }
//
//            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onChildRemoved(snapshot: DataSnapshot) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//
//        })
//    }
    //notifications
//    private fun createnotification()
//    {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name = "Notification Title"
//            val discriptiontext = "Notification description"
//            val important = NotificationManager.IMPORTANCE_DEFAULT
//            val channel = NotificationChannel(CHANNEL_ID, name,important).apply {
//                description = discriptiontext
//            }
//            val NotificationManager : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            NotificationManager.createNotificationChannel(channel)
//        }
//    }
//
//    private fun sendnotification()
//    {
//        val intent = Intent(this, MainActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }
//        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0 , intent , 0)
//
//
//        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
//            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
//            .setContentTitle("Notice Board")
//            .setContentText("Added a new story...")
//            .setContentIntent(pendingIntent)
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//
//        with(NotificationManagerCompat.from(this))
//        {
//            notify(notificatioid, builder.build())
//        }
//    }
}