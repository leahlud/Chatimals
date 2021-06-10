package com.ludwikowski.honorsmobileapps.finalproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.ludwikowski.honorsmobileapps.finalproject.databinding.FragmentChatBinding


class ChatFragment : Fragment(){

    private val viewModel: AppViewModel by activityViewModels()

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        val rootView = binding.root

        setupSpinner()
        viewModel.setChatEventListener()
        viewModel.chatList.observe(viewLifecycleOwner) { newChatList ->
            setChats(newChatList)
        }
        val viewList = listOf(binding.newChatImageView, binding.homeChatImageView, binding.rateChatImageView)
        for(view in viewList) view.setOnClickListener{ v: View ->
            viewModel.getRateList()
            when(v.id){
                R.id.newChat_imageView -> {
                    val dialog = WriteChatDialogFragment()
                    dialog.setTargetFragment(this, 1)
                    parentFragmentManager?.let { dialog.show(it, "ChatCustomDialog") }
                }
                R.id.homeChat_imageView -> rootView.findNavController().navigate(R.id.action_chatFragment_to_petFragment)
                R.id.rateChat_imageView -> rootView.findNavController().navigate(R.id.action_chatFragment_to_rateFragment)
            }
        }
        return rootView
    }
    fun setupSpinner() {
        val sortStylesArrayAdapter = context?.let { ArrayAdapter.createFromResource(it, R.array.sort_styles_array, R.layout.sort_spinner_item) }
        sortStylesArrayAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.sortChatsSpinner.adapter = sortStylesArrayAdapter

        for(i in iterator<Int> { binding.sortChatsSpinner.adapter.count}){
            if(binding.sortChatsSpinner.adapter.getItem(i).equals(viewModel.chatSortStyle)){
                binding.sortChatsSpinner.setSelection(i);
            }
        }
        val itemSelectedListener: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, childView: View?, position: Int, itemId: Long) {
                viewModel.setChatSortStyle(adapterView.getItemAtPosition(position).toString())
            }
            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
        binding.sortChatsSpinner.onItemSelectedListener = itemSelectedListener
    }
    fun setChats(newChatList: MutableList<Chat>){
        binding.noChatsTextView.visibility=View.INVISIBLE
        if(newChatList.size == 0) binding.noChatsTextView.visibility=View.VISIBLE
        val adapter = ChatAdapter(newChatList, viewModel)
        binding.chatRecyclerView.adapter = adapter
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}