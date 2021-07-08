package com.appstyx.authtest.models


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("data")
    val `data`: Data
) {
    data class Data(
        @SerializedName("token")
        val token: String,
        @SerializedName("refreshToken")
        val refreshToken: String
    )
}