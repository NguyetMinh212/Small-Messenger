package dev.proptit.messenger.data.messge.dto

import com.google.gson.annotations.SerializedName

data class MessageInputDto(
    @SerializedName("message")
    val message: String,
    @SerializedName("idReceiveContact")
    val idReceiveContact: Int,
    @SerializedName("idSendContact")
    val idSendContact: Int,
    @SerializedName("time")
    val time: Long
)
