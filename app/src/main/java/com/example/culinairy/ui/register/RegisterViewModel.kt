package com.example.culinairy.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.culinairy.model.RegisterRequestBody
import com.example.culinairy.repository.AuthRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel : ViewModel() {

    val registrationStatus = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

    fun register(name: String, email: String, password: String, address: String) {
        viewModelScope.launch {
            try {
                val response = AuthRepository().register(RegisterRequestBody(name, email, password, address))
                if (response.isSuccessful) {
                    registrationStatus.postValue(true)
                } else {
                    errorMessage.postValue("Registration failed. Please check your credentials.")
                }
            } catch (e: HttpException) {
                errorMessage.postValue("Registration failed. ${e.message}")
            } catch (e: Throwable) {
                errorMessage.postValue("Registration failed. ${e.message}")
            }
        }
    }
}
