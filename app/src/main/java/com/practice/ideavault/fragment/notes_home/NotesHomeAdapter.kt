package com.practice.ideavault.fragment.notes_home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.practice.ideavault.R
import com.practice.ideavault.databinding.LayoutNotesHomeItemBinding
import com.practice.ideavault.databinding.LayoutNotesHomeItemEmptyStateBinding
import com.practice.ideavault.model_class.Note

class NotesHomeAdapter(private val notesList: List<Note>, val callback: (note: Note) -> Unit):
    RecyclerView.Adapter<NotesHomeAdapter.ViewHolder>() {

    private val emptyState = 0
    private val nonEmptyState = 1

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun getItemViewType(position: Int): Int {
        if(notesList.isEmpty()) return emptyState
        return nonEmptyState
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(
            when(viewType){
                nonEmptyState -> R.layout.layout_notes_home_item
                else -> R.layout.layout_notes_home_item_empty_state
            },
            parent,
            false
        ))
    }

    override fun getItemCount(): Int {
        if(notesList.isEmpty()) {
            return 1
        }
        return notesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(getItemViewType(position)){
            emptyState -> bindDataToEmptyState(holder)
            else -> onBindData(holder, position)
        }
    }

    private fun bindDataToEmptyState(holder: ViewHolder){
    }

    private fun onBindData(holder: RecyclerView.ViewHolder, position: Int){
        val data = notesList[position]
        val bindingItem: LayoutNotesHomeItemBinding =
            DataBindingUtil.bind(holder.itemView)!!
        bindingItem.tvNotesHomeItemTitleTextView.text = data.notesTitle
        bindingItem.tvNotesHomeItemContentTextView.text  = data.notesContent
        bindingItem.root.setOnClickListener{
            callback(data)
        }
    }

}