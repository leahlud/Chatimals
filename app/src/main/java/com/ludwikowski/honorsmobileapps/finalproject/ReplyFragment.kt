package com.ludwikowski.honorsmobileapps.finalproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.ludwikowski.honorsmobileapps.finalproject.databinding.FragmentReplyBinding
import java.text.SimpleDateFormat
import java.util.*

class ReplyFragment : Fragment() {

    private val viewModel: AppViewModel by activityViewModels()

    private var _binding: FragmentReplyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentReplyBinding.inflate(inflater, container, false)
        val rootView = binding.root

        binding.noRepliesTextView.visibility=View.INVISIBLE
        val args = ReplyFragmentArgs.fromBundle(requireArguments())
        setMessageView(args.chatUser, args.chatIcon, args.chatMessage, args.chatTimestamp)

        viewModel.setReplyEventListener()
        viewModel.replyList.observe(viewLifecycleOwner) { newReplyList ->
            setReplies(newReplyList)
        }

        val viewList = listOf(binding.backToChatImageView, binding.newReplyImageView)
        for(view in viewList) view.setOnClickListener { v: View ->
            when (v.id) {
                R.id.backToChat_imageView -> {
                    rootView.findNavController().navigateUp()
                }
                R.id.newReply_imageView -> {
                    val dialog = WriteReplyDialogFragment()
                    dialog.setTargetFragment(this, 1)
                    parentFragmentManager?.let { dialog.show(it, "ReplyCustomDialog") }
                }
            }
        }
        return rootView
    }
    fun setMessageView(user: String, icon: String, message: String, time: Long){
        viewModel.setUserToReplyTo(user)
        viewModel.setReplyChatTimeId("$time")
        binding.mainUserTagTextView.text = "@$user"
        binding.mainIconImageView.setImageResource(resources.getIdentifier(icon, "drawable", context?.packageName))
        binding.mainMessageTextView.text = message
        val messageDate: String = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(time)
        val messageTime: String = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(time)
        binding.mainTimestampTextView.text = "$messageDate ($messageTime)"
    }
    fun setReplies(newReplyList: MutableList<Reply>){
        binding.noRepliesTextView.visibility=View.INVISIBLE
        if(newReplyList.size == 0) binding.noRepliesTextView.visibility=View.VISIBLE
        val adapter = ReplyAdapter(newReplyList, viewModel)
        binding.replyRecyclerView.adapter = adapter
    }
    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("HELP", "reply destroyed")
        _binding = null
        viewModel.clearReplyList()
    }
}