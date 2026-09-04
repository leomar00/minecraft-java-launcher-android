package com.minecraft.launcher.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.minecraft.launcher.domain.model.GameProfile
import com.minecraft.launcher.domain.model.MinecraftVersion

@Entity(tableName = "game_profiles")
data class GameProfileEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val accountId: String,
    val versionId: String,
    val versionName: String,
    val javaPath: String?,
    val allocatedMemory: Int,
    val vmArguments: String,
    val gameArguments: String,
    val gamePath: String,
    val lastPlayedAt: Long?,
    val totalPlaytime: Long,
    val createdAt: Long
) {
    fun toDomain(version: MinecraftVersion): GameProfile {
        return GameProfile(
            id = id,
            name = name,
            accountId = accountId,
            version = version,
            javaPath = javaPath,
            allocatedMemory = allocatedMemory,
            vmArguments = vmArguments,
            gameArguments = gameArguments,
            gamePath = gamePath,
            lastPlayedAt = lastPlayedAt,
            totalPlaytime = totalPlaytime,
            createdAt = createdAt
        )
    }

    companion object {
        fun fromDomain(profile: GameProfile): GameProfileEntity {
            return GameProfileEntity(
                id = profile.id,
                name = profile.name,
                accountId = profile.accountId,
                versionId = profile.version.id,
                versionName = profile.version.name,
                javaPath = profile.javaPath,
                allocatedMemory = profile.allocatedMemory,
                vmArguments = profile.vmArguments,
                gameArguments = profile.gameArguments,
                gamePath = profile.gamePath,
                lastPlayedAt = profile.lastPlayedAt,
                totalPlaytime = profile.totalPlaytime,
                createdAt = profile.createdAt
            )
        }
    }
}
