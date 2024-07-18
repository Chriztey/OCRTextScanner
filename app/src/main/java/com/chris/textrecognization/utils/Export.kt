package com.chris.textrecognization.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult.Pdf
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

object Export {

//    fun exportToPdf(
//        context: Context,
//        pdf: Pdf
//    ) {
//
//
//        val fileName = "${UUID.randomUUID()}.pdf"
//        val externalStorageVolumes: Array<out File> = ContextCompat.getExternalFilesDirs(context, null)
//        val primaryExternalStorage = externalStorageVolumes[0]
//        val file = File(primaryExternalStorage, fileName)
//        val fos = FileOutputStream(file)
//        context.contentResolver.openInputStream(pdf.uri)?.use { it ->
//            it.copyTo(fos)
//        }
//        Log.d("FileSavePath", "File saved to: ${file.absolutePath}")
//        Toast.makeText(context, "File saved to: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
//
//    }


    fun exportFile(
        context: Context,
        uri: Uri,
        extention: String
    ) {

        val fileName = "${UUID.randomUUID()}.$extention"
        val externalStorageVolumes: Array<out File> = ContextCompat.getExternalFilesDirs(context, null)
        val primaryExternalStorage = externalStorageVolumes[0]
        val file = File(primaryExternalStorage, fileName)
        val fos = FileOutputStream(file)
        context.contentResolver.openInputStream(uri)?.use { it2 ->
            it2.copyTo(fos)
        }
        Log.d("FileSavePath", "File saved to: ${file.absolutePath}")
        Toast.makeText(context, "File saved to: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
    }


}