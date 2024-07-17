package com.chris.textrecognization

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.chris.textrecognization.screen.MainScreen
import com.chris.textrecognization.screen.OCRScreen
import com.chris.textrecognization.ui.theme.TextRecognizationTheme
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TextRecognizationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    MainScreen(
//                        modifier = Modifier.padding(innerPadding)
//                    )


                    OCRScreen(
                        modifier = Modifier.padding(innerPadding),
                        activity = this@MainActivity,
                        exportPdf = {

                            val fileName = "scan.pdf"
                            val externalStorageVolumes: Array<out File> = ContextCompat.getExternalFilesDirs(applicationContext, null)
                            val primaryExternalStorage = externalStorageVolumes[0]
                            val file = File(primaryExternalStorage, fileName)
                            val fos = FileOutputStream(file)
                            contentResolver.openInputStream(it.uri)?.use { it2 ->
                                it2.copyTo(fos)
                            }
                            Log.d("FileSavePath", "File saved to: ${file.absolutePath}")
                            Toast.makeText(applicationContext, "Test", Toast.LENGTH_SHORT).show()

                        },

                        exportJpg = {
                            val fileName = "scan.jpg"
                            val externalStorageVolumes: Array<out File> = ContextCompat.getExternalFilesDirs(applicationContext, null)
                            val primaryExternalStorage = externalStorageVolumes[0]
                            val file = File(primaryExternalStorage, fileName)
                            val fos = FileOutputStream(file)
                            contentResolver.openInputStream(it)?.use { it2 ->
                                it2.copyTo(fos)
                            }
                            Log.d("FileSavePath", "File saved to: ${file.absolutePath}")
                            Toast.makeText(applicationContext, "Test", Toast.LENGTH_SHORT).show()
                        }
                    )

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TextRecognizationTheme {
        Greeting("Android")
    }
}