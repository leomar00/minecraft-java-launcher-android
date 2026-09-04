package com.minecraft.launcher.domain.usecase

import com.minecraft.launcher.domain.model.MinecraftAccount
import com.minecraft.launcher.domain.model.Result
import com.minecraft.launcher.domain.repository.AccountRepository

class AuthUseCase(private val accountRepository: AccountRepository) {
    suspend fun loginOffline(username: String): Result<MinecraftAccount> {
        if (username.isBlank()) {
            return Result.Error(IllegalArgumentException("Username cannot be empty"))
        }
        return accountRepository.loginOffline(username)
    }

    suspend fun loginWithUsername(username: String, password: String): Result<MinecraftAccount> {
        if (username.isBlank() || password.isBlank()) {
            return Result.Error(IllegalArgumentException("Username and password cannot be empty"))
        }
        return accountRepository.loginWithUsername(username, password)
    }

    suspend fun loginWithMicrosoft(code: String): Result<MinecraftAccount> {
        if (code.isBlank()) {
            return Result.Error(IllegalArgumentException("Auth code cannot be empty"))
        }
        return accountRepository.loginWithMicrosoft(code)
    }

    suspend fun logout(accountId: String): Result<Boolean> {
        if (accountId.isBlank()) {
            return Result.Error(IllegalArgumentException("Account ID cannot be empty"))
        }
        return accountRepository.logout(accountId)
    }

    suspend fun getAccounts(): Result<List<MinecraftAccount>> {
        return accountRepository.getAccounts()
    }

    suspend fun refreshToken(accountId: String): Result<MinecraftAccount> {
        if (accountId.isBlank()) {
            return Result.Error(IllegalArgumentException("Account ID cannot be empty"))
        }
        return accountRepository.refreshToken(accountId)
    }
}
