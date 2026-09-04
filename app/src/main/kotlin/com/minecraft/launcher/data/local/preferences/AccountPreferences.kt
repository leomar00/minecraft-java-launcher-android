package com.minecraft.launcher.data.local.preferences

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.minecraft.launcher.domain.model.MinecraftAccount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AccountPreferences(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        PREFS_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    suspend fun saveCurrentAccount(account: MinecraftAccount) = withContext(Dispatchers.IO) {
        prefs.edit().apply {
            putString(KEY_CURRENT_ACCOUNT_ID, account.id)
            putString(KEY_CURRENT_ACCOUNT_USERNAME, account.username)
            putString(KEY_CURRENT_ACCOUNT_UUID, account.uuid)
            putString(KEY_CURRENT_ACCOUNT_ACCESS_TOKEN, account.accessToken)
            putString(KEY_CURRENT_ACCOUNT_REFRESH_TOKEN, account.refreshToken)
            putString(KEY_CURRENT_ACCOUNT_EMAIL, account.email)
            putString(KEY_CURRENT_ACCOUNT_PROFILE_NAME, account.profileName)
            putLong(KEY_CURRENT_ACCOUNT_CREATED_AT, account.createdAt)
            putBoolean(KEY_CURRENT_ACCOUNT_IS_OFFLINE, account.isOfflineMode)
            apply()
        }
    }

    suspend fun getCurrentAccount(): MinecraftAccount? = withContext(Dispatchers.IO) {
        val id = prefs.getString(KEY_CURRENT_ACCOUNT_ID, null) ?: return@withContext null
        val username = prefs.getString(KEY_CURRENT_ACCOUNT_USERNAME, "") ?: return@withContext null
        val uuid = prefs.getString(KEY_CURRENT_ACCOUNT_UUID, "") ?: return@withContext null
        val accessToken = prefs.getString(KEY_CURRENT_ACCOUNT_ACCESS_TOKEN, "") ?: return@withContext null
        val refreshToken = prefs.getString(KEY_CURRENT_ACCOUNT_REFRESH_TOKEN, null)
        val email = prefs.getString(KEY_CURRENT_ACCOUNT_EMAIL, null)
        val profileName = prefs.getString(KEY_CURRENT_ACCOUNT_PROFILE_NAME, "") ?: return@withContext null
        val createdAt = prefs.getLong(KEY_CURRENT_ACCOUNT_CREATED_AT, 0L)
        val isOffline = prefs.getBoolean(KEY_CURRENT_ACCOUNT_IS_OFFLINE, false)

        MinecraftAccount(
            id = id,
            username = username,
            uuid = uuid,
            accessToken = accessToken,
            refreshToken = refreshToken,
            email = email,
            profileName = profileName,
            createdAt = createdAt,
            isOfflineMode = isOffline
        )
    }

    suspend fun clearCurrentAccount() = withContext(Dispatchers.IO) {
        prefs.edit().clear().apply()
    }

    suspend fun saveClientToken(clientToken: String) = withContext(Dispatchers.IO) {
        prefs.edit().putString(KEY_CLIENT_TOKEN, clientToken).apply()
    }

    suspend fun getClientToken(): String? = withContext(Dispatchers.IO) {
        prefs.getString(KEY_CLIENT_TOKEN, null)
    }

    companion object {
        private const val PREFS_NAME = "minecraft_launcher_accounts"
        private const val KEY_CURRENT_ACCOUNT_ID = "current_account_id"
        private const val KEY_CURRENT_ACCOUNT_USERNAME = "current_account_username"
        private const val KEY_CURRENT_ACCOUNT_UUID = "current_account_uuid"
        private const val KEY_CURRENT_ACCOUNT_ACCESS_TOKEN = "current_account_access_token"
        private const val KEY_CURRENT_ACCOUNT_REFRESH_TOKEN = "current_account_refresh_token"
        private const val KEY_CURRENT_ACCOUNT_EMAIL = "current_account_email"
        private const val KEY_CURRENT_ACCOUNT_PROFILE_NAME = "current_account_profile_name"
        private const val KEY_CURRENT_ACCOUNT_CREATED_AT = "current_account_created_at"
        private const val KEY_CURRENT_ACCOUNT_IS_OFFLINE = "current_account_is_offline"
        private const val KEY_CLIENT_TOKEN = "client_token"
    }
}
