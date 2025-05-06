package com.example.ocrtextscan.presentation.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ocrtextscan.R
import com.example.ocrtextscan.ui.theme.Green
import java.nio.file.WatchEvent

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {

    var text = viewModel.selectedText.collectAsState().value

    val context = LocalContext.current
    val cameraPermission = Manifest.permission.CAMERA

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = GetContent(),
        onResult = viewModel::recognizeTextFromGalleryImage
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = viewModel::recognizeTextFromCameraImage
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch(null)
        } else {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text("MyOCR ", fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(16.dp))
            Card(
                modifier = Modifier
                    .fillMaxHeight(.7f)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(2.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                LazyColumn {
                    item {
                        SelectionContainer {
                            Text(text = text, modifier = Modifier.padding(8.dp))
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        if (text.isNotEmpty()) {
                            viewModel.clearText()
                            Toast.makeText(context, "Text Removed", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Text is empty", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp), colors = ButtonDefaults.buttonColors(containerColor = Green)
                ) { Text("Clear Text", fontSize = 14.sp) }
                Button(
                    onClick = {
                        if (text.isNotEmpty()) {
                            viewModel.copyTextFromRecognizeText()
                            Toast.makeText(context, "Text Copied", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Text is empty", Toast.LENGTH_SHORT).show()
                        }
                    },
                    enabled = text.isNotBlank(),
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp), colors = ButtonDefaults.buttonColors(containerColor = Green)
                ) { Text("Copy Text", fontSize = 14.sp) }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    modifier = Modifier
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Green),
                    onClick = {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (ContextCompat.checkSelfPermission(
                                    context,
                                    cameraPermission
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                cameraLauncher.launch(null)

                            } else {
                                permissionLauncher.launch(cameraPermission)
                            }
                        } else {
                            cameraLauncher.launch(null)
                        }
                    },
                    shape = RoundedCornerShape(20.dp),
                    elevation = ButtonDefaults.elevatedButtonElevation(1.dp),
                ) {
                    Text("Open Camera")
                }
                Spacer(Modifier.width(8.dp))

                Button(
                    modifier = Modifier
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Green),
                    onClick = {
                        galleryLauncher.launch("image/*")
                    },
                    shape = RoundedCornerShape(20.dp),
                    elevation = ButtonDefaults.elevatedButtonElevation(1.dp),
                ) {
                    Text("Open Gallery")
                }
            }
        }

    }


}


@Preview
@Composable
private fun HomePreview() {
    HomeScreen()

}