package com.jenny.deara.board

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.jenny.deara.R
import com.jenny.deara.databinding.ActivityBoardImageViewBinding
import kotlinx.android.synthetic.main.activity_board_image_view.*
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.net.URLConnection

class BoardImageViewActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBoardImageViewBinding

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_image_view)

        val url = intent.getParcelableExtra<Uri>("url")
        Log.d("imgTest", url.toString())

        binding.x.setOnClickListener {
            finish()
        }

        Glide.with(this)
            .load(url)
            .into(imageView)
    }
}