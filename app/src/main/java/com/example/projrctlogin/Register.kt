package com.example.projrctlogin

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TaskInfo
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.ActionMode
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.core.app.ActivityCompat
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_register.*
import org.intellij.lang.annotations.Language
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap



class Register : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    var formate = SimpleDateFormat("dd MMM, YYYY", Locale.US)
    lateinit var typebar: Spinner
    lateinit var streambar: Spinner
    lateinit var valtypebar: String
    lateinit var valstreambar: String
    lateinit var fname: TextView
    lateinit var lname: TextView
    lateinit var signup_btnclik: Button
    lateinit var email: TextView
    lateinit var password: TextView
    lateinit var confirmpassword: TextView
    lateinit var mobile: TextView
    lateinit var profilepic: CircleImageView
    lateinit var show_calender: ImageView
    lateinit var dob: TextView
    lateinit var rbbtnmale:RadioButton
    lateinit var rbbtnfemale:RadioButton
    lateinit var handler: Handler



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            //for no status  bar
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_register)
        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "REGISTER"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        //findviewby id declaration for allll
        password=findViewById(R.id.password)
        email=findViewById(R.id.edt_email)
        signup_btnclik=findViewById(R.id.btn_sign_up)
        mobile=findViewById(R.id.edt_phone_no)
        confirmpassword=findViewById(R.id.confirmPassword)
        profilepic=findViewById(R.id.ivImagePerson)
        fname=findViewById(R.id.edt_first_name)
        lname=findViewById(R.id.edt_last_name)
        show_calender=findViewById(R.id.img_btn_calendar)
        dob=findViewById(R.id.edt_dob)
        rbbtnmale=findViewById(R.id.rbtn_male)
        rbbtnfemale=findViewById(R.id.rbtn_female)


        //findview by id complete


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

        //sign_up button on click method
        signup_btnclik.setOnClickListener {

           val fname:Boolean= namecheck(fname)
            val lname:Boolean=namecheck(lname)
            val email:Boolean=emailcheck(email)
            val mobileno:Boolean=mobilecheck(mobile)
            val password:Boolean=passwordcheck(password, confirmpassword)

           if(fname and lname and email and mobileno and password){
               progressbar.visibility=View.VISIBLE
               //calling function for creating user
               createaccount()
           }
        }



        //show the calender
            show_calender.setOnClickListener{
                val now = Calendar.getInstance()
                try {
                    val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        val selectedDate = Calendar.getInstance()
                        selectedDate.set(Calendar.YEAR,year)
                        selectedDate.set(Calendar.MONTH,month)
                        selectedDate.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                        val date = formate.format(selectedDate.time)
                        dob.setText(date)
                        Toast.makeText(this,"date : " + date, Toast.LENGTH_SHORT).show()
                    },
                        now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH))
                    datePicker.show()
                }
                catch (e: Exception){

                }

            }



    }//------------on create complete................

    override fun onStart() {
        super.onStart()
        progressbar.visibility=View.INVISIBLE
    }

    override fun onResume() {
        super.onResume()
        progressbar.visibility=View.INVISIBLE
    }

    //function for creating user
    private fun createaccount() {
        //storing all the values into variables
            val fname=fname.text.toString()
            val lname=lname.text.toString()
            val email=email.text.toString()
            var gender:String?=null
            if (rbbtnmale.isChecked)
            {
                gender="Male"
            }
            if (rbbtnfemale.isChecked)
            {
                gender="Female"
            }

            val dateofbirth=dob.text.toString()
            val typeofaccount=typebar.selectedItem.toString()
            val stream=streambar.selectedItem.toString()
            val mobileno=mobile.text.toString()
            val password=password.text.toString()

            // for firebase auth
        when{
                   else ->{
                       //progress Dialog
                       val ProgressDialog = ProgressDialog(this)
                       ProgressDialog.setTitle("Signup")
                       ProgressDialog.setMessage("please wait this may take a while....")
                       ProgressDialog.setCanceledOnTouchOutside(false)

                        //firebase auth variable
                       val maut : FirebaseAuth = FirebaseAuth.getInstance()

                       //creating user in firebase auth and if user creation is successful then adding data to realtime database
                       maut.createUserWithEmailAndPassword(email,password)
                           .addOnCompleteListener{task ->
                               if (task.isSuccessful) {
                                   val currentuserid = FirebaseAuth.getInstance().currentUser!!.uid
                                   val useref : DatabaseReference = FirebaseDatabase.getInstance().reference.child("user")
                                   val usermap = HashMap<String , Any>()
                                   usermap["uid"]=currentuserid
                                   usermap["fname"]=fname
                                   usermap["lname"]=lname
                                   usermap["email"]=email
                                   usermap["gender"]=gender.toString()
                                   usermap["dateofbirth"]=dateofbirth
                                   usermap["typeofaccount"]=typeofaccount
                                   usermap["stream"]=stream
                                   usermap["mobileno"]=mobileno
                                   usermap["password"]=password
                                   usermap["image"]="gs://noticeboard-c45eb.appspot.com/Default Image/default_profile_logo.jpg"

                                   useref.child(currentuserid).setValue(usermap)
                                       .addOnCompleteListener { Task->
                                           if (task.isSuccessful)
                                           {
                                                ProgressDialog.dismiss()
                                               Toast.makeText(this,"Account Has Been Created...",Toast.LENGTH_SHORT).show()
                                               handler = Handler()
                                               handler.postDelayed({
                                                   val intent = Intent(applicationContext, Mobileverify::class.java)
                                                   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                                   intent.putExtra("mobile",mobile.text.toString())
                                                   startActivity(intent)
                                                   finish()
                                               },1000)
                                           }
                                           else
                                           {
                                               val message=task.exception.toString()
                                               Toast.makeText(this,"Error : $message",Toast.LENGTH_SHORT).show()
                                               FirebaseAuth.getInstance().signOut()
                                               ProgressDialog.dismiss()
                                               progressbar.visibility=View.INVISIBLE
                                           }
                                       }
                               }
                               else
                               {
                                    val message=task.exception.toString()
                                   Toast.makeText(this,"Error : $message",Toast.LENGTH_SHORT).show()
                                   ProgressDialog.dismiss()
                                   progressbar.visibility=View.INVISIBLE
                               }
                           }
                   }
               }
    }

    //function for validations
    //function check fname and lname
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

    //function for check password
    fun passwordcheck(password: TextView, confirmpassword: TextView): Boolean {
        var pcheck: Boolean = false
        password.validator()
            .nonEmpty()
            .minLength(8)
            .atleastOneUpperCase()
            .atleastOneSpecialCharacters()
            .atleastOneNumber()
            .addErrorCallback {
                // Invalid password
                password.error = it
            }
            .addSuccessCallback {

                val pass = password.text.toString()
                val cpass = confirmpassword.text.toString()

                if (pass == cpass) {
                    pcheck = true
                } else {
                    confirmpassword.error = "Password Not Match"
                }
            }
            .check()
        return pcheck

    }
    //FUNCTION CHECK FOR EMAIL
    fun emailcheck(email: TextView): Boolean {
        var emailcheck: Boolean = false
        email.validator()
            .nonEmpty()
            .validEmail()
            .addErrorCallback {
                // Invalid email
                email.error = it
            }
            .addSuccessCallback {
                // call Login webservice here or anything else for success usecase
                emailcheck = true
            }
            .check()
        return emailcheck
    }
//FUNCTION CHECK FOR MOBILENUMBER
    fun mobilecheck(mobile: TextView): Boolean {
        var mobilecheck: Boolean = false
        mobile.validator()
            .validNumber()
            .nonEmpty()
            .addErrorCallback { mobile.error = it }
            .addSuccessCallback {
                mobilecheck = true
            }
            .check()
        return mobilecheck
    }



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



//    // sign_up btn click coding
//    fun btnsignupclick(view: View) {
//
//        var intent=Intent(applicationContext, MainActivity::class.java)
//        startActivity(intent)
//        finish()
//    }
}//========class complete====================
