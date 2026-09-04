package com.minecraft.launcher.di

import android.content.Context
import androidx.room.Room
import com.minecraft.launcher.data.local.database.MinecraftLauncherDatabase
import com.minecraft.launcher.data.remote.api.MinecraftVersionApi
import com.minecraft.launcher.data.repository.AccountRepositoryImpl
import com.minecraft.launcher.data.repository.GameRepositoryImpl
import com.minecraft.launcher.data.repository.VersionRepositoryImpl
import com.minecraft.launcher.domain.repository.AccountRepository
import com.minecraft.launcher.domain.repository.GameRepository
import com.minecraft.launcher.domain.repository.VersionRepository
import com.minecraft.launcher.domain.usecase.AuthUseCase
import com.minecraft.launcher.domain.usecase.GameUseCase
import com.minecraft.launcher.domain.usecase.VersionUseCase
import com.minecraft.launcher.presentation.viewmodel.AuthViewModel
import com.minecraft.launcher.presentation.viewmodel.HomeViewModel
import com.minecraft.launcher.presentation.viewmodel.SettingsViewModel
import com.minecraft.launcher.presentation.viewmodel.VersionsViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

val appModule = module {
    // Database
    single {
        Room.databaseBuilder(
            androidContext(),
            MinecraftLauncherDatabase::class.java,
            MinecraftLauncherDatabase.DATABASE_NAME
        ).build()
    }

    single { get<MinecraftLauncherDatabase>().accountDao() }
    single { get<MinecraftLauncherDatabase>().gameProfileDao() }

    // Network
    single {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BASIC
                }
            )
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://launcher.mojang.com/v1/objects/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MinecraftVersionApi::class.java)
    }

    // Game Directory
    single {
        File(androidContext().getExternalFilesDir(null), ".minecraft")
    }

    // Repositories
    single<AccountRepository> { AccountRepositoryImpl(get()) }
    single<VersionRepository> { VersionRepositoryImpl(get(), get()) }
    single<GameRepository> { GameRepositoryImpl(get()) }

    // Use Cases
    single { AuthUseCase(get()) }
    single { VersionUseCase(get()) }
    single { GameUseCase(get()) }

    // ViewModels
    viewModel { AuthViewModel(get()) }
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { VersionsViewModel(get()) }
    viewModel { SettingsViewModel() }
}
