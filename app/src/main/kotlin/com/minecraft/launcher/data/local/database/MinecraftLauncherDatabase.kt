package com.minecraft.launcher.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.minecraft.launcher.data.local.entity.AccountEntity
import com.minecraft.launcher.data.local.entity.GameProfileEntity
import com.minecraft.launcher.data.local.dao.AccountDao
import com.minecraft.launcher.data.local.dao.GameProfileDao

@Database(
    entities = [
        AccountEntity::class,
        GameProfileEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MinecraftLauncherDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun gameProfileDao(): GameProfileDao

    companion object {
        const val DATABASE_NAME = "minecraft_launcher.db"
    }
}
