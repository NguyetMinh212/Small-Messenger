package dev.proptit.messenger.data.contact.dto

import com.google.gson.annotations.SerializedName

data class LoginInputDto(
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String
)
