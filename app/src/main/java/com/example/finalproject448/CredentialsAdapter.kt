package com.example.finalproject448

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CredentialsAdapter(
    private val credentials: MutableList<Credentials>,
    private val onDelete: (Credentials) -> Unit
) : RecyclerView.Adapter<CredentialsAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val serviceText: TextView = view.findViewById(R.id.text1)
        val passwordText: TextView = view.findViewById(R.id.text2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_credential, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = credentials[position]
        holder.serviceText.text = "${item.service}\nUsername: ${item.username}"
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
        onDelete(item)
    }

    fun getItem(position: Int): Credentials {
        return credentials[position]
    }

    fun getAll(): List<Credentials> {
        return credentials
    }
}
