package com.example.finalproject448
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var addButton: Button
    private lateinit var adapter: CredentialsViewer
    private val credentials = mutableListOf<Credentials>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.credentialsListView)
        addButton = findViewById(R.id.addCredentialButton)

        // Load saved credentials securely
        credentials.addAll(CredentialsStorage.loadCredentials(this))

        adapter = CredentialsViewer(this, credentials)
        listView.adapter = adapter

        addButton.setOnClickListener {
            // Example: simulate adding a new credential
            val newCredential = Credentials("Twitter", "user123", "Twi!ter@2025")
            credentials.add(newCredential)
            CredentialsStorage.saveCredentials(this, credentials)
            adapter.notifyDataSetChanged()
        }
    }
}
//Test

// Raksha Karthikeyan
