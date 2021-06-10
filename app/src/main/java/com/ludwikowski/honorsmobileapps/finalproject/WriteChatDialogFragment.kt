package com.ludwikowski.honorsmobileapps.finalproject

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.ludwikowski.honorsmobileapps.finalproject.databinding.DialogWriteChatBinding
import java.util.*


class WriteChatDialogFragment : DialogFragment() {

    private val TAG = "ChatCustomDialog"
    private var _binding: DialogWriteChatBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AppViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DialogWriteChatBinding.inflate(inflater, container, false)
        val rootView = binding.root

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        binding.cancelChatTextView.setOnClickListener{
            dialog!!.dismiss()
        }
        binding.sendChatTextView.setOnClickListener {
            val message = binding.chatEditText.text.toString().trim()
            if(message != ""){
                viewModel.addChatToDatabase(message, Date().time)
                binding.chatEditText.text.clear()
                dialog!!.dismiss()
            }
            else{
                binding.chatEditText.setHintTextColor(Color.parseColor("#F68B8B"))
                binding.chatEditText.text.clear()
                binding.chatEditText.hint = "message cannot be empty"
            }
        }
        return rootView
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}