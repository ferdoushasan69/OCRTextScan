package com.example.ocrtextscan.domain

import android.graphics.Bitmap
import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface TextRecognitionRepository {

    fun recognizeTextFromCameraImage(bitmap: Bitmap) : Flow<String>

    fun recognizeTextFromGalleryImage(uri: Uri) : Flow<String>

    fun recognizeCopyText(text : String)
}