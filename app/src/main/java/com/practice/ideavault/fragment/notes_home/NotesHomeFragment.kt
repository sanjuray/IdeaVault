package com.practice.ideavault.fragment.notes_home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.practice.ideavault.MainActivity
import com.practice.ideavault.databinding.FragmentNotesHomeBinding
import com.practice.ideavault.fragment.notes.NotesFragment

class NotesHomeFragment : Fragment() {

    private lateinit var binding: FragmentNotesHomeBinding
    private lateinit var notesHomeAdapter: NotesHomeAdapter

    private val notesHomeViewModel: NotesHomeViewModel by lazy {
        ViewModelProvider(this)[NotesHomeViewModel::class.java]
    }

    companion object {
        fun newInstance(): NotesHomeFragment {
            val fragment = NotesHomeFragment()
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        activity?.onBackPressedDispatcher?.addCallback(this,object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                isEnabled = false
                goBack()
            }
        })
    }

    fun goBack(){
        (context as MainActivity).makeToast(requireContext(),"See you soon!")
        (context as MainActivity).exitApplication()
//        activity?.finish()
    }

    private fun initViewModel(){
        notesHomeViewModel.init(requireContext(),(context as MainActivity).getLoggedInUserEmail(),(context as MainActivity).getLoggedInUserName())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotesHomeBinding.inflate(inflater)

        setGreetingsText()

        initRecyclerView()

        setOnClickListeners()

        return binding.root
    }

    private fun setGreetingsText(){
        val greeting = "Hello 👋\n${notesHomeViewModel.getLoggedInUserName()}"
        binding.tvUserGreetingsTextView.text = greeting
    }

    private fun setOnClickListeners(){
        binding.ivUserSignOffImageView.setOnClickListener{
            (context as MainActivity).signOut()
        }

        binding.btAddNotesButton.setOnClickListener{
            (context as MainActivity).loadFragment(NotesFragment.newInstance(null))
        }
    }

    private fun initRecyclerView(){
        binding.rvNotesHomeRecyclerView.layoutManager = LinearLayoutManager(context)
         notesHomeAdapter = NotesHomeAdapter(
            notesHomeViewModel.getListOfNotes()
            ,callback ={
                (context as MainActivity).loadFragment(NotesFragment.newInstance(it))
        })

        binding.rvNotesHomeRecyclerView.adapter = notesHomeAdapter
    }


}