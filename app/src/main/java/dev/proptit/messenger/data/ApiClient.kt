package dev.proptit.messenger.data

import dev.proptit.messenger.data.contact.service.ContactService
import dev.proptit.messenger.data.messge.service.MessageService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ApiClient {
    private const val BASE_URL = "http://34.124.219.119:8080/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val contactService: ContactService by lazy {
        retrofit.create(ContactService::class.java)
    }

    val messageService: MessageService by lazy {
        retrofit.create(MessageService::class.java)
    }
}