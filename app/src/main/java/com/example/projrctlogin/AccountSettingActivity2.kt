package com.example.projrctlogin

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_account_setting2.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class AccountSettingActivity2 : AppCompatActivity() {

    var formate = SimpleDateFormat("dd MMM, YYYY",Locale.US)
    //variable for calender button
    lateinit var btn_show : ImageButton
    lateinit var calender_profile_frag:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_setting2)

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

    }
}