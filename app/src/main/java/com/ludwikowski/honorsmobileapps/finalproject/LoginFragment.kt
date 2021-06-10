package com.ludwikowski.honorsmobileapps.finalproject

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.text.bold
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.ludwikowski.honorsmobileapps.finalproject.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private val viewModel: AppViewModel by activityViewModels()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val rootView = binding.root

        viewModel.setErrorBlank()
        setAnimations()

        if(savedInstanceState != null){
            viewModel.setErrorMessage(savedInstanceState.getString(ERROR_MESSAGE).toString())
            if(savedInstanceState.getString(USERNAME_HINT).toString() != "Username") setRedUsernameHint()
            if(savedInstanceState.getString(PASSWORD_HINT).toString() != "Password") setRedPasswordHint()
        }

        viewModel.loggedIn.observe(viewLifecycleOwner) { loggedIn ->
            checkLoggedIn(loggedIn)
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            setErrorMessage(error)
        }

        val backViews = listOf<View>(binding.loginBackImageView, binding.loginBackTextView)
        for(view in backViews) view.setOnClickListener {
            rootView.findNavController().navigateUp()
        }

        binding.loginButton.setOnClickListener{
            if(binding.usernameEditText.text.toString()==""){
                viewModel.setErrorBlank()
                setRedUsernameHint()
            }
            else if(binding.passwordEditText.text.toString()==""){
                viewModel.setErrorBlank()
                setRedPasswordHint()
            }
            else {
                viewModel.checkLogin(binding.usernameEditText.text.toString(), binding.passwordEditText.text.toString())
            }
        }
        return rootView
    }
    fun setAnimations(){
        val topAnimation = AnimationUtils.loadAnimation(context, R.anim.top_fade_animation)
        binding.helloTextView.animation=topAnimation
        binding.pawImageView.animation=topAnimation
        binding.subtextTextView.animation=topAnimation
        binding.usernameEditText.animation=topAnimation
        binding.userImageView1.animation=topAnimation
        binding.passwordEditText.animation=topAnimation
        binding.lockImageView.animation=topAnimation
        binding.loginButton.animation=topAnimation
        binding.signUpErrorTextView2.animation=topAnimation
        binding.loginBackTextView.animation = topAnimation
        binding.loginBackImageView.animation = topAnimation
    }
    fun checkLoggedIn(loggedIn: Boolean){
        if(loggedIn) {
            viewModel.setMoodCountDownTimer()
            viewModel.setHungerCountDownTimer()
            viewModel.getChatList()
            viewModel.getRateList()
            binding.root.findNavController().navigate(R.id.action_loginFragment_to_petFragment)}
    }
    fun setErrorMessage(errorMessage: String){
        if(errorMessage!="") binding.signUpErrorTextView2.text = SpannableStringBuilder().bold { append("Error:") }.append("\n$errorMessage")
        else binding.signUpErrorTextView2.text = ""
    }
    fun setRedUsernameHint(){
        binding.usernameEditText.setHintTextColor(Color.parseColor("#F68B8B"))
        binding.usernameEditText.hint = "Please enter a username"
    }
    fun setRedPasswordHint(){
        binding.passwordEditText.setHintTextColor(Color.parseColor("#F68B8B"))
        binding.passwordEditText.hint = "Please enter a password"
    }
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        if(_binding != null) {
            savedInstanceState.putString(ERROR_MESSAGE, viewModel.errorMessage.value)
            savedInstanceState.putString(USERNAME_HINT, binding.usernameEditText.hint.toString())
            savedInstanceState.putString(PASSWORD_HINT, binding.passwordEditText.hint.toString())
        }
    }
    companion object {
        const val ERROR_MESSAGE = "errorMessage"
        const val USERNAME_HINT = "usernameError"
        const val PASSWORD_HINT = "passwordError"
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}