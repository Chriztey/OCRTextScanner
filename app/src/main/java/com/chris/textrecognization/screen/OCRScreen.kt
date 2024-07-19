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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.chris.textrecognization.R
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
    viewModel: TextRecognitionViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val ocrScan by viewModel.ocrScanResult.collectAsState()

    val options = ScannerModule.provideGmsDocumentScannerOptions()

    var imageUris by remember {
        mutableStateOf<List<Uri>>(emptyList())
    }

    val scannedImage by viewModel.jpgScanResult.collectAsState()

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

    @Composable
    fun OCRScanButton() {
        Button(
            shape = RoundedCornerShape(3.dp),
            onClick = {
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
            }) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_camera_24),
                contentDescription = "Scan")
            Text(text = "Scan")
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        imageUris.forEach { uri ->
            viewModel.updatejpgScan(uri)
            AsyncImage(
                model = uri,
                contentDescription =  null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                modifier = Modifier.align(Alignment.End),
                shape = RoundedCornerShape(3.dp),
                onClick = { exportJpg(uri) }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_image_24),
                    contentDescription = "Export")
                Text(text = "Export JPG")
            }

        }

        if (ocrScan == null) {

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Start Scan Document Now")
                Spacer(modifier = Modifier.height(3.dp))
                OCRScanButton()
            }

        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalDivider(
                    thickness = 5.dp
                )

                OCRScanButton()

//                Button(
//                    shape = RoundedCornerShape(3.dp),
//                    onClick = {
//                        scanner.getStartScanIntent(activity)
//                            .addOnSuccessListener {
//                                scannerLauncher.launch(
//                                    IntentSenderRequest.Builder(it).build()
//                                )
//                            }
//                            .addOnFailureListener {
//                                Toast.makeText(
//                                    context,
//                                    it.message,
//                                    Toast.LENGTH_LONG
//                                ).show()
//                            }
//                    }) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.baseline_camera_24),
//                        contentDescription = "Scan")
//                    Text(text = "Scan")
//                }

                Button(
                    shape = RoundedCornerShape(3.dp),
                    onClick = {
                        ocrScan?.let {exportPdf(it)}
                    },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_document_scanner_24),
                        contentDescription = "Export")
                    Text(text = "Export PDF")
                }

                Button(
                    shape = RoundedCornerShape(3.dp),
                    onClick = {
                        for (i in scannedImage) {
                            exportJpg(i)
                        } },
                    ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_document_scanner_24),
                        contentDescription = "Export")
                    Text(text = "Export JPG")
                }

            }
        }




    }


}


