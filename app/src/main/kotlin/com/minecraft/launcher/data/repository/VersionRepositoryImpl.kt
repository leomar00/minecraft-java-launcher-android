package com.minecraft.launcher.data.repository

import com.minecraft.launcher.data.remote.api.MinecraftVersionApi
import com.minecraft.launcher.domain.model.MinecraftVersion
import com.minecraft.launcher.domain.model.Result
import com.minecraft.launcher.domain.model.VersionManifest
import com.minecraft.launcher.domain.repository.VersionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class VersionRepositoryImpl(
    private val api: MinecraftVersionApi,
    private val gameDirectory: File
) : VersionRepository {

    override suspend fun getVersionManifest(): Result<VersionManifest> =
        withContext(Dispatchers.IO) {
            try {
                val dto = api.getVersionManifest()
                val versions = dto.versions.map {
                    MinecraftVersion(
                        id = it.id,
                        name = it.id,
                        type = MinecraftVersion.VersionType.valueOf(it.type.uppercase()),
                        releaseTime = it.releaseTime.toLongOrNull() ?: 0L,
                        url = it.url,
                        sha1 = it.sha1,
                        isInstalled = isVersionInstalled(it.id)
                    )
                }
                val manifest = VersionManifest(
                    versions = versions,
                    latest = VersionManifest.LatestVersion(
                        release = dto.latest.release,
                        snapshot = dto.latest.snapshot
                    )
                )
                Result.Success(manifest)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun getVersion(versionId: String): Result<MinecraftVersion> =
        withContext(Dispatchers.IO) {
            try {
                // TODO: Fetch specific version details
                Result.Error(NotImplementedError("Get version details not yet implemented"))
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun getInstalledVersions(): Result<List<MinecraftVersion>> =
        withContext(Dispatchers.IO) {
            try {
                val versionsDir = File(gameDirectory, "versions")
                if (!versionsDir.exists()) {
                    return@withContext Result.Success(emptyList())
                }
                val versions = versionsDir.listFiles()?.mapNotNull { file ->
                    if (file.isDirectory) {
                        MinecraftVersion(
                            id = file.name,
                            name = file.name,
                            type = MinecraftVersion.VersionType.RELEASE,
                            releaseTime = file.lastModified(),
                            url = "",
                            sha1 = "",
                            isInstalled = true,
                            installPath = file.absolutePath
                        )
                    } else null
                } ?: emptyList()
                Result.Success(versions)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun downloadVersion(
        version: MinecraftVersion,
        progress: (Float) -> Unit
    ): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                // TODO: Implement version download with progress
                Result.Error(NotImplementedError("Version download not yet implemented"))
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun installVersion(version: MinecraftVersion): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                // TODO: Implement version installation
                Result.Error(NotImplementedError("Version installation not yet implemented"))
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun uninstallVersion(versionId: String): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                val versionDir = File(gameDirectory, "versions/$versionId")
                if (versionDir.exists()) {
                    versionDir.deleteRecursively()
                    Result.Success(true)
                } else {
                    Result.Error(NoSuchFileException("Version not found"))
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun isVersionInstalled(versionId: String): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                val versionDir = File(gameDirectory, "versions/$versionId")
                Result.Success(versionDir.exists())
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun getVersionSize(versionId: String): Result<Long> =
        withContext(Dispatchers.IO) {
            try {
                val versionDir = File(gameDirectory, "versions/$versionId")
                if (versionDir.exists()) {
                    val size = versionDir.walkTopDown().sumOf { it.length() }
                    Result.Success(size)
                } else {
                    Result.Error(NoSuchFileException("Version not found"))
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    private fun isVersionInstalled(versionId: String): Boolean {
        return File(gameDirectory, "versions/$versionId").exists()
    }
}
