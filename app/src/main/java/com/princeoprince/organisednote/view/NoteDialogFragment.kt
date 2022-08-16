package com.princeoprince.organisednote.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.DialogFragment
import com.princeoprince.organisednote.R
import com.princeoprince.organisednote.databinding.NotePopupBinding
import com.princeoprince.organisednote.model.Note
import com.princeoprince.organisednote.utils.KEY_NOTE

class NoteDialogFragment : DialogFragment() {
    private lateinit var listener: NoticeNoteDialogListener
    private lateinit var notePopupBinding: NotePopupBinding

    interface NoticeNoteDialogListener {
        fun onNoteDialogPositiveClick(note: Note, isEdited: Boolean)
        fun onNoteDialogNegativeClick(note: Note)
    }

    companion object {
        fun newInstance(
            note: Note? = null, listener: NoticeNoteDialogListener
        ) : NoteDialogFragment {
            val bundle = Bundle()
            val noteDialogFragment = NoteDialogFragment()
            bundle.putSerializable(KEY_NOTE, note)
            noteDialogFragment.arguments = bundle
            noteDialogFragment.listener = listener
            return noteDialogFragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val note = arguments?.getSerializable(KEY_NOTE) as? Note ?: Note()

        val builder = AlertDialog.Builder(requireActivity())
        notePopupBinding = NotePopupBinding.inflate(LayoutInflater.from(context))

        builder.setView(notePopupBinding.root)

        val isExistingNote = note.fileName !== ""

        notePopupBinding.edtFileName.isEnabled = !isExistingNote
        notePopupBinding.edtFileName.setText(note.fileName)
        notePopupBinding.edtNoteText.setText(note.noteText)
        notePopupBinding.notePriority.setSelection(
            if (note.priority == 4) 0 else note.priority
        )

        builder.setTitle(getString(
            if (isExistingNote) R.string.edit_note_title else R.string.add_note_title
        ))

        if (isExistingNote) {
            builder.setNegativeButton(R.string.text_delete) { _, _ ->
                listener.onNoteDialogNegativeClick(note)
            }
        }

        notePopupBinding.notePriority.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        note.priority = if (position <= 0) 4 else position
                }

                override fun onNothingSelected(parent: AdapterView<*>?) { }
            }

        builder.setPositiveButton(R.string.text_save) { _, _ ->
            note.noteText = notePopupBinding.edtNoteText.text.toString()
            note.fileName = notePopupBinding.edtFileName.text.toString()
            listener.onNoteDialogPositiveClick(note, isExistingNote)
        }.setNeutralButton(R.string.text_cancel) { dialog, _ -> dialog.dismiss() }

        return builder.create()
    }
}