package dev.proptit.messenger.data.messge.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.proptit.messenger.data.messge.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM message")
    fun getAllMessages(): Flow<List<MessageEntity>>

    @Query("SELECT * FROM message WHERE idReceiveContact = :id")
    fun getAllMessagesByIdReceiveContact(id: Int): Flow<List<MessageEntity>>

    @Query("SELECT * FROM message WHERE idSendContact = :id")
    fun getAllMessagesByIdSendContact(id: Int): Flow<List<MessageEntity>>

    @Query("SELECT * FROM message WHERE idReceiveContact = :id or idSendContact = :id")
    fun getAllMessagesByContactId(id: Int): Flow<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllMessages(messages: List<MessageEntity>)
}