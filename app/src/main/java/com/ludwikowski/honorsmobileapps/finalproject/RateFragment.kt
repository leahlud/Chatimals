package com.ludwikowski.honorsmobileapps.finalproject

import android.graphics.drawable.ClipDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.ludwikowski.honorsmobileapps.finalproject.databinding.FragmentRateBinding


class RateFragment : Fragment() {

    private val viewModel: AppViewModel by activityViewModels()

    private var _binding: FragmentRateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRateBinding.inflate(inflater, container, false)
        val rootView = binding.root

        viewModel.setRateEventListener()

        viewModel.rateList.observe(viewLifecycleOwner) { newRateList ->
            val adapter = RateAdapter(newRateList, viewModel)
            binding.rateRecyclerView.adapter = adapter
        }
        viewModel.topThreeList.observe(viewLifecycleOwner){ newTopThreeList ->
            displayTopThree(newTopThreeList)
        }

        val viewList = listOf(binding.homeRateImageView, binding.chatRateImageView)
        for(view in viewList) view.setOnClickListener { v: View ->
            viewModel.getChatList()
            when (v.id) {
                R.id.homeRate_imageView -> rootView.findNavController().navigate(R.id.action_rateFragment_to_petFragment)
                R.id.chatRate_imageView -> rootView.findNavController().navigate(R.id.action_rateFragment_to_chatFragment)

            }
        }
        return rootView
    }
    fun displayTopThree(topThreeList: MutableList<RateItem>){
        if(topThreeList.size > 0){
            val firstPet = topThreeList[0]
            binding.firstPlaceImageView.setImageResource(resources.getIdentifier(firstPet.petRes, "drawable", context?.packageName))
            binding.pet1TextView.text=firstPet.petName
            binding.user1TextView.text="@${firstPet.userName}"
            binding.rate1Button.visibility=View.VISIBLE
            binding.white1ImageView.visibility=View.VISIBLE
            binding.star1ImageView.visibility=View.VISIBLE
            binding.starLines1ImageView.visibility=View.VISIBLE
            binding.goldImageView.visibility=View.VISIBLE
            (binding.star1ImageView.drawable as ClipDrawable).level = (firstPet.rating * 2000).toInt()
            binding.rate1Button.setOnClickListener{
                viewModel.setUserToRate(firstPet)
                goToDialog()
            }
        }
        else{
            binding.pet1TextView.text=""
            binding.user1TextView.text=""
            binding.firstPlaceImageView.setImageResource(R.drawable.question)
            binding.rate1Button.visibility=View.INVISIBLE
            binding.white1ImageView.visibility=View.INVISIBLE
            binding.star1ImageView.visibility=View.INVISIBLE
            binding.starLines1ImageView.visibility=View.INVISIBLE
        }
        if(topThreeList.size > 1){
            val secondPet = topThreeList[1]
            binding.secondPlaceImageView.setImageResource(resources.getIdentifier(secondPet.petRes, "drawable", context?.packageName))
            binding.pet2TextView.text=secondPet.petName
            binding.user2TextView.text="@${secondPet.userName}"
            binding.rate2Button.visibility=View.VISIBLE
            binding.white2ImageView.visibility=View.VISIBLE
            binding.star2ImageView.visibility=View.VISIBLE
            binding.starLines2ImageView.visibility=View.VISIBLE
            binding.silverImageView.visibility=View.VISIBLE
            (binding.star2ImageView.drawable as ClipDrawable).level = (secondPet.rating * 2000).toInt()
            binding.rate2Button.setOnClickListener{
                viewModel.setUserToRate(secondPet)
                goToDialog()
            }
        }
        else{
            binding.pet2TextView.text=""
            binding.user2TextView.text=""
            binding.secondPlaceImageView.setImageResource(R.drawable.question)
            binding.rate2Button.visibility=View.INVISIBLE
            binding.white2ImageView.visibility=View.INVISIBLE
            binding.star2ImageView.visibility=View.INVISIBLE
            binding.starLines2ImageView.visibility=View.INVISIBLE
        }
        if(topThreeList.size > 2){
            val thirdPet = topThreeList[2]
            binding.thirdPlaceImageView.setImageResource(resources.getIdentifier(thirdPet.petRes, "drawable", context?.packageName))
            binding.pet3TextView.text=thirdPet.petName
            binding.user3TextView.text="@${thirdPet.userName}"
            binding.rate3Button.visibility=View.VISIBLE
            binding.white3ImageView.visibility=View.VISIBLE
            binding.star3ImageView.visibility=View.VISIBLE
            binding.starLines3ImageView.visibility=View.VISIBLE
            binding.bronzeImageView.visibility=View.VISIBLE
            (binding.star3ImageView.drawable as ClipDrawable).level = (thirdPet.rating * 2000).toInt()
            binding.rate3Button.setOnClickListener{
                viewModel.setUserToRate(thirdPet)
                goToDialog()
            }
        }
        else{
            binding.pet3TextView.text=""
            binding.user3TextView.text=""
            binding.thirdPlaceImageView.setImageResource(R.drawable.question)
            binding.rate3Button.visibility=View.INVISIBLE
            binding.white3ImageView.visibility=View.INVISIBLE
            binding.star3ImageView.visibility=View.INVISIBLE
            binding.starLines3ImageView.visibility=View.INVISIBLE
        }
    }
    fun goToDialog(){
        val dialog = RateDialogFragment()
        dialog.setTargetFragment(this, 1)
        parentFragmentManager?.let { dialog.show(it, "RateCustomDialog") }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}