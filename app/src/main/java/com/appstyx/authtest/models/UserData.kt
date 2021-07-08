package com.appstyx.authtest.models


import com.google.gson.annotations.SerializedName

data class UserData(
    @SerializedName("data")
    val data: Data
) {
    data class Data(
        @SerializedName("user")
        val user: User
    ) {
        data class User(
            @SerializedName("roles")
            val roles: List<String>,
            @SerializedName("_id")
            val id: String,
            @SerializedName("email")
            val email: String,
            @SerializedName("gender")
            val gender: String,
            @SerializedName("firstName")
            val firstName: String,
            @SerializedName("lastName")
            val lastName: String,
            @SerializedName("avatarURL")
            val avatarURL: String,
            @SerializedName("provider")
            val provider: String,
            @SerializedName("username")
            val username: String,
            @SerializedName("createdAt")
            val createdAt: String,
            @SerializedName("updatedAt")
            val updatedAt: String,
            @SerializedName("__v")
            val v: Int
        )
    }
}