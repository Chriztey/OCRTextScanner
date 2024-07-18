package com.chris.textrecognization.di

import android.app.Application
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.chris.textrecognization.data.repo.MainRepoImplementation
import com.chris.textrecognization.domain.repo.MainRepo
import com.chris.textrecognization.viewmodel.TextRecognitionViewModel
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.io.File
import java.io.FileOutputStream
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideTextRecognizer(): TextRecognizer{
        return TextRecognition.getClient(
            TextRecognizerOptions.DEFAULT_OPTIONS
        )
    }

    @Provides
    @Singleton
    fun provideClipboardManager(context: Context): ClipboardManager{
        return context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
    }

    @Provides
    @Singleton
    fun provideTextRecognitionViewModel(
        context: Context,
        recognizer: TextRecognizer,
        clipboardManager: ClipboardManager
    ): MainRepo {
        return MainRepoImplementation(
            context = context,
            recognizer = recognizer,
            clipboardManager = clipboardManager
        )
    }


}