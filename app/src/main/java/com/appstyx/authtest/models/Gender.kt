package com.appstyx.authtest.models


import com.google.gson.annotations.SerializedName

data class Gender(
    @SerializedName("data")
    val data: Data?
) {
    data class Data(
        @SerializedName("genders")
        val genders: List<Gender>?
    ) {
        data class Gender(
            @SerializedName("id")
            val id: String?,
            @SerializedName("name")
            val name: String?
        )
    }
}