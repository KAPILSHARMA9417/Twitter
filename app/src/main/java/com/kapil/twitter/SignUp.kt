package com.kapil.twitter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {
    private lateinit var mAuth:FirebaseAuth
   private lateinit  var mFirebaseAnalytics:FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        mFirebaseAnalytics= FirebaseAnalytics.getInstance(this)
        mAuth=FirebaseAuth.getInstance()

    }

    fun gotoSignUp(view: android.view.View) {
        var user=signup_userName.text.toString()
        var pass=signup_Password.text.toString()
        mAuth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener()
        { task ->
            if (task.isSuccessful) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                Toast.makeText(this, "Create user Successfully", Toast.LENGTH_LONG).show()

            } else {
                Toast.makeText(this, "Create user Fail", Toast.LENGTH_LONG).show()

            }

        }
    }
}