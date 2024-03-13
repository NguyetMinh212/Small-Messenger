package dev.proptit.messenger.ui.screen.login

import android.util.Log
import androidx.lifecycle.ViewModel
import com.orhanobut.hawk.Hawk
import dev.proptit.messenger.data.ApiClient
import dev.proptit.messenger.data.contact.dto.LoginInputDto
import dev.proptit.messenger.data.contact.dto.RegisterInputDto
import dev.proptit.messenger.data.contact.service.ContactService
import dev.proptit.messenger.setup.key.Keys
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit

class LoginViewModel() : ViewModel() {

    private val contactService: ContactService = ApiClient.contactService

    fun getIdUserByLogin(
        username: String,
        password: String,
        onSuccess: (Int) -> Unit,
        onError: () -> Unit
    ) {
        val request = LoginInputDto(username, password)
        val call = contactService.getIdUserByLogin(request)
        call.enqueue(object : retrofit2.Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    val responseData = response.body()
                    if (responseData != null) {
                        onSuccess(responseData)
                        Log.d("MainViewModel", "onResponse: ${Hawk.get(Keys.ID_USER, -1)}")
                        Log.d("MainViewModel", "onResponse: $responseData")
                    } else {
                        onError()
                    }
                } else {
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

    fun registerNewAccount(
        name: String,
        username: String,
        password: String,
        onSuccess: (Int) -> Unit,
        onError: () -> Unit
    ) {

        val request = RegisterInputDto(name = name, username = username, password = password)
        val call = contactService.registerNewAccount(request)
        call.enqueue(object : retrofit2.Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    val id = response.body()
                    if (id != null) {
                        onSuccess(id)
                    } else {
                        onError()
                    }
                } else {
                    onError()
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                onError()
            }

        })

    }
}