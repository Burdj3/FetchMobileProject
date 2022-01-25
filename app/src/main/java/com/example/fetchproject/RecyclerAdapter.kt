package com.example.fetchproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(val list: List<MainActivity.ListItem>) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {

        holder.listIdText.text = list[position].listId
        holder.nameText.text = list[position].name
        holder.idText.text = list[position].id

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var listIdText: TextView = itemView.findViewById(R.id.list_id_text)
        var nameText: TextView = itemView.findViewById(R.id.name_text)
        var idText: TextView = itemView.findViewById(R.id.id_text)
    }

}