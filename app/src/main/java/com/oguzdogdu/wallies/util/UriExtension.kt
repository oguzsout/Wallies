package com.oguzdogdu.wallies.util

import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File

fun Context.downloadImage(url: String, directoryName: String, fileName: String) {
    when {
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED -> {
            ActivityCompat.requestPermissions(
                this as Activity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                123
            )
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
            try {
                val downloadManager =
                    this.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                val downloadUri = Uri.parse(url)
                val request = DownloadManager.Request(downloadUri).apply {
                    setAllowedNetworkTypes(
                        DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE
                    )
                        .setMimeType("image/*")
                        .setAllowedOverRoaming(false)
                        .setNotificationVisibility(
                            DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
                        )
                        .setTitle("Wallies")
                        .setDestinationUri(Uri.fromFile(file))
                }
                downloadManager.enqueue(request)
            } catch (e: Exception) {
                Toast.makeText(this, "Image download failed: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}
