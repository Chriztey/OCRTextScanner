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
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import com.chris.textrecognization.navigation.AppNavHost
import com.chris.textrecognization.screen.OCRScreen
import com.example.compose.TextRecognizationTheme
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
                    AppNavHost(
                        activity = this@MainActivity)
            }
        }
    }
}

