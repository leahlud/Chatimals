package com.ludwikowski.honorsmobileapps.finalproject

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ludwikowski.honorsmobileapps.finalproject.databinding.ReplyItemLayoutBinding
import java.text.SimpleDateFormat
import java.util.*

class ReplyViewHolder(val binding: ReplyItemLayoutBinding, val viewModel: AppViewModel): RecyclerView.ViewHolder(binding.root) {
    private lateinit var currentReply: Reply

    init {
        setClickListeners()
    }

    fun setClickListeners(){
        val deleteList = listOf(binding.deleteReplyTextView, binding.garbageImageView2)
        for(view in deleteList) view.setOnClickListener{
            val dialog = DeleteReplyDialogFragment()
            val activity = itemView.context as AppCompatActivity
            dialog.show(activity.supportFragmentManager, null)
            val args = Bundle()
            args.putLong("timestamp", currentReply.replyTimestamp)
            dialog.arguments = args
        }
    }

    fun bindReply(reply: Reply){
        currentReply = reply

        if(currentReply.replyUser == viewModel.username) {
            binding.deleteReplyTextView.visibility= View.VISIBLE
            binding.garbageImageView2.visibility= View.VISIBLE
        }
        else {
            binding.deleteReplyTextView.visibility= View.GONE
            binding.garbageImageView2.visibility= View.GONE
        }

        binding.replyUserTextView.text = "@${currentReply.replyUser}"
        binding.replyMessageTextView.text = currentReply.replyMessage
        binding.pfpImageView.setImageResource(itemView.context.resources.getIdentifier(currentReply.pfp, "drawable", itemView.context?.packageName))
        val messageDate: String = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(currentReply.replyTimestamp)
        val messageTime: String = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(currentReply.replyTimestamp)
        binding.replyTimestampTextView.text = "$messageDate ($messageTime)"
    }
}