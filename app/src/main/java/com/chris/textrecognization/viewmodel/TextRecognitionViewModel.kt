package com.chris.textrecognization.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chris.textrecognization.domain.repo.MainRepo
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult.Pdf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class TextRecognitionViewModel @Inject constructor(
    private val mainRepo: MainRepo
): ViewModel() {

    private var _extractedText = MutableStateFlow("")
    val extractedText: StateFlow<String> = _extractedText.asStateFlow()

    private var _ocrScanResult = MutableStateFlow<GmsDocumentScanningResult.Pdf?>(null)
    val ocrScanResult: StateFlow<GmsDocumentScanningResult.Pdf?> = _ocrScanResult.asStateFlow()

    private var _jpgScanResult = MutableStateFlow<List<Uri>>(emptyList())
    val jpgScanResult: StateFlow<List<Uri>> = _jpgScanResult.asStateFlow()

    fun updateOcrScan(pdf: Pdf) {
        _ocrScanResult.value = pdf
    }

    fun updatejpgScan(jpg: Uri) {
        _jpgScanResult.value += jpg
    }



    fun getTextFromCapturedImage(bitmap: Bitmap) {
        viewModelScope.launch {
            mainRepo.getTextFromCapturedImage(bitmap)
                .collect {
                    _extractedText.value = it }
        }
    }

    fun getTextFromSelectedImage(uri: Uri) {
        viewModelScope.launch {
            mainRepo.getTextFromSelectedImage(uri)
                .collect {
                    _extractedText.value = it }
        }
    }

    fun copyTextToClipboard() {
        viewModelScope.launch {
            mainRepo.copyTextToClipboard(_extractedText.value)
        }
    }




}