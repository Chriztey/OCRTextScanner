package com.chris.textrecognization.navigation

import android.app.Activity
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
    modifier: Modifier
) {

    val navController = rememberNavController()
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = MainScreen) {

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
            RecognizeTextScreen(modifier = Modifier)
        }

        composable<OCRScreen> {

            val context = LocalContext.current

            OCRScreen(
                modifier = Modifier,
                activity = activity,
                exportPdf = { Export.exportFile(
                    context = context,
                    uri = it.uri,
                    extention = "pdf") },
                exportJpg = { Export.exportFile(
                    context = context,
                    uri = it,
                    extention = "jpg") }
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