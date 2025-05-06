package com.example.ocrtextscan.data

import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.example.ocrtextscan.domain.TextRecognitionRepository
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognizer
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class TextRecognitionRepositoryImpl @Inject constructor(
    private val context: Context,
    private val recognizer: TextRecognizer,
    private val clipboardManager: ClipboardManager
): TextRecognitionRepository{
    override fun recognizeTextFromCameraImage(bitmap: Bitmap): Flow<String> {
        return callbackFlow {
            val inputImage = InputImage.fromBitmap(bitmap,0)
            recognizer.process(inputImage)
                .addOnSuccessListener {
                    launch {
                        send(it.text)
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, "recognizeTextFromCameraImage: ${it.message}")
                }
            awaitClose{cancel()}
        }
    }

    override fun recognizeTextFromGalleryImage(uri: Uri): Flow<String> {
        return callbackFlow {
            val inputImage = InputImage.fromFilePath(context,uri)
            recognizer.process(inputImage)
                .addOnSuccessListener {
                    launch {
                        send(it.text)
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, "recognizeTextFromGalleryImage: ${it.cause}")
                }
            awaitClose{cancel()}
        }
    }

    override fun recognizeCopyText(text: String) {
        clipboardManager.setPrimaryClip(ClipData.newPlainText("",text))

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2){
            Toast.makeText(context,"Copied", Toast.LENGTH_SHORT).show()
        }
    }
}