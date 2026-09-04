package com.minecraft.launcher.domain.repository

import com.minecraft.launcher.domain.model.GameProfile
import com.minecraft.launcher.domain.model.Result

interface GameRepository {
    suspend fun getProfiles(): Result<List<GameProfile>>
    suspend fun getProfile(profileId: String): Result<GameProfile>
    suspend fun createProfile(profile: GameProfile): Result<Boolean>
    suspend fun updateProfile(profile: GameProfile): Result<Boolean>
    suspend fun deleteProfile(profileId: String): Result<Boolean>
    suspend fun launchGame(profileId: String, onLog: (String) -> Unit): Result<Boolean>
    suspend fun stopGame(profileId: String): Result<Boolean>
    suspend fun getGameLogs(profileId: String): Result<String>
}
