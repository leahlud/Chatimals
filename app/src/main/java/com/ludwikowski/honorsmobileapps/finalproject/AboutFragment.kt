package com.ludwikowski.honorsmobileapps.finalproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.navigation.findNavController
import com.ludwikowski.honorsmobileapps.finalproject.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        val rootView = binding.root

        setAnimations()
        binding.backButton.setOnClickListener{
            rootView.findNavController().navigateUp()
        }
        return rootView
    }
    fun setAnimations(){
        val bottomAnimation = AnimationUtils.loadAnimation(context, R.anim.bottom_fade_animation)
        binding.subtitle1.animation=bottomAnimation
        binding.subtitle2.animation=bottomAnimation
        binding.subtitle3.animation=bottomAnimation
        binding.content1.animation=bottomAnimation
        binding.content2.animation=bottomAnimation
        binding.content3.animation=bottomAnimation
        binding.backButton.animation=bottomAnimation

    }
}