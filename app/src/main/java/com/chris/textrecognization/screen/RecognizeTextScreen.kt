package com.chris.textrecognization.screen

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import android.widget.ToggleButton
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import androidx.core.content.ContextCompat

import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.chris.textrecognization.R
import com.chris.textrecognization.component.AppBottomBar
import com.chris.textrecognization.component.AppTopBar
import com.chris.textrecognization.viewmodel.MainScreenState
import com.chris.textrecognization.viewmodel.TextRecognitionViewModel

@Composable
fun RecognizeTextScreen(
    modifier: Modifier = Modifier,
    viewModel: TextRecognitionViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
    ) {

    var openConvertDialog by remember {
        mutableStateOf(false)
    }

    var toggleShowImage by remember {
        mutableStateOf(false)
    }

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

    if (openConvertDialog) {
        ConvertDialogBox (
            onDismiss = { openConvertDialog = false },
            cameraLauncher = { if(hasCameraPermission) {
                cameraLauncher.launch()}
            else {cameraPermissionLauncher
                .launch(Manifest.permission.CAMERA)}
                openConvertDialog = false
            },
            galleryLauncher = {
                galleryLauncher.launch(
                    "image/*")
                openConvertDialog = false
            }
        )
    }




    if (text.isBlank()) {

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .size(
                        height = 255.dp,
                        width = 270.dp
                    )
                    .background(Color.Gray.copy(alpha = 0.3f))
            )

            Image(
                modifier = Modifier
                    .size(
                        height = 300.dp,
                        width = 280.dp
                    )
                    .clickable {
                        openConvertDialog = true
                    },
                contentScale = ContentScale.FillBounds,
                painter = painterResource(id = R.drawable.button_startconvert),
                contentDescription = "text recognize")
        }

    } else {

        Scaffold(
            topBar = {
                AppTopBar(title = "ImageToText") {
                    onNavigateBack()
                }
            },
            bottomBar = {
                AppBottomBar(label = "Change Image") {
                    openConvertDialog = true
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .height(426.dp)
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.9f)
                                    .heightIn(
                                        min = 360.dp,
                                    )
                                    .align(Alignment.Center)
                                    .verticalScroll(rememberScrollState()),
                                elevation = CardDefaults.cardElevation(7.dp)
                            ) {
                                SelectionContainer {
                                    Text(
                                        text = text,
                                        modifier = Modifier.padding(
                                            start = 8.dp,
                                            end = 8.dp,
                                            top = 4.dp,
                                            bottom = 16.dp
                                        )
                                    )
                                }
                            }

                            Button(
                                onClick = {
                                    viewModel.copyTextToClipboard()
                                },
                                enabled = text.isNotBlank(),
                                modifier = Modifier.align(Alignment.BottomCenter)
                            ) {
                                Text(text = "Copy All")
                            }

                        }
                    }

                    item {
                        HorizontalDivider(
                            thickness = 2.dp,
                            color = MaterialTheme.colorScheme.secondaryContainer
                        )
                    }

                    item {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Switch(
                                checked = toggleShowImage,
                                onCheckedChange = { toggleShowImage = !toggleShowImage })
                            Text(
                                text = "show image",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                    }

                    if (toggleShowImage) {

                        item {

                            Box(
                                modifier = modifier
                                    .fillParentMaxWidth(),
                                contentAlignment = Alignment.Center
                                ) {
                                when {
                                    galleryImage == null && capturedImage == null -> Text(text = "No Image Selected")
                                    capturedImage != null ->
                                        Image(
                                            bitmap = capturedImage!!.asImageBitmap(),
                                            contentDescription = null,
                                            Modifier
                                                .size(500.dp),
                                            contentScale = ContentScale.Fit,
                                        )

                                    else ->
                                        AsyncImage(
                                            model = ImageRequest
                                                .Builder(context)
                                                .data(galleryImage)
                                                .crossfade(true)
                                                .build(),
                                            contentScale = ContentScale.Fit,
                                            contentDescription = null
                                        )
                                }
                            }


                        }


                    }


                }

            }
        }

    }



}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConvertDialogBox(
    onDismiss: () -> Unit,
    cameraLauncher: () -> Unit,
    galleryLauncher: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = {
            onDismiss() }) {

        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(28.dp))
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainer)
                ,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)

            ) {
                Icon(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    painter = painterResource(
                        id = R.drawable.baseline_image_24
                    ),
                    tint = MaterialTheme.colorScheme.secondary,
                    contentDescription = "image"
                )

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Start Convert",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall)

                Text(
                    text = "Select an image or pick image from gallery to start converting.",
                    style = MaterialTheme.typography.bodyMedium)

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { cameraLauncher() }) {
                    Text(text = "Camera")
                }

                Spacer(modifier = Modifier.width(8.dp))

                TextButton(onClick = { galleryLauncher() }) {
                    Text(text = "Gallery")
                }
            }

        }

    }
}