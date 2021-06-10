package com.ludwikowski.honorsmobileapps.finalproject

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ludwikowski.honorsmobileapps.finalproject.databinding.ChatItemLayoutBinding

class ChatAdapter(val chatList: MutableList<Chat>, val viewModel: AppViewModel): RecyclerView.Adapter<ChatViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ChatItemLayoutBinding.inflate(layoutInflater, parent, false)
        return ChatViewHolder(binding, viewModel)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = chatList[position]
        holder.bindChat(chat)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }
}