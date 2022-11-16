package com.jenny.deara.mypages

import android.content.ContentResolver
import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.widget.Toast
import androidx.databinding.DataBindingUtil.setContentView
import com.jenny.deara.R

class ScreenshotDetectActivity(private val context: Context) {

    private var contentObserver: ContentObserver? = null

    fun start() {
        if (contentObserver == null) {
            contentObserver = context.contentResolver.registerObserver()
        }
    }

    fun stop() {
        contentObserver?.let { context.contentResolver.unregisterContentObserver(it) }
        contentObserver = null
    }

    private fun queryScreenshots(uri: Uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            queryRelativeDataColumn(uri)
        } else {
            queryDataColumn(uri)
        }
    }

    private fun queryDataColumn(uri: Uri) {
        val projection = arrayOf(
            MediaStore.Images.Media.DATA
        )
        context.contentResolver.query(
            uri,
            projection,
            null,
            null,
            null
        )?.use { cursor ->
            val dataColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
            while (cursor.moveToNext()) {
                val path = cursor.getString(dataColumn)
                if (path.contains("screenshot", true)) {
                    // do something
                    Toast.makeText(context, "캡쳐가 감지되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun queryRelativeDataColumn(uri: Uri) {
        val projection = arrayOf(
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.RELATIVE_PATH
        )
        context.contentResolver.query(
            uri,
            projection,
            null,
            null,
            null
        )?.use { cursor ->
            val relativePathColumn =
                cursor.getColumnIndex(MediaStore.Images.Media.RELATIVE_PATH)
            val displayNameColumn =
                cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
            while (cursor.moveToNext()) {
                val name = cursor.getString(displayNameColumn)
                val relativePath = cursor.getString(relativePathColumn)
                if (name.contains("screenshot", true) or
                    relativePath.contains("screenshot", true)
                ) {
                    // do something
                    Toast.makeText(context, "토스트 메세지 띄우기 입니다.", Toast.LENGTH_SHORT).show()
                    //Toast.makeText(context, name, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun ContentResolver.registerObserver(): ContentObserver {
        val contentObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean, uri: Uri?) {
                super.onChange(selfChange, uri)
                uri?.let { queryScreenshots(it) }
            }
        }
        registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true, contentObserver)
        return contentObserver
    }
}
