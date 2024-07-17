package com.chris.textrecognization

import android.app.Application
import com.chris.textrecognization.di.AppComponent
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication: Application() {
//    lateinit var appComponent: AppComponent

//    override fun onCreate() {
//        super.onCreate()
//
////        appComponent = DaggerAppComponent.builder()
////            .appModule(AppModule())
////            .build()
//
//        appComponent.inject(this)
//    }

}