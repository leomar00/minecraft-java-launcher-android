package com.minecraft.launcher.domain.usecase

import com.minecraft.launcher.domain.model.GameProfile
import com.minecraft.launcher.domain.model.Result
import com.minecraft.launcher.domain.repository.GameRepository

class GameUseCase(private val gameRepository: GameRepository) {
    suspend fun getProfiles(): Result<List<GameProfile>> {
        return gameRepository.getProfiles()
    }

    suspend fun createProfile(profile: GameProfile): Result<Boolean> {
        if (profile.name.isBlank()) {
            return Result.Error(IllegalArgumentException("Profile name cannot be empty"))
        }
        if (profile.gamePath.isBlank()) {
            return Result.Error(IllegalArgumentException("Game path cannot be empty"))
        }
        return gameRepository.createProfile(profile)
    }

    suspend fun updateProfile(profile: GameProfile): Result<Boolean> {
        return gameRepository.updateProfile(profile)
    }

    suspend fun deleteProfile(profileId: String): Result<Boolean> {
        if (profileId.isBlank()) {
            return Result.Error(IllegalArgumentException("Profile ID cannot be empty"))
        }
        return gameRepository.deleteProfile(profileId)
    }

    suspend fun launchGame(profileId: String): Result<Boolean> {
        if (profileId.isBlank()) {
            return Result.Error(IllegalArgumentException("Profile ID cannot be empty"))
        }
        return gameRepository.launchGame(profileId) { log ->
            // Handle game logs
        }
    }
}
