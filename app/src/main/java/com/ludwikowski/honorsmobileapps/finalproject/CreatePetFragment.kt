package com.ludwikowski.honorsmobileapps.finalproject

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.text.bold
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.ludwikowski.honorsmobileapps.finalproject.databinding.FragmentCreatePetBinding

class CreatePetFragment : Fragment(), ConfirmPetDialogFragment.OnConfirmButtonSelected {

    private val viewModel: AppViewModel by activityViewModels()

    private var _binding: FragmentCreatePetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCreatePetBinding.inflate(inflater, container, false)
        val rootView = binding.root

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            setErrorMessage(error)
        }
        viewModel.petCreated.observe(viewLifecycleOwner) { status ->
            checkPetStatus(status)
        }

        val animalButtons = listOf(binding.dogButton, binding.catButton)
        for (button in animalButtons) button.setOnClickListener { view: View ->
            when (view.id) {
                R.id.dog_button -> {
                    view.setBackgroundResource(R.drawable.clicked_button_tint)
                    binding.catButton.setTextColor(Color.parseColor("#4A4A4A"))
                    binding.dogButton.setTextColor(Color.parseColor("#FFFFFF"))
                    binding.catButton.setBackgroundResource(R.drawable.circular_shape)
                    viewModel.setSelectedAnimal("dog")
                }
                R.id.cat_button -> {
                    view.setBackgroundResource(R.drawable.clicked_button_tint)
                    binding.catButton.setTextColor(Color.parseColor("#FFFFFF"))
                    binding.dogButton.setTextColor(Color.parseColor("#4A4A4A"))
                    binding.dogButton.setBackgroundResource(R.drawable.circular_shape)
                    viewModel.setSelectedAnimal("cat")
                }
            }
        }
        val colorsList = listOf(binding.redCollarImageView, binding.orangeCollarImageView, binding.pinkCollarImageView, binding.greenCollarImageView, binding.blueCollarImageView, binding.purpleCollarImageView)
        for(button in colorsList) button.setOnClickListener{ v: View ->
            when(v.id){
                R.id.redCollar_imageView -> {
                    setColors(0)
                    viewModel.setSelectedCollarColor("red")
                    binding.colorTextView.setTextColor(Color.parseColor("#FF7373"))
                }
                R.id.orangeCollar_imageView -> {
                    setColors(1)
                    viewModel.setSelectedCollarColor("orange")
                    binding.colorTextView.setTextColor(Color.parseColor("#FFAA7A"))
                }
                R.id.pinkCollar_imageView -> {
                    setColors(2)
                    viewModel.setSelectedCollarColor("pink")
                    binding.colorTextView.setTextColor(Color.parseColor("#FFA3E5"))
                }
                R.id.greenCollar_imageView -> {
                    setColors(3)
                    viewModel.setSelectedCollarColor("green")
                    binding.colorTextView.setTextColor(Color.parseColor("#6ADCA0"))
                }
                R.id.blueCollar_imageView -> {
                    setColors(4)
                    viewModel.setSelectedCollarColor("blue")
                    binding.colorTextView.setTextColor(Color.parseColor("#83D1FF"))
                }
                R.id.purpleCollar_imageView -> {
                    setColors(5)
                    viewModel.setSelectedCollarColor("purple")
                    binding.colorTextView.setTextColor(Color.parseColor("#C1AEFF"))
                }
            }
            binding.colorTextView.text="${viewModel.selectedCollarColor.substring(0,1).toUpperCase()}${viewModel.selectedCollarColor.substring(1)}"
        }

        val furButtons = listOf<Button>(binding.blackRadioButton, binding.tanRadioButton, binding.whiteRadioButton)
        for (button in furButtons) button.setOnClickListener { view: View ->
            when (view.id) {
                R.id.black_radioButton -> {
                    viewModel.setSelectedFurColor("black")
                }
                R.id.tan_radioButton -> viewModel.setSelectedFurColor("tan")
                R.id.white_radioButton -> viewModel.setSelectedFurColor("white")
            }
        }

        binding.createPetButton.setOnClickListener {
            viewModel.checkPetCreation(binding.petNameEditText.text.toString())
        }

        return rootView
    }

    fun setErrorMessage(error: String) {
        if (error != "") binding.createPetErrorTextView.text = SpannableStringBuilder().bold { append("Error:") }.append("\n$error")
        else binding.createPetErrorTextView.text = ""
    }

    fun checkPetStatus(status: Boolean) {
        if (status) {
            viewModel.setEnteredPetname(binding.petNameEditText.text.toString())
            val dialog = ConfirmPetDialogFragment()
            dialog.setTargetFragment(this, 1)
            parentFragmentManager?.let { dialog.show(it, "MyCustomDialog") }
        }
    }

    fun setColors(index: Int){
        val outlineList = listOf(binding.outlineImageView1, binding.outlineImageView2, binding.outlineImageView3, binding.outlineImageView4, binding.outlineImageView5, binding.outlineImageView6)
        for(view in outlineList) view.visibility=View.GONE
        val colorButtonList = listOf(binding.redCollarImageView, binding.orangeCollarImageView, binding.pinkCollarImageView, binding.greenCollarImageView, binding.blueCollarImageView, binding.purpleCollarImageView)
        for(current in colorButtonList.indices) {
            if(current == index){
                val id = resources.getIdentifier("outline_imageView${index+1}", "id", context?.packageName)
                val outlineView = binding.root.findViewById<ImageView>(id)
                outlineView.visibility=View.VISIBLE
            }
        }

    }

    override fun navigateAndCreatePet() {
        viewModel.createPet(binding.petNameEditText.text.toString())
        viewModel.setMoodCountDownTimer()
        viewModel.setHungerCountDownTimer()
        viewModel.getRateList()
        viewModel.getChatList()
        binding.root.findNavController().navigate(R.id.action_createPetFragment_to_petFragment)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}