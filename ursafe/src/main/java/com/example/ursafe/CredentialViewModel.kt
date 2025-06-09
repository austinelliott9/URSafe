package com.example.ursafe.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ursafe.data.Credential
import com.example.ursafe.data.URSafeDatabase
import com.example.ursafe.util.SecurityUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CredentialViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var passphrase: ByteArray
    val dao by lazy {
        URSafeDatabase.getInstance(application, passphrase).credentialDao()
    }

    fun addCredential(service: String, username: String, password: String) {
        val credential = Credential(serviceName = service, username = username, password = password)
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertCredential(credential)
        }
    }
}