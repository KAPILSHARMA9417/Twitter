package com.kapil.twitter

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.health.PackageHealthStats
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI
import android.provider.MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI
import android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream
import java.security.Permission
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        if(mAuth!!.currentUser!=null)
        {
            var intent=Intent(this,login_1::class.java)
            startActivity(intent)
        }
        btn_signIn.setOnClickListener()
        {
            logIn()
        }
        image_User.setOnClickListener()
        {
            checkPermission()
        }
    }

    var READIMAGE = 253
    fun checkPermission() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    READIMAGE
                )
                return
            }
        }
        loadImage()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        when (requestCode) {
            READIMAGE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadImage()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Cannot access your image",
                        Toast.LENGTH_LONG
                    ).show()

                }

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    val PICK_IMAGE_CODE = 123

    fun loadImage() {

        var intent = Intent(
            Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(intent, PICK_IMAGE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE_CODE && data != null) {

//         val selectImage=data.data
//         val filePathCoulm=arrayOf(MediaStore.Images.Media.DATA)
//     val cursor=contentResolver.query(selectImage!!,filePathCoulm,null,null,null)
//         cursor!!.moveToFirst()
//         val coulomIndex=cursor.getColumnIndex(filePathCoulm[0])
//            val picturePath=cursor.getString(coulomIndex)
//         cursor.close()
//         image_User.setImageBitmap(BitmapFactory.decodeFile(picturePath))

            val selectImage = data.data
            var bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectImage)
            image_User.setImageBitmap(bitmap)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    fun signUp(view:View)
    {
        var intent:Intent=Intent(this@MainActivity,SignUp::class.java)
        startActivity(intent)
    }
    fun addImagetoFirebase()
    {
        var baseStorage=FirebaseStorage.getInstance()
        var storageRef=baseStorage.getReferenceFromUrl("gs://demofirebaseapp-5e49d.appspot.com")
        var df=SimpleDateFormat("ddMMyyHHmmss")
        var dateobj=Date()
        val imagePath=splitEmail(mAuth!!.currentUser!!.email.toString())+df.format(dateobj)+".jpg"
        val imageRef=storageRef.child("images/"+imagePath)
        image_User.isDrawingCacheEnabled=true
        image_User.buildDrawingCache()

        val drawable=image_User.drawable as BitmapDrawable
        val bitmap=drawable.bitmap
        val baos=ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
         val data=baos.toByteArray()
        val uploadTask=imageRef.putBytes(data)
        uploadTask.addOnFailureListener()
        {
Toast.makeText(this,"stored image failure",Toast.LENGTH_LONG).show()
        }.addOnSuccessListener {
       taskSnapshot->

//var DownloadURL=taskSnapshot.downloadUrl!!.toString()
            Toast.makeText(this,"stored image success",Toast.LENGTH_LONG).show()

        }




    }
    fun splitEmail(email:String):String
    {
       var split= email.split("@")
        return split[0].toString()
    }
    fun logIn()
    {
        var user=userName.text.toString()
        var pass=Password.text.toString()
        if(user!=null)
        {
            mAuth!!.signInWithEmailAndPassword(user,pass).addOnCompleteListener()
            {

                task->

                    if(task.isSuccessful)
                    {
                        addImagetoFirebase()
                        Toast.makeText(this@MainActivity,"Sign Successfully",Toast.LENGTH_LONG).show()

                        var intent:Intent=Intent(this@MainActivity,login_1::class.java)
                        startActivity(intent)

                    }
                else
                    {
                        Toast.makeText(this@MainActivity,"Sign UnSuccessfully",Toast.LENGTH_LONG).show()

                    }

            }

        }
    }
}