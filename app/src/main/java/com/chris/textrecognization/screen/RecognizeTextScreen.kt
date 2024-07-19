package com.chris.textrecognization.screen

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import androidx.core.content.ContextCompat

import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.chris.textrecognization.viewmodel.TextRecognitionViewModel

@Composable
fun RecognizeTextScreen(
    modifier: Modifier,
    viewModel: TextRecognitionViewModel = hiltViewModel()) {

    val context = LocalContext.current

    val text by viewModel.extractedText.collectAsState()
    val capturedImage by viewModel.capturedImage.collectAsState()
    val galleryImage by viewModel.galleryImage.collectAsState()


    var hasCameraPermission by remember { mutableStateOf(false) }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            hasCameraPermission = true
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            viewModel.updateCapturedImage(it)
            viewModel.getTextFromCapturedImage(it)
            viewModel.updateGalleryImage(null)
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            if (it != null) {
                viewModel.updateGalleryImage(it)
                viewModel.getTextFromSelectedImage(it)
                viewModel.updateCapturedImage(null)
            }
        }
    )



    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
        ) {

//        Card(
//            elevation = CardDefaults.cardElevation(7.dp)
//        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
            ) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(
                                min = 200.dp,
                            ),
                        elevation = CardDefaults.cardElevation(7.dp)
                    ) {
                        SelectionContainer {
                            Text(
                                text = text,
                                modifier = Modifier.padding(
                                    vertical = 3.dp,
                                    horizontal = 7.dp
                                )
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }

                item {
                    when {
                        galleryImage == null && capturedImage == null -> Text(text = "No Image Selected")
                        capturedImage != null ->
                            Image(
                                bitmap = capturedImage!!.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier.size(200.dp)
                            )
                        else ->
                            AsyncImage(
                                model = ImageRequest
                                    .Builder(context)
                                    .data(galleryImage)
                                    .crossfade(true)
                                    .build(),
                                contentScale = ContentScale.Fit,
                                contentDescription = null)
                    }
                }

            }
//        }
        






//                capturedImage?.let { bitmap ->
//                    Image(
//                        bitmap = bitmap.asImageBitmap(),
//                        contentDescription = null,
//                        modifier = Modifier.size(200.dp)
//                    )
//                }



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