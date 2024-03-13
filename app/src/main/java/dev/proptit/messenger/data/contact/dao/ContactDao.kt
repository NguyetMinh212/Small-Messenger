package dev.proptit.messenger.data.contact.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.proptit.messenger.data.contact.entity.ContactEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Query("SELECT * FROM contact")
    fun getAllContacts(): Flow<List<ContactEntity>>

    @Query("SELECT * FROM contact WHERE id = :id")
    suspend fun getContactById(id: Int): ContactEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addContact(contact: ContactEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllContacts(contacts: List<ContactEntity>)

    @Query("SELECT * FROM contact WHERE id IN (SELECT idSendContact FROM message WHERE idSendContact != :userId UNION SELECT idReceiveContact FROM message WHERE idReceiveContact != :userId)")
    fun getContactsFromMessages(userId: Int): Flow<List<ContactEntity>>

}