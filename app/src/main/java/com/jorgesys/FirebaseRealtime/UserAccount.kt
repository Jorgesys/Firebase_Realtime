package com.jorgesys.FirebaseRealtime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener


class UserAccount : AppCompatActivity() {

    private lateinit var dbReference: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase
    private val TAG = "UserAccount"
    private var userId: String = ""

    //TextView
    private lateinit var userNameTv: TextView
    private lateinit var userSurnameTv: TextView

    //EditText
    private lateinit var userNameEt: EditText
    private lateinit var userSurnameEt: EditText

    //Button
    private lateinit var updateUserBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseDatabase = FirebaseDatabase.getInstance()
        dbReference = firebaseDatabase.getReference("users")

        userNameTv = findViewById(R.id.user_name)
        userSurnameTv = findViewById(R.id.user_surname)

        userNameEt = findViewById(R.id.name_edt_text)
        userSurnameEt = findViewById(R.id.surname_edt_text)

        updateUserBtn = findViewById(R.id.update_user_btn)

        userId = dbReference.push().key.toString()

        updateUserBtn.setOnClickListener{
            var name: String = userNameEt.text.toString()
            var surname: String = userSurnameEt.text.toString()

            if(TextUtils.isEmpty(userId)){
                createUser(name, surname)
            } else{
                updateUser(name, surname)
            }
        }
    }

    private fun updateUser(name: String, surname: String) {
        Log.i(TAG, "updateUser() name: ${name} mobile: ${surname}")
        // updating the user via child nodes
        if (!TextUtils.isEmpty(name))
            dbReference.child(userId).child("name").setValue(name)

        if (!TextUtils.isEmpty(surname))
            dbReference.child(userId).child("mobile").setValue(surname)

        addUserChangeListener()

    }

    private fun createUser(name: String, mobile: String) {
        val user = UserInfo(name, mobile)
        dbReference.child(userId).setValue(user)
    }

    private fun addUserChangeListener() {
        // User data change listener
        dbReference.child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(UserInfo::class.java)
                Log.i(TAG, "onDataChange() user: ${user}")
                // Check for null
                if (user == null) {
                    return
                }

                // Display newly updated name and email
                userNameTv.setText(user?.name).toString()
                userSurnameTv.setText(user?.mobile).toString()

                // clear edit text
                userNameEt.setText("")
                userSurnameEt.setText("")

                changeButtonText()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.e(TAG, "onCancelled() error: ${error.message}")
            }
        })
    }

    private fun changeButtonText(){
        Log.i(TAG, "changeButtonText()")
        if (TextUtils.isEmpty(userId)) {
            updateUserBtn.text = "Save";
        } else {
            updateUserBtn.text = "Update";
        }
    }
}