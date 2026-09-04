package com.minecraft.launcher.domain.model

data class GameProfile(
    val id: String,
    val name: String,
    val accountId: String,
    val version: MinecraftVersion,
    val javaPath: String? = null,
    val allocatedMemory: Int = 2048, // MB
    val vmArguments: String = "-XX:+UnlockExperimentalVMOptions -XX:+UseG1GC",
    val gameArguments: String = "",
    val gamePath: String,
    val lastPlayedAt: Long? = null,
    val totalPlaytime: Long = 0, // milliseconds
    val createdAt: Long = System.currentTimeMillis()
) {
    val allocatedMemoryInGB: Float
        get() = allocatedMemory / 1024f

    val playtimeInHours: Float
        get() = totalPlaytime / (1000f * 60f * 60f)

    fun getJavaArguments(): List<String> {
        return listOf(
            "-Xmx${allocatedMemory}M",
            "-Xms${allocatedMemory / 2}M"
        ) + vmArguments.split(" ").filter { it.isNotBlank() }
    }
}
