package com.ludwikowski.honorsmobileapps.finalproject

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.ludwikowski.honorsmobileapps.finalproject.databinding.RateItemLayoutBinding

class RateAdapter (val rateList: MutableList<RateItem>, val viewModel: AppViewModel): RecyclerView.Adapter<RateViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RateItemLayoutBinding.inflate(layoutInflater, parent, false)
        return RateViewHolder(binding, viewModel)
    }

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
        val rateItem = rateList[position]
        holder.bindRateItem(rateItem, position)
    }

    override fun getItemCount(): Int {
        return rateList.size
    }
}