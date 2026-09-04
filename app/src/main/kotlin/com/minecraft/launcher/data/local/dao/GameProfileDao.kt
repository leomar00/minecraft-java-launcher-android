package com.minecraft.launcher.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.minecraft.launcher.data.local.entity.GameProfileEntity

@Dao
interface GameProfileDao {
    @Query("SELECT * FROM game_profiles")
    suspend fun getAll(): List<GameProfileEntity>

    @Query("SELECT * FROM game_profiles WHERE id = :id")
    suspend fun getById(id: String): GameProfileEntity?

    @Query("SELECT * FROM game_profiles WHERE accountId = :accountId")
    suspend fun getByAccountId(accountId: String): List<GameProfileEntity>

    @Query("SELECT * FROM game_profiles ORDER BY lastPlayedAt DESC LIMIT 1")
    suspend fun getLastPlayedProfile(): GameProfileEntity?

    @Insert
    suspend fun insert(profile: GameProfileEntity)

    @Update
    suspend fun update(profile: GameProfileEntity)

    @Delete
    suspend fun delete(profile: GameProfileEntity)

    @Query("DELETE FROM game_profiles WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("UPDATE game_profiles SET lastPlayedAt = :timestamp WHERE id = :id")
    suspend fun updateLastPlayed(id: String, timestamp: Long)
}
