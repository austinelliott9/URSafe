package com.example.ursafe
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //testGson(this) // This runs the GSON test

    }

    /*private fun testGson(context: Context) {
        val gson = Gson()

        // Test function to create a sample list of credentials to test JSON
        val credentialsList = listOf(
            Credentials("zaman", "password123", "Gmail"),
            Credentials("Reddit", "qwerty456", "ReadIt"),
            Credentials("Facebook", "Project488","FacePassW")
        )

        // this Serialize to JSON
        val json = gson.toJson(credentialsList)
        Log.d("GsonTest", "Serialized JSON: $json")

        // this Store it temporarily in SharedPreferences
        val prefs = CredentialsStorage.getPrefs(context)
        prefs.edit {
            putString("test_credentials", json)
        }

        // This Read it back and deserialize
        val storedJson = prefs.getString("test_credentials", null)
        //this reads JSON in human readable format (not good practice), test only
        Log.d("GsonTest", "Stored JSON: $storedJson")

        //Log.d("GsonTest", "Stored JSON: $storedJson")
        val type = object : TypeToken<List<Credentials>>() {}.type
        val deserializedList: List<Credentials> = gson.fromJson(storedJson, type)
        Log.d("GsonTest", "Deserialized List: $deserializedList")
    }*/

}


