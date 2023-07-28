package com.kishan.qrscanner

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.util.Linkify
import android.webkit.URLUtil
import android.widget.Toast
import com.kishan.qrscanner.databinding.ActivityResultBinding
import java.net.URL

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val result = intent.getStringExtra("Result")
        val url = SpannableString.valueOf(result)
        Linkify.addLinks(url,Linkify.WEB_URLS)
        binding.textView2.text = url


        // button to copy text
        if (URLUtil.isValidUrl(url.toString())){
            binding.button.text = "Go to Website"
        }
        binding.button.setOnClickListener {
            if(URLUtil.isValidUrl(url.toString())){
                openWebPage(url.toString())
            }else{
                copyToClipBoard(result!!)
            }
        }



    }

    //logic to copy text
    private fun copyToClipBoard(result:String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", result)
        clipboard.setPrimaryClip(clipData)
        Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    private fun openWebPage(uri:String){
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(uri)
        startActivity(intent)
    }



}