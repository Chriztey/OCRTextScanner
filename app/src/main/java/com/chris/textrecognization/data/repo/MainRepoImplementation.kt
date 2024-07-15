package com.chris.textrecognization.data.repo

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.chris.textrecognization.domain.repo.MainRepo
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognizer
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainRepoImplementation @Inject constructor (
    private val context: Context,
    private val recognizer: TextRecognizer,
    private val clipboardManager: ClipboardManager
): MainRepo  {
    override fun getTextFromCapturedImage(bitmap: Bitmap): Flow<String> {
        return callbackFlow {
            val inputImage = InputImage.fromBitmap(bitmap, 0)
            recognizer.process(
                inputImage
            )
                .addOnSuccessListener {
                    launch {
                        send(it.text)
                    }
                }
                .addOnFailureListener {
                    it.printStackTrace()
                }
            awaitClose {  }
        }
    }

    override fun getTextFromSelectedImage(uri: Uri): Flow<String> {
        return callbackFlow {
            val inputImage = InputImage.fromFilePath(context, uri)
            recognizer.process(
                inputImage
            )
                .addOnSuccessListener {
                    launch {
                        send(it.text)
                    }
                }
                .addOnFailureListener {
                    it.printStackTrace()
                }
            awaitClose {  }
        }
    }

    override fun copyTextToClipboard(text: String) {
        clipboardManager.setPrimaryClip(
            ClipData.newPlainText("", text)
        )
    }
}