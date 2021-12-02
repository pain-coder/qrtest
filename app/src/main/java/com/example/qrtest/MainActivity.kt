package com.example.qrtest

import android.app.Activity
import android.content.pm.PackageManager
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerException
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import kotlinx.android.synthetic.main.activity_main.*

private const val CAMERA_REQUEST_CODE =101
var varres:Any?=null

class MainActivity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupPermissions()
        codeScanner()
    }

    private fun codeScanner() {
        codeScanner = CodeScanner(this, scanner_view)
        var screen = findViewById<TextView>(R.id.tv_textView)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                runOnUiThread{
                    screen.text= varres.toString()
                }

            }

             errorCallback = ErrorCallback {
                 runOnUiThread {
                     Log.e("Main", "Camera initialization error: ${it.message}")
                 }
             }
        }

        scanner_view.setOnClickListener {
            codeScanner.startPreview()
        }

    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    fun readsc(){
        varres = codeScanner.decodeCallback
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this,
        android.Manifest.permission.CAMERA,)

        if (permission != PackageManager.PERMISSION_GRANTED){
            makeRequest()
        }


    }
    private fun makeRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode)
        {
          CAMERA_REQUEST_CODE ->
          {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "you need camera permission to run this app", Toast.LENGTH_SHORT).show()

            }
             else
             {
              //successfulRequest
             }
          }
        }
    }
}

