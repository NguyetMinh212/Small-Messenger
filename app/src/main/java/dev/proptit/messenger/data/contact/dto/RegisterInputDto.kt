package dev.proptit.messenger.data.contact.dto

import com.google.gson.annotations.SerializedName

data class RegisterInputDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("avatar")
    val avatar: String = "",
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String
)
