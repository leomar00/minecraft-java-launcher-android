package com.minecraft.launcher.domain.usecase

import com.minecraft.launcher.domain.model.MinecraftVersion
import com.minecraft.launcher.domain.model.Result
import com.minecraft.launcher.domain.model.VersionManifest
import com.minecraft.launcher.domain.repository.VersionRepository

class VersionUseCase(private val versionRepository: VersionRepository) {
    suspend fun getAvailableVersions(): Result<VersionManifest> {
        return versionRepository.getVersionManifest()
    }

    suspend fun getInstalledVersions(): Result<List<MinecraftVersion>> {
        return versionRepository.getInstalledVersions()
    }

    suspend fun installVersion(
        version: MinecraftVersion,
        onProgress: (Float) -> Unit
    ): Result<Boolean> {
        if (version.id.isBlank()) {
            return Result.Error(IllegalArgumentException("Version ID cannot be empty"))
        }
        return try {
            versionRepository.downloadVersion(version, onProgress)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun uninstallVersion(versionId: String): Result<Boolean> {
        if (versionId.isBlank()) {
            return Result.Error(IllegalArgumentException("Version ID cannot be empty"))
        }
        return versionRepository.uninstallVersion(versionId)
    }
}
