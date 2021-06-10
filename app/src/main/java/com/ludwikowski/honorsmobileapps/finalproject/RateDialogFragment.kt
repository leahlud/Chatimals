package com.ludwikowski.honorsmobileapps.finalproject

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.ludwikowski.honorsmobileapps.finalproject.databinding.DialogRateBinding


class RateDialogFragment : DialogFragment() {

    private val TAG = "RateCustomDialog"
    private var _binding: DialogRateBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AppViewModel by activityViewModels()
    private var rating: Double = 0.0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DialogRateBinding.inflate(inflater, container, false)
        val rootView = binding.root

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        for(rater in viewModel.userToRate.listOfRaters){
            if(rater.raterId == viewModel.userId) {
                setStarsGrey()
                val starList = listOf(binding.starImageView1, binding.starImageView2, binding.starImageView3, binding.starImageView4, binding.starImageView5)
                for (index in 0 until rater.raterRating.toInt()) starList[index].setImageResource(R.drawable.star_gold)
                binding.previousRatingTextView.visibility = View.VISIBLE
                binding.numberTextView.text = "${rater.raterRating.toInt()}/5"
            }
        }

        binding.rateTextView.text="Rate ${viewModel.userToRate.userName}'s pet:"
        binding.cancelRateTextView.setOnClickListener{
            dialog!!.dismiss()
        }
        binding.confirmRateTextView.setOnClickListener{
            if(rating == 0.0){
                binding.rateErrorTextView.visibility=View.VISIBLE
            }
            else{
                viewModel.addRateToDatabase(rating)

                dialog!!.dismiss()
            }
        }
        val viewList = listOf(binding.starImageView1, binding.starImageView2, binding.starImageView3, binding.starImageView4, binding.starImageView5)
        for (view in viewList) view.setOnClickListener{ v: View ->
            setStarsGrey()
            binding.previousRatingTextView.visibility = View.INVISIBLE
            binding.rateErrorTextView.visibility = View.INVISIBLE
            when(v.id){
                R.id.star_imageView1 -> {
                    binding.starImageView1.setImageResource(R.drawable.star_gold)
                    rating = 1.0
                }
                R.id.star_imageView2 -> {
                    for(index in 0 until viewList.size-3) viewList[index].setImageResource(R.drawable.star_gold)
                    rating = 2.0
                }
                R.id.star_imageView3 -> {
                    for(index in 0 until viewList.size-2) viewList[index].setImageResource(R.drawable.star_gold)
                    rating = 3.0
                }
                R.id.star_imageView4 -> {
                    for(index in 0 until viewList.size-1) viewList[index].setImageResource(R.drawable.star_gold)
                    rating = 4.0
                }
                R.id.star_imageView5 -> {
                    for(star in viewList) star.setImageResource(R.drawable.star_gold)
                    rating = 5.0
                }
            }
            binding.numberTextView.text="${rating.toInt()}/5"
        }

        return rootView
    }
    fun setStarsGrey(){
        val starViewList = listOf(binding.starImageView1, binding.starImageView2, binding.starImageView3, binding.starImageView4, binding.starImageView5)
        for(starView in starViewList) starView.setImageResource(R.drawable.star_grey)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}