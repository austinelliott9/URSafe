package com.example.finalproject448

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up a blank container for fragment
        setContentView(R.layout.activity_test)

        // Load the fragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, GeneratePasswordFragment())
                .commit()
        }
    }
}
