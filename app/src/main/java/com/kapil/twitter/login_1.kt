package com.kapil.twitter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth

class login_1 : AppCompatActivity() {
    var mAuth:FirebaseAuth?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth= FirebaseAuth.getInstance()
        setContentView(R.layout.activity_login1)

    }

    fun signOut(view: android.view.View) {
        mAuth!!.signOut()
        var intent=Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}