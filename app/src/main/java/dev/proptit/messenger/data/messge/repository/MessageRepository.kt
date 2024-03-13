package dev.proptit.messenger.data.messge.repository

import android.util.Log
import dev.proptit.messenger.MainApplication
import dev.proptit.messenger.data.messge.dao.MessageDao
import dev.proptit.messenger.data.messge.entity.MessageEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class MessageRepository() {

    fun getAllMessages() = MainApplication.getInstance().database.messageDao().getAllMessages()


    fun getAllMessagesByIdReceiveContact(id: Int) =
        MainApplication.getInstance().database.messageDao().getAllMessagesByIdReceiveContact(id)

    fun getAllMessagesByIdSendContact(id: Int) =
        MainApplication.getInstance().database.messageDao().getAllMessagesByIdSendContact(id)

    fun getAllMessagesByContactId(id: Int) : Flow<List<MessageEntity>> {
        Log.d("MainViewModel", "getAllMessagesByContactId: $id")
        return MainApplication.getInstance().database.messageDao().getAllMessagesByContactId(id)
    }

    suspend fun addAllMessages(messages: List<MessageEntity>){
        return withContext(Dispatchers.IO) {
            MainApplication.getInstance().database.messageDao().addAllMessages(messages)
        }

    }
}