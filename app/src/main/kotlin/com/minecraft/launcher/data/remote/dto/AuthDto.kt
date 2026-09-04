package com.minecraft.launcher.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.minecraft.launcher.domain.model.MinecraftAccount
import java.util.UUID

data class AuthResponseDto(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("clientToken")
    val clientToken: String,
    @SerializedName("profile")
    val profile: ProfileDto,
    @SerializedName("user")
    val user: UserDto?
) {
    data class ProfileDto(
        @SerializedName("id")
        val id: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("legacy")
        val legacy: Boolean = false
    )

    data class UserDto(
        @SerializedName("id")
        val id: String,
        @SerializedName("email")
        val email: String?,
        @SerializedName("username")
        val username: String?
    )

    fun toDomain(): MinecraftAccount {
        return MinecraftAccount(
            id = UUID.randomUUID().toString(),
            username = profile.name,
            uuid = profile.id,
            accessToken = accessToken,
            refreshToken = clientToken,
            email = user?.email,
            profileName = profile.name,
            isOfflineMode = false
        )
    }
}

data class MinecraftProfileDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("skins")
    val skins: List<SkinDto>?,
    @SerializedName("capes")
    val capes: List<CapeDto>?
) {
    data class SkinDto(
        @SerializedName("id")
        val id: String,
        @SerializedName("state")
        val state: String,
        @SerializedName("url")
        val url: String,
        @SerializedName("textureKey")
        val textureKey: String?
    )

    data class CapeDto(
        @SerializedName("id")
        val id: String,
        @SerializedName("state")
        val state: String,
        @SerializedName("url")
        val url: String,
        @SerializedName("textureKey")
        val textureKey: String?
    )
}

data class RefreshTokenRequestDto(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("clientToken")
    val clientToken: String,
    @SerializedName("requestUser")
    val requestUser: Boolean = true
)

data class RefreshTokenResponseDto(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("clientToken")
    val clientToken: String,
    @SerializedName("profile")
    val profile: AuthResponseDto.ProfileDto,
    @SerializedName("user")
    val user: AuthResponseDto.UserDto?
) {
    fun toDomain(): MinecraftAccount {
        return MinecraftAccount(
            id = UUID.randomUUID().toString(),
            username = profile.name,
            uuid = profile.id,
            accessToken = accessToken,
            refreshToken = clientToken,
            email = user?.email,
            profileName = profile.name,
            isOfflineMode = false
        )
    }
}
