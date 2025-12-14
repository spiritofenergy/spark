package com.kodex.spark

import android.app.Application
import androidx.collection.arrayMapOf
import com.kodex.spark.ui.room.mainModuleRoom
import com.yandex.mobile.ads.common.MobileAds
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

//import com.yandex.moduleads.common. MobileAds

@HiltAndroidApp
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@App)
            modules(mainModuleRoom)
        }
    }


    }