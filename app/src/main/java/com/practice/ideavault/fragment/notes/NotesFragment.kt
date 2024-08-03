package com.practice.ideavault.fragment.notes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.practice.ideavault.MainActivity
import com.practice.ideavault.database.login_database_helper.NotesDatabaseHelper
import com.practice.ideavault.databinding.FragmentNotesBinding
import com.practice.ideavault.model_class.Note

class NotesFragment : Fragment() {

    private lateinit var binding: FragmentNotesBinding
    private lateinit var note: Note

    private lateinit var notesDatabaseHelper: NotesDatabaseHelper

    companion object {
        fun newInstance(note: Note?): NotesFragment {
            val fragment = NotesFragment()
            if(note != null){
                val args = Bundle()
                args.putSerializable("Note",note)
                fragment.arguments = args
            }
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this,object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                //update the db
                updateDb()
                isEnabled = false
                goBack()
            }
        })

        notesDatabaseHelper = NotesDatabaseHelper(context as MainActivity)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentNotesBinding.inflate(inflater)
        if(arguments != null){
            note = arguments?.getSerializable("Note") as Note
            setNoteElement()
            Log.d("SGERROR",note.id.toString()+" "+note.notesTitle+" "+note.notesContent)
        }else{
            setOnlyEditedTime()
        }

        setOnClickListeners()

        return binding.root
    }

    private fun setOnClickListeners(){
        binding.ivBackButtonImageView.setOnClickListener{
            goBack()
        }
        binding.ivNotesDeleteImageView.setOnClickListener{
            val msg: String
            if(arguments != null){
                notesDatabaseHelper.deleteNote(note.id)
                msg = "Note is deleted"
            }else{
                msg = "Empty Note cannot be deleted"
            }
            (context as MainActivity).makeToast(requireContext(),msg)
            goBack()
        }
    }

    private fun goBack(){
        (context as MainActivity).onBackPressed()
    }

    private fun updateDb(){
        val title = binding.etNotesTitleEditText.text.toString()
        val content = binding.etNotesContentEditText.text.toString()
        val date = (context as MainActivity).getCurrentDate()
        if(arguments == null){
            note = Note(null,title.trim(),content.trim(),date)
            notesDatabaseHelper.insertNote(note,(context as MainActivity).getLoggedInUserEmail())
        }else{
            note.notesTitle = title.trim()
            note.notesContent = content.trim()
            note.notesEditTime = date
            notesDatabaseHelper.updateNote(note,(context as MainActivity).getLoggedInUserEmail())
        }
    }

    private fun setNoteElement(){
        binding.etNotesTitleEditText.setText(note.notesTitle)
        binding.etNotesContentEditText.setText(note.notesContent)
        val editTime = "Edited ${note.notesEditTime}"
        binding.tvNotesLastEditTimeTextView.text = editTime
    }

    private fun setOnlyEditedTime(){
        val editTime = "Edited ${(context as MainActivity).getCurrentDate()}"
        binding.tvNotesLastEditTimeTextView.text = editTime
    }

}