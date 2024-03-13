package dev.proptit.messenger.data.contact.service

import dev.proptit.messenger.data.contact.dto.LoginInputDto
import dev.proptit.messenger.data.contact.dto.RegisterInputDto
import dev.proptit.messenger.data.contact.entity.ContactEntity
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ContactService {

    @POST("api/contact/login")
    fun getIdUserByLogin(@Body request: LoginInputDto): Call<Int>

    @POST("api/contact/register")
    fun registerNewAccount(@Body request: RegisterInputDto): Call<Int>

    @GET("api/contact")
    fun getAllContacts(): Call<List<ContactEntity>>

}