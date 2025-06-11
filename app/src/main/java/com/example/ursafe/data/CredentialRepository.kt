package com.example.ursafe.data

import android.content.Context
import com.example.ursafe.util.SecurityUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CredentialRepository(private val credentialDao: CredentialDao) {

    fun getAllCredentials(context: Context): Flow<List<Credential>> {
        return credentialDao.getAllCredentials().map { list ->
            list.map {
                it.copy(
                    username = SecurityUtils.decrypt(it.username, context),
                    password = SecurityUtils.decrypt(it.password, context)
                )
            }
        }
    }

    suspend fun insertCredential(context: Context, credential: Credential) {
        val encrypted = credential.copy(
            username = SecurityUtils.encrypt(credential.username, context),
            password = SecurityUtils.encrypt(credential.password, context)
        )
        credentialDao.insertCredential(encrypted)
    }

    suspend fun deleteCredential(credential: Credential) {
        credentialDao.deleteCredential(credential)
    }

    suspend fun clearAll() {
        credentialDao.clearAll()
    }
}