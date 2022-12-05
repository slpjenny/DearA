package com.jenny.deara.board

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.jenny.deara.R
import com.jenny.deara.databinding.ActivityBoardImageViewBinding
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.net.URLConnection

class BoardImageViewActivity : AppCompatActivity() {

//    private lateinit var binding : ActivityBoardImageViewBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_image_view)
//
//        val url = intent.getParcelableExtra<Uri>("url")
//        Log.d("imgTest", url.toString())
//
//        binding.x.setOnClickListener {
//            finish()
//        }
//
//        binding.imageView.setImageBitmap(url?.let { getImageBitmap(it) })
//    }
//
//    private fun getImageBitmap(url: Uri): Bitmap {
//        lateinit var bm: Bitmap
//        try {
//            val aURL = URL(url.toString())
//            val conn: URLConnection = aURL.openConnection()
//            conn.connect()
//            val `is`: InputStream = conn.getInputStream()
//            val bis = BufferedInputStream(`is`)
//            bm = BitmapFactory.decodeStream(bis)
//            bis.close()
//            `is`.close()
//        } catch (e: IOException) {
//            Log.e("BiteMap", "Error getting bitmap", e)
//        }
//        return bm
//    }
}