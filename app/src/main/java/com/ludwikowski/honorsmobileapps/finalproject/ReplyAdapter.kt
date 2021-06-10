package com.ludwikowski.honorsmobileapps.finalproject

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ludwikowski.honorsmobileapps.finalproject.databinding.ReplyItemLayoutBinding

class ReplyAdapter(val replyList: MutableList<Reply>, val viewModel: AppViewModel): RecyclerView.Adapter<ReplyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReplyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ReplyItemLayoutBinding.inflate(layoutInflater, parent, false)
        return ReplyViewHolder(binding, viewModel)
    }

    override fun onBindViewHolder(holder: ReplyViewHolder, position: Int) {
        val reply = replyList[position]
        holder.bindReply(reply)
    }

    override fun getItemCount(): Int {
        return replyList.size
    }
}