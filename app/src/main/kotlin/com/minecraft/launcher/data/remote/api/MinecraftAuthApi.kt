package com.minecraft.launcher.data.remote.api

import com.minecraft.launcher.data.remote.dto.AuthResponseDto
import com.minecraft.launcher.data.remote.dto.MinecraftProfileDto
import com.minecraft.launcher.data.remote.dto.RefreshTokenRequestDto
import com.minecraft.launcher.data.remote.dto.RefreshTokenResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface MinecraftAuthApi {
    @POST("authenticate")
    suspend fun authenticate(
        @Body request: AuthRequest
    ): AuthResponseDto

    @POST("refresh")
    suspend fun refreshToken(
        @Body request: RefreshTokenRequestDto
    ): RefreshTokenResponseDto

    @GET("profile/minecraft")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): MinecraftProfileDto

    @POST("invalidate")
    suspend fun invalidate(
        @Body request: InvalidateRequest
    ): Unit

    data class AuthRequest(
        val agent: Agent,
        val username: String,
        val password: String,
        val requestUser: Boolean = true
    ) {
        data class Agent(
            val name: String = "Launcher",
            val version: Int = 21
        )
    }

    data class InvalidateRequest(
        val accessToken: String,
        val clientToken: String
    )
}
