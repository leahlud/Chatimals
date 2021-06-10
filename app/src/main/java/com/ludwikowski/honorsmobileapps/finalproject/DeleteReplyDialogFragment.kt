package com.ludwikowski.honorsmobileapps.finalproject

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.ludwikowski.honorsmobileapps.finalproject.databinding.DialogDeleteBinding


class DeleteReplyDialogFragment : DialogFragment() {

    private val TAG = "DeleteReplyCustomDialog"
    private var _binding: DialogDeleteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AppViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DialogDeleteBinding.inflate(inflater, container, false)
        val rootView = binding.root

        val timeId = arguments?.getLong("timestamp")

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        binding.deleteMessageTextView.text="Are you sure you want to\ndelete your reply?"

        binding.cancelDeleteTextView.setOnClickListener {
            dialog!!.dismiss()
        }
        binding.confirmDeleteTextView.setOnClickListener {
            if (timeId != null) {
                viewModel.deleteReply(timeId)
            }
            dialog!!.dismiss()
        }
        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}