package com.example.finalproject448

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var addButton: Button
    private lateinit var adapter: CredentialsViewer
    private val credentials = mutableListOf<Credentials>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_login, container, false)
        listView = view.findViewById(R.id.credentialsListView)
        addButton = view.findViewById(R.id.addCredentialButton)

        // Load saved credentials securely
        credentials.addAll(CredentialsStorage.loadCredentials(requireContext()))

        adapter = CredentialsViewer(requireContext(), credentials)
        listView.adapter = adapter

        addButton.setOnClickListener {
            // Example: simulate adding a new credential
            val newCredential = Credentials("Twitter", "user123", "Twi!ter@2025")
            credentials.add(newCredential)
            CredentialsStorage.saveCredentials(requireContext(), credentials)
            adapter.notifyDataSetChanged()
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}