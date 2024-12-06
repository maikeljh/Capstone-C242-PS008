package com.example.culinairy.ui.capture_ocr

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CaptureReceiptViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is capture receipt Fragment"
    }
    val text: LiveData<String> = _text
}