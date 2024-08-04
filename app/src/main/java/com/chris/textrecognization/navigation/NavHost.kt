package com.chris.textrecognization.navigation

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.chris.textrecognization.screen.MainScreen
import com.chris.textrecognization.screen.OCRScreen
import com.chris.textrecognization.screen.RecognizeTextScreen
import com.chris.textrecognization.utils.Export
import kotlinx.serialization.Serializable

@Composable
fun AppNavHost(
    activity: Activity,
) {
    val navController = rememberNavController()

    NavHost(
            navController = navController,
            startDestination = MainScreen
        ) {

            composable<MainScreen> {
                MainScreen(
                    navigateToOCR = {
                        navController.navigate(OCRScreen)
                    },
                    recognizeText = {
                        navController.navigate(TextRecognizeScreen)
                    }
                )
            }

            composable<TextRecognizeScreen> {
                RecognizeTextScreen(
                    modifier = Modifier,
                    onNavigateBack = {
                        navController.navigate(MainScreen)
                    }
                    )
            }

            composable<OCRScreen> {

                val context = LocalContext.current

                OCRScreen(
                    modifier = Modifier,
                    activity = activity,
                    exportPdf = {
                        Export.exportFile(
                            context = context,
                            uri = it.uri,
                            extension = "pdf"
                        )
                    },
                    exportJpg = {
                        Export.exportFile(
                            context = context,
                            uri = it,
                            extension = "jpg"
                        )
                    },
                    onNavigateBack = {
                        navController.navigate(MainScreen)
                    }
                )
            }
        }

}

@Serializable
object TextRecognizeScreen
@Serializable
object OCRScreen
@Serializable
object MainScreen