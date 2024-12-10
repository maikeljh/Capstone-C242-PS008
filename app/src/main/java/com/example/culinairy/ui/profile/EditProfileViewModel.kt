package com.example.culinairy.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EditProfileViewModel : ViewModel() {
    private val _name = MutableLiveData<String>("Jhonson King")
    val name: LiveData<String> get() = _name

    private val _email = MutableLiveData<String>("jhonking@gmail.com")
    val email: LiveData<String> get() = _email

    private val _address = MutableLiveData<String>("Jhonson King")
    val address: LiveData<String> get() = _address

    fun updateProfile(newName: String, newEmail: String, newAddress: String) {
        _name.value = newName
        _email.value = newEmail
        _address.value = newAddress
    }
}