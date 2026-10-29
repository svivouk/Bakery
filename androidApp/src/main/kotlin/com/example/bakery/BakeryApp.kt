package com.example.bakery

import android.app.Application
import com.example.bakery.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BakeryApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin { androidContext(this@BakeryApp); modules(appModule) }
    }
}