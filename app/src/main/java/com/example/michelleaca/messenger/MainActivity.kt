package com.example.michelleaca.messenger

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        register_button.setOnClickListener {
            val email = email_editText.text.toString()
            val password = password_editText.text.toString()

            Log.d("MainActivity", "Email is " + email)
            Log.d("MainActivity", "Password: $password")

        }

        already_have_account_textView.setOnClickListener {
            Log.d("MainActivity", "show log in activity")

            //launch the login activity
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
