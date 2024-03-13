package dev.proptit.messenger.data.contact.repository

import com.orhanobut.hawk.Hawk
import dev.proptit.messenger.MainApplication
import dev.proptit.messenger.data.ApiClient
import dev.proptit.messenger.data.contact.dao.ContactDao
import dev.proptit.messenger.data.contact.entity.ContactEntity
import dev.proptit.messenger.setup.key.Keys
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ContactRepository() {

    fun getAllContacts(): Flow<List<ContactEntity>> {
        return MainApplication.getInstance().database.contactDao().getAllContacts()
    }

    suspend fun getContactById(id: Int): ContactEntity {
        return withContext(Dispatchers.IO) {
            MainApplication.getInstance().database.contactDao().getContactById(id)
        }
    }

    suspend fun addNewContact(contacts: List<ContactEntity>) {
        return withContext(Dispatchers.IO) {
            MainApplication.getInstance().database.contactDao().addAllContacts(contacts)
        }
    }

    fun getContactsFromMessages(userId: Int): Flow<List<ContactEntity>>{
        return MainApplication.getInstance().database.contactDao().getContactsFromMessages(userId)
    }
}