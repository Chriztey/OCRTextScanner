package com.chris.textrecognization.screen

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.IntentSender
import android.graphics.Paint.Align
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.chris.textrecognization.R
import com.chris.textrecognization.component.AppBottomBar
import com.chris.textrecognization.component.AppTopBar
import com.chris.textrecognization.di.ScannerModule
import com.chris.textrecognization.viewmodel.TextRecognitionViewModel
import com.google.mlkit.vision.documentscanner.GmsDocumentScanner
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_JPEG
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_PDF
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.SCANNER_MODE_FULL
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult.Pdf
import java.io.File
import java.io.FileOutputStream
import java.util.Scanner

@Composable
fun OCRScreen(
    modifier: Modifier,
    activity: Activity,
    exportPdf: (Pdf) -> Unit,
    exportJpg: (Uri) -> Unit,
    viewModel: TextRecognitionViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {

    val context = LocalContext.current

    // Scan Images
    var imageUris by rememberSaveable {
        mutableStateOf<List<Uri>>(emptyList())
    }

    val scannedImage by viewModel.jpgScanResult.collectAsState()

    val ocrScan by viewModel.ocrScanResult.collectAsState()

    // Scanner
    val options = ScannerModule.provideGmsDocumentScannerOptions()

    val scanner = ScannerModule.provideGmsDocumentScanning(options)

    val scannerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = {
            if(it.resultCode == RESULT_OK) {
                val result = GmsDocumentScanningResult.fromActivityResultIntent(it.data)
                imageUris = result?.pages?.map { it.imageUri } ?: emptyList()


                result?.pdf?.let { pdf ->
                    viewModel.updateOcrScan(pdf)
                }
            }
        }
    )


    // Scanner Function
    fun startScanner() {
        scanner.getStartScanIntent(activity)
            .addOnSuccessListener {
                scannerLauncher.launch(
                    IntentSenderRequest.Builder(it).build()
                )
            }
            .addOnFailureListener {
                Toast.makeText(
                    context,
                    it.message,
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    // Pager
    val pagerState = rememberPagerState(pageCount = {
        imageUris.size
    })

    // Confirmation ReScan
    var doRescan by remember {
        mutableStateOf(false)
    }



    // Screen
    if (imageUris.isEmpty()) {

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
                        startScanner()
                    },
                contentScale = ContentScale.FillBounds,
                painter = painterResource(id = R.drawable.button_startscanning),
                contentDescription = "ocr")
        }

    } else {

        Scaffold(
            topBar = {
                AppTopBar(title = "OCR") {
                    onNavigateBack()
                }
            },

            bottomBar = {
                AppBottomBar(label = "ReScan") {
                    doRescan = true
                }
            }

        ) {

            if (doRescan) {
                rescanConfirmation(onDismiss = { doRescan = false }) {
                    startScanner()
                }
            }

            Column(
                modifier = modifier
                    .padding(it)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        HorizontalPager(
                            modifier = Modifier.fillMaxSize(),
                            state = pagerState
                        ) { page ->
                        Box(
                            modifier = Modifier.height(426.dp)
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.9f)
                                    .heightIn(min = 360.dp)
                                    .align(Alignment.Center)
                                    .verticalScroll(rememberScrollState()),
                                elevation = CardDefaults.cardElevation(7.dp)
                            ) {


                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .heightIn(360.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                         AsyncImage(
                                             modifier = Modifier.align(Alignment.Center),
                                             model = imageUris[page],
                                             contentDescription = null,
                                             contentScale = ContentScale.Fit,
                                         )


                                    }
                                }

                            Button(
                                onClick = {
                                    exportJpg(imageUris[page])
                                },
                                modifier = Modifier.align(Alignment.BottomCenter)
                            ) {
                                Text(text = "Export as JPG")
                            }

                            }
                        }
                    }

                    item {
                        Row(
                            Modifier
                                .wrapContentHeight()
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            repeat(pagerState.pageCount) { iteration ->
                                val color = if (
                                    pagerState.currentPage == iteration)
                                    MaterialTheme.colorScheme.primary else
                                        MaterialTheme.colorScheme.secondary
                                Box(
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .size(4.dp)
                                )
                            }
                        }
                    }

                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Export All",
                                style = MaterialTheme.typography.titleLarge
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Button(
                                    modifier = Modifier
                                        .sizeIn(minWidth = 140.dp),
                                    elevation = ButtonDefaults.buttonElevation(4.dp),
                                    onClick = {
                                        ocrScan?.let {exportPdf(it)}
                                    }) {
                                    Text(text = "PDF")
                                }

                                Button(
                                    modifier = Modifier
                                        .sizeIn(minWidth = 140.dp),
                                    elevation = ButtonDefaults.buttonElevation(4.dp),
                                    onClick = {
                                        for (i in imageUris) {
                                            exportJpg(i)
                                        }
                                    }) {
                                    Text(text = "JPG")
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
fun rescanConfirmation(
    onDismiss: () -> Unit,
    launchScanner: () -> Unit
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
                    text = "ReScan Document",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall)

                Text(
                    text = "Warning: This action will clear previous scanned image.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium)

                Text(
                    text = "Do you want to continue ?",
                    style = MaterialTheme.typography.bodyMedium)

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { onDismiss() }) {
                    Text(text = "NO")
                }

                Spacer(modifier = Modifier.width(8.dp))

                TextButton(onClick = {
                    launchScanner()
                    onDismiss()
                }) {
                    Text(text = "YES")
                }
            }

        }

    }

}