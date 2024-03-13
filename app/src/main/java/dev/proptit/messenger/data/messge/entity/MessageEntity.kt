package dev.proptit.messenger.data.messge.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "message")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val message: String,
    val idSendContact: Int,
    val idReceiveContact: Int,
    val time: Long = System.currentTimeMillis()
)