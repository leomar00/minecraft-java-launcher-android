package com.minecraft.launcher

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import com.minecraft.launcher.di.appModule

class MinecraftLauncherApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeDependencyInjection()
    }

    private fun initializeDependencyInjection() {
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MinecraftLauncherApp)
            modules(appModule)
        }
    }
}
