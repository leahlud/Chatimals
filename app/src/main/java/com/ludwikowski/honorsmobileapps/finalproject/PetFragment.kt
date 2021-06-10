package com.ludwikowski.honorsmobileapps.finalproject

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ClipDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ludwikowski.honorsmobileapps.finalproject.databinding.FragmentPetBinding


class PetFragment : Fragment(), SignOutDialogFragment.OnYesButtonSelected{

    private val viewModel: AppViewModel by activityViewModels()

    private var _binding: FragmentPetBinding? = null
    private val binding get() = _binding!!

    private lateinit var mediaPlayer : MediaPlayer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPetBinding.inflate(inflater, container, false)
        val rootView = binding.root

        setUpPetFragment()
        viewModel.setUpHealthObservers()

        viewModel.petMood.observe(viewLifecycleOwner){ mood ->
            (binding.moodImageView.drawable as ClipDrawable).level = mood * 100
            calculateOverallHealth()
        }
        viewModel.petHunger.observe(viewLifecycleOwner){ hunger ->
            (binding.hungerImageView.drawable as ClipDrawable).level = hunger * 100
            calculateOverallHealth()
        }

        binding.petImageView.setOnClickListener {
            if (viewModel.userPet.mood != 0 && viewModel.userPet.hunger != 0 && viewModel.userPet.mood + viewModel.userPet.hunger >= 71 && !mediaPlayer.isPlaying) {
                viewModel.addMoodPoints(1)
                if (viewModel.userPet.petResourceString.indexOf("dog") != -1) {
                    binding.tongueImageView.setImageResource(R.drawable.dog_tongue)
                    binding.tongueImageView.visibility = View.VISIBLE
                    playSound("woof")
                } else {
                    binding.tongueImageView.setImageResource(R.drawable.cat_tongue)
                    binding.tongueImageView.visibility = View.VISIBLE
                    playSound("meow")
                }
            }
        }

        val buttonsList = mutableListOf(binding.chatPetImageView, binding.ratePetImageView, binding.foodButton, binding.waterButton, binding.playButton)
        for(button in buttonsList) button.setOnClickListener{ v: View ->
            viewModel.getChatList()
            viewModel.getRateList()
            when(v.id){
                R.id.chatPet_imageView -> rootView.findNavController().navigate(R.id.action_petFragment_to_chatFragment)
                R.id.ratePet_imageView -> rootView.findNavController().navigate(R.id.action_petFragment_to_rateFragment)
                R.id.food_button -> {
                    if (!mediaPlayer.isPlaying) {
                        binding.hungerGif.setImageResource(R.drawable.food_bowl_gif)
                        binding.hungerGif.visibility = View.VISIBLE
                        viewModel.addHungerPoints(4)
                        playSound("food")
                    }
                }
                R.id.water_button -> {
                    if (!mediaPlayer.isPlaying) {
                        binding.hungerGif.setImageResource(R.drawable.water_bowl_gif)
                        binding.hungerGif.visibility = View.VISIBLE
                        viewModel.addHungerPoints(2)
                        playSound("water")
                    }
                }
                R.id.play_button -> {
                    if (!mediaPlayer.isPlaying) {
                        viewModel.addMoodPoints(4)
                        if (viewModel.userPet.petResourceString.indexOf("dog") != -1) {
                            binding.playGif.setImageResource(R.drawable.tennis_gif)
                            binding.playGif.visibility = View.VISIBLE
                            playSound("play")
                        } else {
                            binding.playGif.setImageResource(R.drawable.yarn_gif)
                            binding.playGif.visibility = View.VISIBLE
                            playSound("play")
                        }
                    }
                }
            }
        }
        binding.toolbar.inflateMenu(R.menu.options_menu)
        binding.toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.about_item -> rootView.findNavController().navigate(R.id.action_petFragment_to_aboutFragment)
                R.id.sign_out_item -> {
                    val dialog = SignOutDialogFragment()
                    dialog.setTargetFragment(this, 1)
                    parentFragmentManager?.let { dialog.show(it, "SignOutCustomDialog") }
                }
            }
            NavigationUI.onNavDestinationSelected(it, requireView().findNavController()) || super.onOptionsItemSelected(it)
        }
        return rootView
    }

    fun setUpPetFragment(){
        mediaPlayer = MediaPlayer.create(context, R.raw.meow)
        viewModel.setResources()
        calculateOverallHealth()
        binding.petNameTextView.text = viewModel.userPet.petName
        (binding.hungerImageView.drawable as ClipDrawable).colorFilter = PorterDuffColorFilter(Color.parseColor(viewModel.userPet.colorTheme), PorterDuff.Mode.SRC_IN)
        (binding.moodImageView.drawable as ClipDrawable).colorFilter = PorterDuffColorFilter(Color.parseColor(viewModel.userPet.colorTheme), PorterDuff.Mode.SRC_IN)
    }
    fun calculateOverallHealth(){
        val totalPoints = viewModel.userPet.mood + viewModel.userPet.hunger

        if(viewModel.userPet.mood == 0 || viewModel.userPet.hunger == 0){
            binding.statusImageView.setImageResource(R.drawable.sad)
            makePetSleep()
            if(viewModel.userPet.mood == 0 && viewModel.userPet.hunger == 0){
                binding.statusImageView.setImageResource(R.drawable.sleep)
                binding.sleepGif.visibility = View.VISIBLE
            }
            else binding.sleepGif.visibility = View.GONE
        }
        else {
            binding.sleepGif.visibility = View.GONE
            when (totalPoints) {
                in 0..70 -> {
                    binding.statusImageView.setImageResource(R.drawable.sad)
                    makePetSleep()
                }
                in 71..140 -> {
                    binding.statusImageView.setImageResource(R.drawable.neutral)
                    makePetStand()
                }
                in 141..200 -> {
                    binding.statusImageView.setImageResource(R.drawable.happy)
                    makePetStand()
                    if (viewModel.userPet.mood == 100 && viewModel.userPet.hunger == 100) {
                        binding.statusImageView.setImageResource(R.drawable.love)
                        if (viewModel.userPet.petResourceString.indexOf("dog") != -1) {
                            binding.dogHeartsGif.visibility = View.VISIBLE
                            binding.catHeartsGif.visibility = View.GONE
                        }
                        else{
                            binding.catHeartsGif.visibility = View.VISIBLE
                            binding.dogHeartsGif.visibility = View.GONE

                        }
                    }
                    else {
                        binding.dogHeartsGif.visibility = View.GONE
                        binding.catHeartsGif.visibility = View.GONE
                    }
                }
            }
        }
    }
    fun playSound(sound: String){
        when (sound) {
            "woof" -> mediaPlayer = MediaPlayer.create(context, R.raw.woof)
            "meow" -> mediaPlayer = MediaPlayer.create(context, R.raw.meow)
            "food" -> mediaPlayer = MediaPlayer.create(context, R.raw.eating)
            "water" -> mediaPlayer = MediaPlayer.create(context, R.raw.drinking)
            "play" -> mediaPlayer = MediaPlayer.create(context, R.raw.silence)
        }
        mediaPlayer.setOnCompletionListener {
            binding.hungerGif.visibility = View.GONE
            binding.tongueImageView.visibility = View.GONE
            binding.playGif.visibility = View.GONE
            mediaPlayer.reset()
        }
        mediaPlayer.start()
    }
    fun makePetStand(){
        binding.petImageView.visibility = View.VISIBLE
        binding.sleepImageView.visibility = View.INVISIBLE
        binding.petImageView.setImageResource(resources.getIdentifier(viewModel.standingPetResource, "drawable", context?.packageName))
    }
    fun makePetSleep(){
        binding.petImageView.visibility = View.INVISIBLE
        binding.sleepImageView.visibility = View.VISIBLE
        binding.sleepImageView.setImageResource(resources.getIdentifier(viewModel.sleepingPetResource, "drawable", context?.packageName))
    }
    override fun signOutAndNavigate() {
        viewModel.signOut()
        binding.root.findNavController().navigate(R.id.action_petFragment_to_startFragment)
    }
    override fun onDestroyView() {
        try {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
                mediaPlayer.release()
            }
        } catch (e: IllegalStateException) {
            Log.i("HELP", e.toString())
        }
        super.onDestroyView()
        _binding = null
    }
}