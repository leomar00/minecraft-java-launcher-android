package com.minecraft.launcher.domain.model

data class MinecraftVersion(
    val id: String,
    val name: String,
    val type: VersionType,
    val releaseTime: Long,
    val url: String,
    val sha1: String,
    val size: Long = 0,
    val isInstalled: Boolean = false,
    val installPath: String? = null,
    val javaVersion: Int = 8 // Minimum Java version required
) {
    enum class VersionType {
        RELEASE,
        SNAPSHOT,
        OLD_BETA,
        OLD_ALPHA
    }

    val displayName: String
        get() = "$name (${type.name})"

    val sizeInMB: Float
        get() = size / (1024f * 1024f)
}

data class VersionManifest(
    val versions: List<MinecraftVersion>,
    val latest: LatestVersion
) {
    data class LatestVersion(
        val release: String,
        val snapshot: String
    )
}
