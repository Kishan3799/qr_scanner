package com.kishan.qrscanner

import android.Manifest
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.ClipboardManager
import android.text.SpannableString
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.kishan.qrscanner.databinding.ActivityMainBinding


private const val CAMERA_REQUEST_CODD = 101

class MainActivity : AppCompatActivity() {

    private lateinit var codeScanner : CodeScanner
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPermission()
        qrCodeScanner()
    }

    private fun qrCodeScanner() {


        codeScanner = CodeScanner(this, binding.scannerView)

        codeScanner.apply {
            camera  = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true

            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                runOnUiThread {
                    sendResultActivity(it.text)
                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.d("Main2" ,"Camera initialization error: ${it.message}")
                }
            }

        }


        binding.scannerView.setOnClickListener { codeScanner.startPreview() }


    }

    private fun sendResultActivity(text: String?) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("Result" , text)
        startActivity(intent)
    }


    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setupPermission(){
        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        )

        if (permission != PackageManager.PERMISSION_GRANTED){
            makeRequestForPermission()
        }
    }

    private fun makeRequestForPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODD
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            CAMERA_REQUEST_CODD -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,"Camera permission is needed", Toast.LENGTH_SHORT).show()
                }else {
                    Log.d("MAIN", "Launch successfully")
                }
            }
        }
    }


}