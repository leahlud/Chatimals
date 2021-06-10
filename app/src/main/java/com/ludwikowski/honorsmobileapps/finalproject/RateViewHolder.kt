package com.ludwikowski.honorsmobileapps.finalproject

import android.graphics.drawable.ClipDrawable
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.ludwikowski.honorsmobileapps.finalproject.databinding.RateItemLayoutBinding


class RateViewHolder(val binding: RateItemLayoutBinding, val viewModel: AppViewModel): RecyclerView.ViewHolder(binding.root) {
    private lateinit var currentRateItem: RateItem

    init {
        setClickListener()
    }

    fun setClickListener() {
        binding.goRateTextView.setOnClickListener{
            viewModel.setUserToRate(currentRateItem)
            val dialogFragment = RateDialogFragment()
            val activity = itemView.context as AppCompatActivity
            dialogFragment.show(activity.supportFragmentManager, null)
        }
    }

    fun bindRateItem(rateItem: RateItem, position: Int){
        setAnim()
        currentRateItem = rateItem
        binding.ratePetNameTextView.text=rateItem.petName
        binding.rateUserTagTextView.text="@${rateItem.userName}"
        if(currentRateItem.rating==0.0) binding.placeTextView.text="?"
        else binding.placeTextView.text="${position+4}"
        val res1 = currentRateItem.petRes.substring(currentRateItem.petRes.indexOf("_")+1)
        val res2 = res1.substring(res1.indexOf("_")+1)
        binding.rateFurImageView.setImageResource(itemView.context.resources.getIdentifier(currentRateItem.petRes.substring(0, currentRateItem.petRes.indexOf("_")), "drawable", itemView.context?.packageName))
        binding.rateCollarImageView.setImageResource(itemView.context.resources.getIdentifier(res2.substring(0, res2.indexOf("_")), "drawable", itemView.context?.packageName))
        binding.rateItemPetImageView.setImageResource(itemView.context.resources.getIdentifier(currentRateItem.petRes+"_circle", "drawable", itemView.context?.packageName))


    }
    fun setAnim(){
        binding.ratePetNameTextView.animation= AnimationUtils.loadAnimation(itemView.context, R.anim.rate_fade)
        binding.rateUserTagTextView.animation= AnimationUtils.loadAnimation(itemView.context, R.anim.rate_fade)
        binding.rateItemPetImageView.animation= AnimationUtils.loadAnimation(itemView.context, R.anim.rate_fade)
        binding.rateFurImageView.animation= AnimationUtils.loadAnimation(itemView.context, R.anim.rate_fade)
        binding.rateFurImageView2.animation= AnimationUtils.loadAnimation(itemView.context, R.anim.rate_fade)
        binding.rateCollarImageView.animation= AnimationUtils.loadAnimation(itemView.context, R.anim.rate_fade)
        binding.rateCollarImageView2.animation= AnimationUtils.loadAnimation(itemView.context, R.anim.rate_fade)
        binding.rateFurTextView.animation= AnimationUtils.loadAnimation(itemView.context, R.anim.rate_fade)
        binding.rateCollarTextView.animation= AnimationUtils.loadAnimation(itemView.context, R.anim.rate_fade)
        binding.goRateTextView.animation= AnimationUtils.loadAnimation(itemView.context, R.anim.rate_fade)

    }
}