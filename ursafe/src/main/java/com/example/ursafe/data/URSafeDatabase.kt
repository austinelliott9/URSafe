package com.example.ursafe.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import net.sqlcipher.database.SupportFactory

@Database(entities = [Credential::class], version = 1)
abstract class URSafeDatabase : RoomDatabase() {
    abstract fun credentialDao(): CredentialDao

    companion object {
        @Volatile
        private var INSTANCE: URSafeDatabase? = null

        fun getInstance(context: Context, passphrase: ByteArray): URSafeDatabase {
            return INSTANCE ?: synchronized(this) {
                val factory = SupportFactory(passphrase)
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    URSafeDatabase::class.java,
                    "ursafe_encrypted.db"
                )
                    .openHelperFactory(factory)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}