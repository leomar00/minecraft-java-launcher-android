package com.minecraft.launcher.data.remote.dto

data class VersionManifestDto(
    val versions: List<VersionDto>,
    val latest: LatestDto
) {
    data class VersionDto(
        val id: String,
        val type: String,
        val url: String,
        val time: String,
        val releaseTime: String,
        val sha1: String
    )

    data class LatestDto(
        val release: String,
        val snapshot: String
    )
}
