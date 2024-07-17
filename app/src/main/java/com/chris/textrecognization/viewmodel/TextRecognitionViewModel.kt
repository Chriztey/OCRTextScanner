package com.chris.textrecognization.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chris.textrecognization.domain.repo.MainRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TextRecognitionViewModel @Inject constructor(
    private val mainRepo: MainRepo
): ViewModel() {
    private var _extractedText = MutableStateFlow("")
    val extractedText: StateFlow<String> = _extractedText.asStateFlow()

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