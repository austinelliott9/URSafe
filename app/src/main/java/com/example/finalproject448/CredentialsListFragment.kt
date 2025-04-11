package com.example.finalproject448

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// Fragment initialization parameters (optional for your use case)
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CredentialsListFragment : Fragment() {

    // Optional fragment parameters (you can remove them if not needed)
    private var param1: String? = null
    private var param2: String? = null

    // UI components
    private lateinit var credentialsListView: ListView
    private lateinit var credentials: List<Credentials>
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize parameters from arguments (if any)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment (make sure you have a ListView in the layout)
        return inflater.inflate(R.layout.fragment_credentials_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the ListView
        credentialsListView = view.findViewById(R.id.credentialsListView)

        // Load credentials using the fragment method
        credentials = loadCredentials(requireContext())

        // Set up the adapter to display the credentials
        val adapter = CredentialsViewer(requireContext(), credentials)
        credentialsListView.adapter = adapter
    }

    // Load credentials from EncryptedSharedPreferences
    private fun loadCredentials(context: Context): MutableList<Credentials> {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val sharedPrefs = EncryptedSharedPreferences.create(
            context,
            "secure_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        val json = sharedPrefs.getString("stored_credentials", null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<Credentials>>() {}.type
        return gson.fromJson(json, type)
    }

    companion object {
        /**
         * Factory method to create a new instance of this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CredentialsListFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CredentialsListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
