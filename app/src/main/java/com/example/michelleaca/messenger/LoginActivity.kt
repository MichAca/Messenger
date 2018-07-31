package com.example.michelleaca.messenger

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        login_button.setOnClickListener {
            val email = login_email_editText.text.toString()
            val password = login_password_editText.text.toString()

            Log.d("Login", "Attempt login")

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)

        }


        back_to_reg_textView.setOnClickListener {
            finish()
        }
    }
}