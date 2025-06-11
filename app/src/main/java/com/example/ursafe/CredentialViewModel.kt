package com.example.ursafe.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.ursafe.data.Credential
import com.example.ursafe.data.CredentialRepository
import com.example.ursafe.data.URSafeDatabase
import com.example.ursafe.util.DatabaseUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CredentialViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = URSafeDatabase.getInstance(application, DatabaseUtils.getPassphrase()).credentialDao()
    private val repository = CredentialRepository(dao)

    val credentials: LiveData<List<Credential>> = repository
        .getAllCredentials(application.applicationContext)
        .asLiveData()

    fun addCredential(service: String, username: String, password: String) {
        val credential = Credential(serviceName = service, username = username, password = password)
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertCredential(getApplication(), credential)
        }
    }

    fun deleteCredential(credential: Credential) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCredential(credential)
        }
    }

    fun clearAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearAll()
        }
    }
}
