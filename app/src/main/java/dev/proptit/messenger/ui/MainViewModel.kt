package dev.proptit.messenger.ui

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.DEFAULT_ARGS_KEY
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.orhanobut.hawk.Hawk
import dev.proptit.messenger.data.ApiClient
import dev.proptit.messenger.data.contact.dto.LoginInputDto
import dev.proptit.messenger.data.contact.dto.RegisterInputDto
import dev.proptit.messenger.data.contact.entity.ContactEntity
import dev.proptit.messenger.data.contact.repository.ContactRepository
import dev.proptit.messenger.data.contact.service.ContactService
import dev.proptit.messenger.data.messge.dto.MessageInputDto
import dev.proptit.messenger.data.messge.entity.MessageEntity
import dev.proptit.messenger.data.messge.repository.MessageRepository
import dev.proptit.messenger.data.messge.service.MessageService
import dev.proptit.messenger.setup.key.Keys
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel(
    private val contactRepository: ContactRepository,
    private val messageRepository: MessageRepository
) : ViewModel() {

    private val contactService: ContactService = ApiClient.contactService
    private val messageService: MessageService = ApiClient.messageService

    private var job: Job? = null

    private val _curContactId = MutableStateFlow(1)
    val curContactId = _curContactId.asStateFlow()

    val allMessages by lazy { MutableLiveData<List<MessageEntity>>() }
    val allContacts by lazy { MutableLiveData<List<ContactEntity>>() }
    val allContactsByMessages by lazy { MutableLiveData<List<ContactEntity>>() }
    val curContact by lazy { MutableLiveData<ContactEntity>() }

    init {
        viewModelScope.launch() {
            _curContactId.collect {
                Log.d("MainViewModel", "curContactId: $it")
                curContact.value = contactRepository.getContactById(it)
            }
        }

        viewModelScope.launch() {
            messageRepository.getAllMessages().collect { results ->
                allMessages.value = results
            }
        }

        viewModelScope.launch() {
            contactRepository.getAllContacts().collect { results ->
                allContacts.value = results
            }
        }

        viewModelScope.launch() {
            contactRepository.getContactsFromMessages(Hawk.get(Keys.ID_USER, -1))
                .collect { results ->
                    allContactsByMessages.value = results
                }
        }

    }

    fun getAllMessagesByContactId(): Flow<List<MessageEntity>> {
        return messageRepository.getAllMessagesByContactId(_curContactId.value)
    }

    fun updateCurContactId(id: Int) {
        _curContactId.update { id }
    }

    fun addAllMessages(messages: List<MessageEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            messageRepository.addAllMessages(messages)
        }
    }

    fun addContact(contacts: List<ContactEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            contactRepository.addNewContact(contacts)
        }
    }


    fun createNewMessage(
        message: String,
        idReceiveContact: Int,
        idSendContact: Int,
        time: Long,
        onSuccess: (Int) -> Unit,
        onError: () -> Unit
    ) {
        val request = MessageInputDto(message, idReceiveContact, idSendContact, time)
        val call = messageService.createNewMessage(request)
        call.enqueue(object : retrofit2.Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    val messageId = response.body()
                    if (messageId != null) {
                        onSuccess(messageId)
                    }
                } else{
                    onError()
                    Log.d("MainViewModel", "onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                onError()
                Log.d("MainViewModel", "onFailure: ${t.message}")
            }

        })
    }

    fun startFetchingAllContactsWithUser() {
        // Start a coroutine to fetch users every2 seconds
        job = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                // Fetch users from API
                getAllContactsWithUser {
                    val contacts = it.filter { it.id != Hawk.get(Keys.ID_USER, -1) }
                    Log.d("MainViewModel", "startFetchingAllContactsWithUser: $contacts")
                    addContact(contacts)
                }
                delay(2000)
            }
        }
    }

    fun stopFetchingAllContactsWithUser() {
        job?.cancel()
    }

    fun getAllContactsWithUser(onSuccess: (List<ContactEntity>) -> Unit) {
        val call = contactService.getAllContacts()
        call.enqueue(object : retrofit2.Callback<List<ContactEntity>> {
            override fun onResponse(
                call: Call<List<ContactEntity>>,
                response: Response<List<ContactEntity>>
            ) {
                if (response.isSuccessful) {
                    val responseData = response.body()
                    if (responseData != null) {
                        onSuccess(responseData)
                    }
                }
            }

            override fun onFailure(call: Call<List<ContactEntity>>, t: Throwable) {

            }

        })
    }

    fun startFetchingAllMessagesFromContactId() {
        job = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                getAllMessagesFromContactId {
                    Log.d("MainViewModel", "startFetchingAllMessages: $it")
                    addAllMessages(it)
                }
                delay(2000)
            }
        }
    }

    fun stopFetchingAllMessagesFromContactId() {
        job?.cancel()
    }

    fun getAllMessagesFromContactId(onSuccess: (List<MessageEntity>) -> Unit) {
        val call = messageService.getMessageFromContactId(Hawk.get(Keys.ID_USER, -1))
        call.enqueue(object : retrofit2.Callback<List<MessageEntity>> {
            override fun onResponse(
                call: Call<List<MessageEntity>>,
                response: Response<List<MessageEntity>>
            ) {
                if (response.isSuccessful) {
                    val messagesList = response.body()
                    if (messagesList != null) {
                        onSuccess(messagesList)
                    }
                }
            }

            override fun onFailure(call: Call<List<MessageEntity>>, t: Throwable) {

            }

        })
    }

}

class MainViewModelFactory(
    private val contactRepository: ContactRepository,
    private val messageRepository: MessageRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(contactRepository, messageRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}