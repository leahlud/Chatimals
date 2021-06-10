package com.ludwikowski.honorsmobileapps.finalproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.navigation.findNavController
import com.ludwikowski.honorsmobileapps.finalproject.databinding.FragmentStartBinding

class StartFragment : Fragment() {

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        val rootView = binding.root

        setAnimations()
        val buttonsList = listOf(binding.loginNavButton, binding.signupNavButton)
        for(button in buttonsList) button.setOnClickListener{ view: View ->
            when(view.id){
                R.id.loginNav_button -> rootView.findNavController().navigate(R.id.action_startFragment_to_loginFragment)
                R.id.signupNav_button -> rootView.findNavController().navigate(R.id.action_startFragment_to_signUpFragment)
            }
        }

        return rootView
    }
    fun setAnimations(){
        binding.cat1ImageView.animation=AnimationUtils.loadAnimation(context, R.anim.fast_fade_animation)
        binding.dogImageView.animation=AnimationUtils.loadAnimation(context, R.anim.medium_fade_animation)
        binding.cat2ImageView.animation=AnimationUtils.loadAnimation(context, R.anim.slow_fade_animation)
        binding.chatimalsImageView.animation= AnimationUtils.loadAnimation(context, R.anim.fade_in_animation)
        binding.loginNavButton.animation= AnimationUtils.loadAnimation(context, R.anim.fade_in_animation)
        binding.signupNavButton.animation= AnimationUtils.loadAnimation(context, R.anim.fade_in_animation)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}