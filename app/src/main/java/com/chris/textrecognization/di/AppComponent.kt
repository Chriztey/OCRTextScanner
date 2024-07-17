package com.chris.textrecognization.di

import com.chris.textrecognization.BaseApplication
import com.chris.textrecognization.viewmodel.TextRecognitionViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(application: BaseApplication)
    fun inject(viewModel: TextRecognitionViewModel)
}