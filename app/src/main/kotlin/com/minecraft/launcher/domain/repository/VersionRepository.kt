package com.minecraft.launcher.domain.repository

import com.minecraft.launcher.domain.model.MinecraftVersion
import com.minecraft.launcher.domain.model.Result
import com.minecraft.launcher.domain.model.VersionManifest

interface VersionRepository {
    suspend fun getVersionManifest(): Result<VersionManifest>
    suspend fun getVersion(versionId: String): Result<MinecraftVersion>
    suspend fun getInstalledVersions(): Result<List<MinecraftVersion>>
    suspend fun downloadVersion(version: MinecraftVersion, progress: (Float) -> Unit): Result<Boolean>
    suspend fun installVersion(version: MinecraftVersion): Result<Boolean>
    suspend fun uninstallVersion(versionId: String): Result<Boolean>
    suspend fun isVersionInstalled(versionId: String): Result<Boolean>
    suspend fun getVersionSize(versionId: String): Result<Long>
}
