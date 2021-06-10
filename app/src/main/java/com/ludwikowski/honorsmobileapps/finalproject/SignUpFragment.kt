package com.ludwikowski.honorsmobileapps.finalproject

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.text.bold
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ludwikowski.honorsmobileapps.finalproject.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    private val viewModel: AppViewModel by activityViewModels()
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        val rootView = binding.root

        viewModel.setErrorBlank()
        setAnimations()

        if(savedInstanceState != null){
            viewModel.setErrorMessage(savedInstanceState.getString(ERROR_MESSAGE).toString())
            if(savedInstanceState.getString(USERNAME_HINT).toString() != "Username") setRedUsernameHint()
            if(savedInstanceState.getString(PASSWORD_HINT).toString() != "Password") setRedPasswordHint()
            if(savedInstanceState.getString(CONFIRM_PASSWORD_HINT).toString() != "Confirm Password") setRedConfirmPasswordHint()
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            setErrorMessage(error)
        }
        viewModel.accountCreated.observe(viewLifecycleOwner) { newAccountCreated ->
            checkAccountStatus(newAccountCreated)
        }

        val backViews = listOf<View>(binding.signupBackImageView, binding.signupBackTextView)
        for(view in backViews) view.setOnClickListener {
            rootView.findNavController().navigateUp()
        }

        binding.termsTextView.setOnClickListener {
            val dialog = TermsDialogFragment()
            dialog.setTargetFragment(this, 1)
            parentFragmentManager?.let { dialog.show(it, "TermsCustomDialog") }
        }
        binding.signUpButton.setOnClickListener {
            if (binding.makeUsernameEditText.text.toString() == "") {
                viewModel.setErrorBlank()
                setRedUsernameHint()
            }
            else if (binding.makePasswordEditText.text.toString() == "") {
                viewModel.setErrorBlank()
                setRedPasswordHint()
            }
            else if (binding.confirmPasswordEditText.text.toString() == "") {
                viewModel.setErrorBlank()
                setRedConfirmPasswordHint()
            }
            else if(!viewModel.checkForSpaces(binding.makeUsernameEditText.text.toString())){
                viewModel.setSpaceError("Username")
            }
            else if (!viewModel.checkForSpaces(binding.makePasswordEditText.text.toString())) {
                viewModel.setSpaceError("Password")
            }
            else {
                viewModel.checkUsernameAvailability(binding.makeUsernameEditText.text.toString(),
                        binding.makePasswordEditText.text.toString(),
                        binding.confirmPasswordEditText.text.toString(),
                        binding.acceptTermsCheckBox.isChecked)
            }
        }
        return rootView
    }
    fun setAnimations(){
        val bottomAnimation = AnimationUtils.loadAnimation(context, R.anim.bottom_fade_animation)
        binding.signUpTextView.animation=bottomAnimation
        binding.pawImageView2.animation=bottomAnimation
        binding.subtextTextView2.animation=bottomAnimation
        binding.makeUsernameEditText.animation=bottomAnimation
        binding.userImageView.animation=bottomAnimation
        binding.makePasswordEditText.animation=bottomAnimation
        binding.lock1ImageView.animation=bottomAnimation
        binding.confirmPasswordEditText.animation=bottomAnimation
        binding.lock2ImageView.animation=bottomAnimation
        binding.acceptTermsCheckBox.animation=bottomAnimation
        binding.termsTextView.animation=bottomAnimation
        binding.signUpButton.animation=bottomAnimation
        binding.firstImageView.animation= AnimationUtils.loadAnimation(context, R.anim.fade_in_animation)
        binding.signupBackTextView.animation = bottomAnimation
        binding.signupBackImageView.animation = bottomAnimation
    }
    fun setErrorMessage(error: String) {
        if (error != "") binding.signUpErrorTextView.text = SpannableStringBuilder().bold { append("Error:") }.append("\n$error")
        else binding.signUpErrorTextView.text = ""
    }
    fun checkAccountStatus(accountCreated: Boolean) {
        if (accountCreated) binding.root.findNavController().navigate(R.id.action_signUpFragment_to_createPetFragment)
    }
    fun setRedUsernameHint(){
        binding.makeUsernameEditText.setHintTextColor(Color.parseColor("#F68B8B"))
        binding.makeUsernameEditText.hint = "Please enter a username"
    }
    fun setRedPasswordHint(){
        binding.makePasswordEditText.setHintTextColor(Color.parseColor("#F68B8B"))
        binding.makePasswordEditText.hint = "Please enter a password"
    }
    fun setRedConfirmPasswordHint(){
        binding.confirmPasswordEditText.setHintTextColor(Color.parseColor("#F68B8B"))
        binding.confirmPasswordEditText.hint = "Please confirm your password"
    }
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        if(_binding != null) {
            savedInstanceState.putString(ERROR_MESSAGE, viewModel.errorMessage.value)
            savedInstanceState.putString(USERNAME_HINT, binding.makeUsernameEditText.hint.toString())
            savedInstanceState.putString(PASSWORD_HINT, binding.makePasswordEditText.hint.toString())
            savedInstanceState.putString(CONFIRM_PASSWORD_HINT, binding.confirmPasswordEditText.hint.toString())
        }
    }
    companion object {
        const val ERROR_MESSAGE = "errorMessage"
        const val USERNAME_HINT = "usernameError"
        const val PASSWORD_HINT = "passwordError"
        const val CONFIRM_PASSWORD_HINT = "confirmPasswordError"
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}