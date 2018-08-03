package com.example.michelleaca.messenger

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_button.setOnClickListener {

            performRegister()

        }

        already_have_account_textView.setOnClickListener {
            Log.d("RegisterActivity", "show log in activity")

            //launch the login activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        select_photo_button.setOnClickListener {
            Log.d("RegisterActivity", "Show photo selector")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

    }

    var selectPhotoUri: Uri? = null
    // capturing the result of startActivityResult
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            // proceed and check what the selected picture was

            Log.d("RegisterActivity", "Photo was selected")

            selectPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectPhotoUri)

            selected_photo_imageView_register.setImageBitmap(bitmap)

            select_photo_button.alpha = 0f
//            val bitmapDrawable = BitmapDrawable(bitmap)
//            select_photo_button.setBackgroundDrawable(bitmapDrawable)

        }
    }

    private fun performRegister() {
        val email = reg_email_editText.text.toString()
        val password = reg_password_editText.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter your email and password", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("RegisterActivity", "Email is " + email)
        Log.d("RegisterActivity", "Password: $password")

        //Firebase authentication to create user with email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener

                    //else if successful
                    Log.d("RegisterActivity", "Successfully created user with uid: ${it.result.user.uid}")


                    uploadImageToFirebase()
                }
                .addOnFailureListener {
                    Log.d("RegisterActivity", "Failed to create user: ${it.message}")
                    Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()

                }
    }

    private fun uploadImageToFirebase() {

        if (selectPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectPhotoUri!!)
                .addOnSuccessListener {
                    Log.d("RegisterActivity", "Successfully uploaded image: ${it.metadata?.path}")

                    ref.downloadUrl.addOnSuccessListener {
                        Log.d("RegisterActivity","File location: $it")

                        saveUserToFirebaseDatabase(it.toString())
                    }
                }

              //  .addOnFailureListener{
                    //do some logging here
             //   }

    }

    private fun saveUserToFirebaseDatabase(profileImageUrl: String){
        val uid = FirebaseAuth.getInstance().uid ?:""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, reg_username_editText.text.toString(),profileImageUrl)

        ref.setValue(user)
                .addOnSuccessListener {
                    Log.d("RegisterActivity","User saved to database")
                }
    }
}

class User (val uid: String, val username: String, val profileImageUrl: String)



