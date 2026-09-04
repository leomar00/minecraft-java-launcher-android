package com.minecraft.launcher.domain.repository

import com.minecraft.launcher.domain.model.MinecraftAccount
import com.minecraft.launcher.domain.model.Result

interface AccountRepository {
    suspend fun loginWithMicrosoft(code: String): Result<MinecraftAccount>
    suspend fun loginWithUsername(username: String, password: String): Result<MinecraftAccount>
    suspend fun loginOffline(username: String): Result<MinecraftAccount>
    suspend fun getAccounts(): Result<List<MinecraftAccount>>
    suspend fun getAccount(id: String): Result<MinecraftAccount>
    suspend fun saveAccount(account: MinecraftAccount): Result<Boolean>
    suspend fun deleteAccount(id: String): Result<Boolean>
    suspend fun refreshToken(accountId: String): Result<MinecraftAccount>
    suspend fun logout(accountId: String): Result<Boolean>
}
