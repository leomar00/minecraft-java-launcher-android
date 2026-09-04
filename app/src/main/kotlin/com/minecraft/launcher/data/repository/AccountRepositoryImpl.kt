package com.minecraft.launcher.data.repository

import com.minecraft.launcher.data.local.dao.AccountDao
import com.minecraft.launcher.data.local.entity.AccountEntity
import com.minecraft.launcher.domain.model.MinecraftAccount
import com.minecraft.launcher.domain.model.Result
import com.minecraft.launcher.domain.repository.AccountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AccountRepositoryImpl(
    private val accountDao: AccountDao
) : AccountRepository {

    override suspend fun loginWithMicrosoft(code: String): Result<MinecraftAccount> =
        withContext(Dispatchers.IO) {
            try {
                // TODO: Implement Microsoft OAuth flow
                Result.Error(NotImplementedError("Microsoft login not yet implemented"))
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun loginWithUsername(username: String, password: String): Result<MinecraftAccount> =
        withContext(Dispatchers.IO) {
            try {
                // TODO: Implement Minecraft launcher authentication
                Result.Error(NotImplementedError("Username/password login not yet implemented"))
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun loginOffline(username: String): Result<MinecraftAccount> =
        withContext(Dispatchers.IO) {
            try {
                val account = MinecraftAccount.createOfflineAccount(username)
                val entity = AccountEntity.fromDomain(account)
                accountDao.insert(entity)
                Result.Success(account)
            } catch (e: Exception) {
                Result.Error(e)
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
                Result.Success(true)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun refreshToken(accountId: String): Result<MinecraftAccount> =
        withContext(Dispatchers.IO) {
            try {
                // TODO: Implement token refresh logic
                Result.Error(NotImplementedError("Token refresh not yet implemented"))
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun logout(accountId: String): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                accountDao.deleteById(accountId)
                Result.Success(true)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
}
