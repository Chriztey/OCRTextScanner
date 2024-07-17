package com.chris.textrecognization.screen

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.hilt.navigation.compose.hiltViewModel
import com.chris.textrecognization.viewmodel.TextRecognitionViewModel

@Composable
fun MainScreen(
    modifier: Modifier,
    viewModel: TextRecognitionViewModel = hiltViewModel()) {

    val text by viewModel.extractedText.collectAsState()
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            viewModel.getTextFromSelectedImage(it!!) }
    )

    var hasCameraPermission by remember { mutableStateOf(false) }
    //var capturedImage by remember { mutableStateOf<Bitmap?>(null) }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }


    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = {
            viewModel.getTextFromCapturedImage(it!!) }
    )



    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
        ) {

        Card(
            elevation = CardDefaults.cardElevation(7.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
            ) {
                item {
                    SelectionContainer {
                        Text(
                            text = text,
                            modifier = Modifier.padding(
                                vertical = 3.dp,
                                horizontal = 7.dp)
                        )
                    }
                }
            }
        }

        Button(
            onClick = { viewModel.copyTextToClipboard() },
            enabled = text.isNotEmpty()
        ) {
            Text(text = "Copy")
        }

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(onClick = {
                if(hasCameraPermission) {
                    cameraLauncher.launch()
                } else {
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }) {
                Text(text = "Start Camera")
            }

            Button(onClick = {
                galleryLauncher.launch(
                    "image/*"
                )
            }) {
                Text(text = "Choose From Gallery")
            }

        }

    }

}