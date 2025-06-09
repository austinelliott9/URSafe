package com.example.ursafe

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CredentialsListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var addButton: Button
    private lateinit var adapter: CredentialsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_credentials_list, container, false)
        recyclerView = view.findViewById(R.id.credentialsRecyclerView)
        addButton = view.findViewById(R.id.addCredentialButton)

        adapter = CredentialsAdapter(
            CredentialsStorage.loadCredentials(requireContext())
                .sortedBy { it.service.lowercase() }
                .toMutableList()
        ) {
            CredentialsStorage.saveCredentials(requireContext(), adapter.getAll())
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Swipe to delete
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
                    .setMessage("Are you sure you want to delete the credential for ${credential.service}?")
                    .setPositiveButton("Delete") { _, _ ->
                        adapter.removeAt(position)
                    }
                    .setNegativeButton("Cancel") { _, _ ->
                        adapter.notifyItemChanged(position) // Reset swipe
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
