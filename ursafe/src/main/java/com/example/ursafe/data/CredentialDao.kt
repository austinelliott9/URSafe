package com.example.ursafe.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CredentialDao {

    @Query("SELECT * FROM credentials")
    fun getAllCredentials(): Flow<List<Credential>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCredential(credential: Credential)

    @Delete
    suspend fun deleteCredential(credential: Credential)

    @Query("DELETE FROM credentials")
    suspend fun clearAll()
}