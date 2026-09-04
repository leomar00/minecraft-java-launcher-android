package com.minecraft.launcher.domain.model

import java.util.UUID

data class MinecraftAccount(
    val id: String = UUID.randomUUID().toString(),
    val username: String,
    val uuid: String,
    val accessToken: String,
    val refreshToken: String? = null,
    val email: String? = null,
    val profileName: String,
    val skinUrl: String? = null,
    val capeUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val isOfflineMode: Boolean = false
) {
    companion object {
        fun createOfflineAccount(username: String): MinecraftAccount {
            return MinecraftAccount(
                username = username,
                uuid = generateOfflineUUID(username),
                accessToken = "0",
                profileName = username,
                isOfflineMode = true
            )
        }

        private fun generateOfflineUUID(username: String): String {
            // Simplified UUID generation for offline mode
            return "00000000-0000-3000-8000-000000000000".take(18) + username.take(12)
        }
    }
}
