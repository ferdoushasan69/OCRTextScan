package com.example.ocrtextscan.presentation.home

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ocrtextscan.domain.TextRecognitionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: TextRecognitionRepository
) : ViewModel() {

    private val _selectedText = MutableStateFlow("")
    val selectedText = _selectedText.asStateFlow()

    fun recognizeTextFromCameraImage(bitmap: Bitmap?) {
        bitmap?.let {bitmap->

            viewModelScope.launch {
                repository.recognizeTextFromCameraImage(bitmap)
                    .collect {
                        _selectedText.value = it
                    }
            }
        }
    }

    fun recognizeTextFromGalleryImage(uri: Uri?) {
        uri?.let { uri ->
            viewModelScope.launch {
                repository.recognizeTextFromGalleryImage(uri)
                    .collect {
                        _selectedText.value = it
                    }
            }
        }
    }

    fun copyTextFromRecognizeText() {
        repository.recognizeCopyText(_selectedText.value)
    }

    fun clearText(){
        _selectedText.value = ""
    }
}