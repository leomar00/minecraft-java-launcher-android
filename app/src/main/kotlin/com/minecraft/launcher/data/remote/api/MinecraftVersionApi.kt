package com.minecraft.launcher.data.remote.api

import com.minecraft.launcher.data.remote.dto.VersionManifestDto
import retrofit2.http.GET
import retrofit2.http.Path

interface MinecraftVersionApi {
    @GET("version_manifest_v2.json")
    suspend fun getVersionManifest(): VersionManifestDto

    @GET("versions/{versionId}/{versionId}.json")
    suspend fun getVersionDetails(
        @Path("versionId") versionId: String
    ): VersionDetailsDto

    @GET("versions/{versionId}/{versionId}.jar")
    suspend fun downloadVersion(
        @Path("versionId") versionId: String
    ): okhttp3.ResponseBody
}

data class VersionDetailsDto(
    val id: String,
    val type: String,
    val downloads: DownloadsDto,
    val javaVersion: JavaVersionDto?
) {
    data class DownloadsDto(
        val client: FileDto,
        val server: FileDto,
        val windows_server: FileDto?
    ) {
        data class FileDto(
            val sha1: String,
            val size: Long,
            val url: String
        )
    }

    data class JavaVersionDto(
        val majorVersion: Int
    )
}
