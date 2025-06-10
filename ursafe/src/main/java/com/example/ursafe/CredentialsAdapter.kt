package com.example.ursafe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ursafe.data.Credential

class CredentialsAdapter(
    private val credentials: MutableList<Credential>,
    private val onUpdate: () -> Unit
) : RecyclerView.Adapter<CredentialsAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val serviceText: TextView = view.findViewById(R.id.text_service)
        val usernameText: TextView = view.findViewById(R.id.text_username)
        val passwordText: TextView = view.findViewById(R.id.text_password)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_credential, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = credentials[position]
        holder.serviceText.text = item.serviceName
        holder.usernameText.text = "Username: ${item.username}"
        holder.passwordText.text = "•••••••••• (tap to reveal)"

        holder.itemView.setOnClickListener {
            val isHidden = holder.passwordText.text.startsWith("••")
            holder.passwordText.text = if (isHidden) item.password else "•••••••••• (tap to reveal)"
        }
    }

    override fun getItemCount(): Int = credentials.size

    fun removeAt(position: Int) {
        val item = credentials[position]
        credentials.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getItem(position: Int): Credential {
        return credentials[position]
    }

    fun getAll(): List<Credential> {
        return credentials
    }

    fun updateList(newList: List<Credential>) {
        credentials.clear()
        credentials.addAll(newList)
        notifyDataSetChanged()
    }
}
