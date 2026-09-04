package com.minecraft.launcher.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MicrosoftTokenResponseDto(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String?,
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("expires_in")
    val expiresIn: Long,
    @SerializedName("scope")
    val scope: String?
)
