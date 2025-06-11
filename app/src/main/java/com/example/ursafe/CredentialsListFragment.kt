package com.example.ursafe

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ursafe.viewmodel.CredentialViewModel

class CredentialsListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var addButton: Button
    private lateinit var searchView: SearchView
    private lateinit var adapter: CredentialsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_credentials_list, container, false)

        recyclerView = view.findViewById(R.id.credentialsRecyclerView)
        addButton = view.findViewById(R.id.addCredentialButton)
        searchView = view.findViewById(R.id.searchView)

        val viewModel = ViewModelProvider(requireActivity())[CredentialViewModel::class.java]
        adapter = CredentialsAdapter(mutableListOf()) {}

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.credentials.observe(viewLifecycleOwner) { credentialList ->
            adapter.updateList(credentialList)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.filter(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter(newText ?: "")
                return true
            }
        })

        // Swipe to delete with confirmation dialog
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val credential = adapter.getItem(position)

                AlertDialog.Builder(requireContext())
                    .setTitle("Delete Credential")
                    .setMessage("Are you sure you want to delete the credential for ${credential.serviceName}?")
                    .setPositiveButton("Delete") { _, _ ->
                        viewModel.deleteCredential(credential)
                    }
                    .setNegativeButton("Cancel") { _, _ ->
                        adapter.notifyItemChanged(position)
                    }
                    .setCancelable(false)
                    .show()
            }
        })

        itemTouchHelper.attachToRecyclerView(recyclerView)

        addButton.setOnClickListener {
            findNavController().navigate(R.id.action_credentialsListFragment_to_addCredentialsFragment)
        }

        return view
    }
}
