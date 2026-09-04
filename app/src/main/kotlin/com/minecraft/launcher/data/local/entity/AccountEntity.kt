package com.minecraft.launcher.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.minecraft.launcher.domain.model.MinecraftAccount

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey
    val id: String,
    val username: String,
    val uuid: String,
    val accessToken: String,
    val refreshToken: String?,
    val email: String?,
    val profileName: String,
    val skinUrl: String?,
    val capeUrl: String?,
    val createdAt: Long,
    val isOfflineMode: Boolean
) {
    fun toDomain(): MinecraftAccount {
        return MinecraftAccount(
            id = id,
            username = username,
            uuid = uuid,
            accessToken = accessToken,
            refreshToken = refreshToken,
            email = email,
            profileName = profileName,
            skinUrl = skinUrl,
            capeUrl = capeUrl,
            createdAt = createdAt,
            isOfflineMode = isOfflineMode
        )
    }

    companion object {
        fun fromDomain(account: MinecraftAccount): AccountEntity {
            return AccountEntity(
                id = account.id,
                username = account.username,
                uuid = account.uuid,
                accessToken = account.accessToken,
                refreshToken = account.refreshToken,
                email = account.email,
                profileName = account.profileName,
                skinUrl = account.skinUrl,
                capeUrl = account.capeUrl,
                createdAt = account.createdAt,
                isOfflineMode = account.isOfflineMode
            )
        }
    }
}
