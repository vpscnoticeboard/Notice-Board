

package com.example.projrctlogin

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_register.*
import org.intellij.lang.annotations.Language

class Register : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    lateinit var typebar: Spinner
    lateinit var streambar: Spinner
    lateinit var valtypebar: String
    lateinit var valstreambar: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            //for no status  bar
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_register)
//        //actionbar
//        val actionbar = supportActionBar
//        //set actionbar title
//        actionbar!!.title = "REGISTER"
//        //set back button
//        actionbar.setDisplayHomeAsUpEnabled(true)
//        actionbar.setDisplayHomeAsUpEnabled(true)


        // add items in spinner type and stream

        typebar = findViewById(R.id.Type)
        streambar = findViewById(R.id.stream)
        val type = resources.getStringArray(R.array.type)
        val strem = resources.getStringArray(R.array.stream)
        val aa: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, type)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        typebar.adapter = aa
        typebar.onItemSelectedListener = this
        val bb: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, strem)
        bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        streambar.adapter = bb
        streambar.onItemSelectedListener = this

        // load image from gallary

        ivImagePerson.setOnClickListener(View.OnClickListener {
            checkPermission()
        })



    }//------------on create complete................

    //code for the image load function
    val READIMAGE:Int=253
    private fun checkPermission() {
        if(Build.VERSION.SDK_INT>=23){
            if(ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED){

                requestPermissions(arrayOf( android.Manifest.permission.READ_EXTERNAL_STORAGE),READIMAGE)
                return
            }
        }

        loadImage()
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when(requestCode){
            READIMAGE->{
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    loadImage()
                }else{
                    Toast.makeText(applicationContext,"Cannot access your images",Toast.LENGTH_LONG).show()
                }
            }
            else-> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }


    }
    val PICK_IMAGE_CODE=123
    private fun loadImage() {
        var intent= Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,PICK_IMAGE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==PICK_IMAGE_CODE  && data!=null && resultCode == RESULT_OK){

            val selectedImage=data.data
            val filePathColum= arrayOf(MediaStore.Images.Media.DATA)
            val cursor= selectedImage?.let { contentResolver.query(it,filePathColum,null,null,null) }
            if (cursor != null) {
                cursor.moveToFirst()
            }
            val coulomIndex= cursor!!.getColumnIndex(filePathColum[0])
            val picturePath= cursor.getString(coulomIndex)
            cursor.close()
            ivImagePerson.setImageBitmap(BitmapFactory.decodeFile(picturePath))
        }
    }


    //method for spinner
    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    //method for spinner
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        //val text: String = parent?.getItemAtPosition(position).toString()

        valstreambar = streambar.selectedItem.toString()
        valtypebar = typebar.selectedItem.toString()
    }

    //method for backbutton in action bar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }



    // sign_up btn click coding
    fun btnsignupclick(view: View) {

        var intent=Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}//========class complete====================
