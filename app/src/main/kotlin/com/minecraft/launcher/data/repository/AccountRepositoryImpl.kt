package com.minecraft.launcher.data.repository

import com.minecraft.launcher.data.local.dao.AccountDao
import com.minecraft.launcher.data.local.entity.AccountEntity
import com.minecraft.launcher.data.local.preferences.AccountPreferences
import com.minecraft.launcher.data.remote.api.MinecraftAuthApi
import com.minecraft.launcher.domain.model.MinecraftAccount
import com.minecraft.launcher.domain.model.Result
import com.minecraft.launcher.domain.repository.AccountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AccountRepositoryImpl(
    private val accountDao: AccountDao,
    private val accountPreferences: AccountPreferences,
    private val authApi: MinecraftAuthApi
) : AccountRepository {

    override suspend fun loginWithMicrosoft(code: String): Result<MinecraftAccount> =
        withContext(Dispatchers.IO) {
            try {
                // TODO: Implement Microsoft OAuth flow
                // 1. Exchange code for Microsoft access token
                // 2. Use token to authenticate with Minecraft
                // 3. Get Minecraft profile
                Result.Error(NotImplementedError("Microsoft login not yet implemented"))
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun loginWithUsername(username: String, password: String): Result<MinecraftAccount> =
        withContext(Dispatchers.IO) {
            try {
                val clientToken = generateClientToken()
                val request = MinecraftAuthApi.AuthRequest(
                    agent = MinecraftAuthApi.AuthRequest.Agent(),
                    username = username,
                    password = password
                )

                val response = authApi.authenticate(request)
                val account = response.toDomain()

                // Save account to database
                val entity = AccountEntity.fromDomain(account)
                accountDao.insert(entity)

                // Save as current account in preferences
                accountPreferences.saveCurrentAccount(account)
                accountPreferences.saveClientToken(clientToken)

                Result.Success(account)
            } catch (e: Exception) {
                Result.Error(e, "Failed to authenticate: ${e.message}")
            }
        }

    override suspend fun loginOffline(username: String): Result<MinecraftAccount> =
        withContext(Dispatchers.IO) {
            try {
                val account = MinecraftAccount.createOfflineAccount(username)
                val entity = AccountEntity.fromDomain(account)
                accountDao.insert(entity)

                // Save as current account in preferences
                accountPreferences.saveCurrentAccount(account)

                Result.Success(account)
            } catch (e: Exception) {
                Result.Error(e, "Failed to login offline: ${e.message}")
            }
        }

    override suspend fun getAccounts(): Result<List<MinecraftAccount>> =
        withContext(Dispatchers.IO) {
            try {
                val accounts = accountDao.getAll().map { it.toDomain() }
                Result.Success(accounts)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun getAccount(id: String): Result<MinecraftAccount> =
        withContext(Dispatchers.IO) {
            try {
                val entity = accountDao.getById(id)
                if (entity != null) {
                    Result.Success(entity.toDomain())
                } else {
                    Result.Error(NoSuchElementException("Account not found"))
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun saveAccount(account: MinecraftAccount): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                val entity = AccountEntity.fromDomain(account)
                val existing = accountDao.getById(account.id)
                if (existing != null) {
                    accountDao.update(entity)
                } else {
                    accountDao.insert(entity)
                }
                Result.Success(true)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun deleteAccount(id: String): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                accountDao.deleteById(id)
                // Clear from preferences if it was the current account
                val current = accountPreferences.getCurrentAccount()
                if (current?.id == id) {
                    accountPreferences.clearCurrentAccount()
                }
                Result.Success(true)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun refreshToken(accountId: String): Result<MinecraftAccount> =
        withContext(Dispatchers.IO) {
            try {
                val account = accountDao.getById(accountId)?.toDomain()
                    ?: return@withContext Result.Error(NoSuchElementException("Account not found"))

                if (account.refreshToken.isNullOrBlank()) {
                    return@withContext Result.Error(IllegalStateException("No refresh token available"))
                }

                val clientToken = accountPreferences.getClientToken()
                    ?: return@withContext Result.Error(IllegalStateException("No client token found"))

                val request = com.minecraft.launcher.data.remote.dto.RefreshTokenRequestDto(
                    accessToken = account.accessToken,
                    clientToken = clientToken
                )

                val response = authApi.refreshToken(request)
                val updatedAccount = response.toDomain()

                // Update in database
                val entity = AccountEntity.fromDomain(updatedAccount)
                accountDao.update(entity)

                // Update current account in preferences if it matches
                if (accountPreferences.getCurrentAccount()?.id == accountId) {
                    accountPreferences.saveCurrentAccount(updatedAccount)
                }

                Result.Success(updatedAccount)
            } catch (e: Exception) {
                Result.Error(e, "Failed to refresh token: ${e.message}")
            }
        }

    override suspend fun logout(accountId: String): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                val account = accountDao.getById(accountId)?.toDomain()
                if (account != null && !account.isOfflineMode) {
                    try {
                        val clientToken = accountPreferences.getClientToken()
                        if (clientToken != null) {
                            val request = MinecraftAuthApi.InvalidateRequest(
                                accessToken = account.accessToken,
                                clientToken = clientToken
                            )
                            authApi.invalidate(request)
                        }
                    } catch (e: Exception) {
                        // Invalidation failed, but continue with logout
                    }
                }

                accountDao.deleteById(accountId)

                // Clear from preferences if it was the current account
                if (accountPreferences.getCurrentAccount()?.id == accountId) {
                    accountPreferences.clearCurrentAccount()
                }

                Result.Success(true)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    private fun generateClientToken(): String {
        return java.util.UUID.randomUUID().toString()
    }
}
