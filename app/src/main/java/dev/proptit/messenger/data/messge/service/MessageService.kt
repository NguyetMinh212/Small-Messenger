package dev.proptit.messenger.data.messge.service

import dev.proptit.messenger.data.messge.dto.MessageInputDto
import dev.proptit.messenger.data.messge.entity.MessageEntity
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MessageService {
    @POST("api/message/create")
    fun createNewMessage(@Body request: MessageInputDto): Call<Int>

    @GET("api/message/{id}")
    fun getMessageFromContactId(@Path("id") contactId: Int): Call<List<MessageEntity>>
}