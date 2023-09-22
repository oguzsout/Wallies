package com.oguzdogdu.wallieshd.util

import android.Manifest
import android.app.DownloadManager
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.oguzdogdu.wallieshd.presentation.main.MainActivity
import dagger.hilt.android.internal.managers.FragmentComponentManager
import java.io.File
import java.io.IOException

fun Context.downloadImage(url: String, directoryName: String, fileName: String): Boolean {
    when {
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED -> {
            ActivityCompat.requestPermissions(
                FragmentComponentManager.findActivity(this) as MainActivity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                123
            )
            return false
        }

        else -> {
            val directory = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                directoryName
            )
            if (!directory.exists()) {
                directory.mkdirs()
            }
            val file = File(directory, fileName)

            if (file.exists()) {
                return false
            }

            try {
                val downloadManager =
                    this.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                val downloadUri = Uri.parse(url)
                val request = DownloadManager.Request(downloadUri).apply {
                    setAllowedNetworkTypes(
                        DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE
                    )
                        .setMimeType("image/*")
                        .setAllowedOverRoaming(true)
                        .setNotificationVisibility(
                            DownloadManager.Request.VISIBILITY_VISIBLE
                        )
                        .setTitle("Wallies")
                        .setDestinationUri(Uri.fromFile(file))
                }
                downloadManager.enqueue(request)
                return true
            } catch (e: Exception) {
                Toast.makeText(this, "Image download failed: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
            return false
        }
    }
}

fun Uri.toBitmap(context: Context): Bitmap? {
    val contentResolver: ContentResolver = context.contentResolver

    var bitmap: Bitmap? = null
    if (Build.VERSION.SDK_INT >= 29) {
        val source: ImageDecoder.Source = ImageDecoder.createSource(contentResolver, this)
        try {
            bitmap = ImageDecoder.decodeBitmap(source)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    } else {
        try {
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, this)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return bitmap
}
