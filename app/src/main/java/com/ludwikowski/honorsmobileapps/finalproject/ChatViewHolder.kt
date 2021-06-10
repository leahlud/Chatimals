package com.ludwikowski.honorsmobileapps.finalproject

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ludwikowski.honorsmobileapps.finalproject.databinding.ChatItemLayoutBinding
import java.text.SimpleDateFormat
import java.util.*


class ChatViewHolder(val binding: ChatItemLayoutBinding, val viewModel: AppViewModel): RecyclerView.ViewHolder(binding.root) {

    private lateinit var currentChat: Chat

    init {
        setClickListeners()
    }

    fun setClickListeners(){
        binding.heartImageButton.setOnClickListener{
            if (currentChat.listOfUserLikes.indexOf(viewModel.userId) != -1) {
                viewModel.unlike(currentChat.timestamp)
                binding.heartImageButton.setImageResource(R.drawable.unclicked_heart)
                binding.likesTextView.setTextColor(Color.parseColor("#878787"))
            } else {
                viewModel.like(currentChat.timestamp)
                binding.heartImageButton.setImageResource(R.drawable.clicked_heart)
                binding.likesTextView.setTextColor(Color.parseColor("#F38FA2"))
            }
        }
        val deleteList = listOf(binding.deleteChatTextView, binding.garbageImageView)
        for(view in deleteList) view.setOnClickListener { v: View ->
            val dialogFragment = DeleteDialogFragment()
            val activity = itemView.context as AppCompatActivity
            dialogFragment.show(activity.supportFragmentManager, null)
            val args = Bundle()
            args.putLong("time", currentChat.timestamp)
            args.putString("type", "message")
            dialogFragment.arguments = args
        }
        val replyList = listOf(binding.replyTextView, binding.replyArrowImageView)
        for(view in replyList) view.setOnClickListener { v: View ->
            viewModel.setMessageId(currentChat.timestamp)
            viewModel.getReplyList()
            val action = ChatFragmentDirections.actionChatFragmentToReplyFragment(currentChat.message, currentChat.iconResourceId, currentChat.timestamp, currentChat.username)
            binding.root.findNavController().navigate(action)
        }
    }

    fun bindChat(chat: Chat){
        currentChat = chat

        if(currentChat.listOfUserLikes.indexOf(viewModel.userId) != -1) {
            binding.heartImageButton.setImageResource(R.drawable.clicked_heart)
            binding.likesTextView.setTextColor(Color.parseColor("#F38FA2"))
        }
        else {
            binding.heartImageButton.setImageResource(R.drawable.unclicked_heart)
            binding.likesTextView.setTextColor(Color.parseColor("#878787"))
        }

        if(currentChat.username == viewModel.username) {
            binding.garbageImageView.visibility = View.VISIBLE
            binding.deleteChatTextView.visibility = View.VISIBLE
        }
        else {
            binding.garbageImageView.visibility = View.GONE
            binding.deleteChatTextView.visibility = View.GONE
        }

        binding.iconImageView.setImageResource(itemView.context.resources.getIdentifier(currentChat.iconResourceId, "drawable", itemView.context?.packageName))
        binding.userTagTextView.text = "@${currentChat.username}"
        binding.messageTextView.text = currentChat.message
        binding.likesTextView.text = "${currentChat.likes}"

        val messageDate: String = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(currentChat.timestamp)
        val messageTime: String = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(currentChat.timestamp)
        binding.timeStampTextView.text = "$messageDate ($messageTime)"
    }
}