package com.ludwikowski.honorsmobileapps.finalproject

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ludwikowski.honorsmobileapps.finalproject.databinding.DialogSignOutBinding


class SignOutDialogFragment : DialogFragment() {

    private val TAG = "SignOutCustomDialog"
    private var _binding: DialogSignOutBinding? = null
    private val binding get() = _binding!!

    interface OnYesButtonSelected {
        fun signOutAndNavigate()
    }

    var mOnInputSelected: OnYesButtonSelected? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DialogSignOutBinding.inflate(inflater, container, false)
        val rootView = binding.root

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        binding.noTextView.setOnClickListener{
            dialog!!.dismiss()
        }
        binding.yesTextView.setOnClickListener {
            mOnInputSelected!!.signOutAndNavigate()
            dialog!!.dismiss()
        }
        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mOnInputSelected = targetFragment as OnYesButtonSelected?
        } catch (e: ClassCastException) {
            Log.e(TAG, "onAttach: ClassCastException : " + e.message)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}