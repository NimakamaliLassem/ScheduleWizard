package com.schedulewizard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.database.Note

class NotesAdapter(private var notesList: List<Note>) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notesList[position]
        holder.titleTextView.text = note.title
        holder.typeTextView.text = note.type
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    fun addNote(note: Note) {
        notesList = notesList.toMutableList().apply { add(note) }
        notifyDataSetChanged()
    }

    fun getNoteAtPosition(position: Int): Note? {
        return if (position in 0 until notesList.size) {
            notesList[position]
        } else {
            null
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.title_text_view)
        val typeTextView: TextView = itemView.findViewById(R.id.type_text_view)
    }
}
