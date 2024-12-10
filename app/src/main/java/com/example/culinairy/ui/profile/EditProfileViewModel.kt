package com.example.culinairy.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.culinairy.model.UpdateUserRequestBody
import com.example.culinairy.repository.AuthRepository
import kotlinx.coroutines.launch

class EditProfileViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name

    private val _address = MutableLiveData<String>()
    val address: LiveData<String> get() = _address

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchUser(token: String) {
        viewModelScope.launch {
            try {
                val response = repository.getUser(token)
                if (response.isSuccessful) {
                    val user = response.body()
                    user?.let {
                        _name.value = it.user.name
                        _address.value = it.user.address
                    }
                } else {
                    _error.value = "Failed to fetch user: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
            }
        }
    }

    fun updateProfile(token: String, updateUserBody: UpdateUserRequestBody) {
        viewModelScope.launch {
            try {
                val response = repository.updateUser(token, updateUserBody)
                if (response.isSuccessful) {
                    val user = response.body()
                    user?.let {
                        _name.value = it.user.name
                        _address.value = it.user.address
                    }
                } else {
                    _error.value = "Failed to update user: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
            }
        }
    }
}