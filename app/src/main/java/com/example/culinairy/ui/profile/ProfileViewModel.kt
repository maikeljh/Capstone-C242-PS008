package com.example.culinairy.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is profile Fragment"
//    }
//    val text: LiveData<String> = _text
    private val _name = MutableLiveData<String>("Jhonson King")
    val name: LiveData<String> get() = _name

    private val _email = MutableLiveData<String>("jhonking@gmail.com")
    val email: LiveData<String> get() = _email

    fun updateProfile(newName: String, newEmail: String) {
        _name.value = newName
        _email.value = newEmail
    }
}