package dev.proptit.messenger.data.contact.entity

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
@Entity(tableName = "contact")
data class ContactEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val avatar: String,
): Serializable