package com.ludwikowski.honorsmobileapps.finalproject

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.text.bold
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.ludwikowski.honorsmobileapps.finalproject.databinding.DialogConfirmPetBinding


class ConfirmPetDialogFragment : DialogFragment() {

    private val TAG = "MyCustomDialog"
    private var _binding: DialogConfirmPetBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AppViewModel by activityViewModels()

    interface OnConfirmButtonSelected {
        fun navigateAndCreatePet()
    }

    var mOnInputSelected: OnConfirmButtonSelected? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DialogConfirmPetBinding.inflate(inflater, container, false)
        val rootView = binding.root

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        binding.confirmPetNameTextView.text = SpannableStringBuilder().bold { append("Pet Name: ") }.append(viewModel.enteredPetName)
        binding.confirmAnimalTextView.text = SpannableStringBuilder().bold { append("Animal: ") }.append(viewModel.selectedAnimal)
        binding.confirmFurColorTextView.text = SpannableStringBuilder().bold { append("Fur color:") }.append("")
        binding.confirmCollarColorTextView.text = SpannableStringBuilder().bold { append("Collar color:") }.append("")
        binding.furImageView.setImageResource(resources.getIdentifier(viewModel.selectedFurColor, "drawable", context?.packageName))
        binding.collarImageView.setImageResource(resources.getIdentifier(viewModel.selectedCollarColor, "drawable", context?.packageName))


        binding.cancelAction.setOnClickListener{
            dialog!!.dismiss()
        }
        binding.yesAction.setOnClickListener {
            mOnInputSelected!!.navigateAndCreatePet()
            dialog!!.dismiss()
        }
        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mOnInputSelected = targetFragment as OnConfirmButtonSelected?
        } catch (e: ClassCastException) {
            Log.e(TAG, "onAttach: ClassCastException : " + e.message)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}