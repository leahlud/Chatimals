package com.ludwikowski.honorsmobileapps.finalproject

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.ludwikowski.honorsmobileapps.finalproject.databinding.DialogWriteReplyBinding
import java.util.*


class WriteReplyDialogFragment : DialogFragment() {

    private val TAG = "ReplyCustomDialog"
    private var _binding: DialogWriteReplyBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AppViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DialogWriteReplyBinding.inflate(inflater, container, false)
        val rootView = binding.root

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        binding.replyEditText.setHint("write a reply to ${viewModel.userToReplyTo}...")

        binding.cancelReplyTextView.setOnClickListener{
            dialog!!.dismiss()
        }
        binding.sendReplyTextView.setOnClickListener {
            val message = binding.replyEditText.text.toString().trim()
            if(message != ""){
                viewModel.addReplyToDatabase(message, Date().time)
                binding.replyEditText.text.clear()
                dialog!!.dismiss()
            }
            else {
                binding.replyEditText.setHintTextColor(Color.parseColor("#F68B8B"))
                binding.replyEditText.text.clear()
                binding.replyEditText.hint = "message cannot be empty"
            }
        }
        return rootView
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}