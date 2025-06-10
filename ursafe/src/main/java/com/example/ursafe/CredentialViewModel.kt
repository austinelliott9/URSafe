package com.example.ursafe.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ursafe.data.Credential
import com.example.ursafe.data.URSafeDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.ursafe.util.DatabaseUtils

class CredentialViewModel(application: Application) : AndroidViewModel(application) {
    val dao by lazy {
        URSafeDatabase.getInstance(application, DatabaseUtils.getPassphrase()).credentialDao()
    }

    val credentials: LiveData<List<Credential>> by lazy {
        dao.getAllCredentials().asLiveData()
    }

    fun addCredential(service: String, username: String, password: String) {
        val credential = Credential(serviceName = service, username = username, password = password)
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertCredential(credential)
        }
    }

    fun deleteCredential(credential: Credential) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteCredential(credential)
        }
    }

    fun clearAll() {
        viewModelScope.launch(Dispatchers.IO) {
            dao.clearAll()
        }
    }
}