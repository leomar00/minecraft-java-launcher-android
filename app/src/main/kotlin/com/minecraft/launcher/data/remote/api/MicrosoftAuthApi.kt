package com.minecraft.launcher.data.remote.api

import com.minecraft.launcher.data.remote.dto.MicrosoftTokenResponseDto
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface MicrosoftAuthApi {
    @POST("token")
    @FormUrlEncoded
    suspend fun getToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String,
        @Field("grant_type") grantType: String = "authorization_code",
        @Field("redirect_uri") redirectUri: String,
        @Field("scope") scope: String = "XboxLive.signin offline_access"
    ): MicrosoftTokenResponseDto

    @POST("token")
    @FormUrlEncoded
    suspend fun refreshToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("refresh_token") refreshToken: String,
        @Field("grant_type") grantType: String = "refresh_token",
        @Field("scope") scope: String = "XboxLive.signin offline_access"
    ): MicrosoftTokenResponseDto
}

data class MicrosoftTokenResponseDto(
    val access_token: String,
    val refresh_token: String?,
    val token_type: String,
    val expires_in: Long
)
