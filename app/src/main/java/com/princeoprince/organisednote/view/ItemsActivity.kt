package com.princeoprince.organisednote.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import androidx.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.princeoprince.organisednote.R
import com.princeoprince.organisednote.adapter.NoteAdapter
import com.princeoprince.organisednote.controller.NotePrefs
import com.princeoprince.organisednote.databinding.ActivityItemsBinding
import com.princeoprince.organisednote.databinding.DialogRadioGroupBinding
import com.princeoprince.organisednote.model.Note
import com.princeoprince.organisednote.utils.*

class ItemsActivity : AppCompatActivity(), NoteDialogFragment.NoticeNoteDialogListener {

    private lateinit var binding: ActivityItemsBinding

    private val notePrefs: NotePrefs by lazy {
        NotePrefs(PreferenceManager.getDefaultSharedPreferences(this))
    }

    private val priorities by lazy {
        notePrefs.getNotePriorityFilters().toMutableSet()
    }

    private val noteAdapter: NoteAdapter by lazy {
        NoteAdapter(
            this,
            priorities,
            notePrefs.getNoteSortOrder(),  // Read from preferences
            ::showNoteDialog)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = noteAdapter
        binding.fab.setOnClickListener { showNoteDialog() }

        changeNotesBackgroundColour()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_note, menu)

        when (notePrefs.getNoteSortOrder()) {
            NoteSortOrder.FILENAME_ASC ->
                menu.findItem(R.id.sort_by_filename_asc).isChecked = true
            NoteSortOrder.FILENAME_DESC ->
                menu.findItem(R.id.sort_by_filename_desc).isChecked = true
            NoteSortOrder.DATE_LAST_MOD_ASC ->
                menu.findItem(R.id.sort_by_date_last_modified_asc).isChecked = true
            NoteSortOrder.DATE_LAST_MOD_DESC ->
                menu.findItem(R.id.sort_by_date_last_modified_desc).isChecked = true
            NoteSortOrder.PRIORITY_ASC ->
                menu.findItem(R.id.sort_by_priority_asc).isChecked = true
            NoteSortOrder.PRIORITY_DESC ->
                menu.findItem(R.id.sort_by_priority_desc).isChecked = true
        }

        menu.findItem(R.id.priority_1).isChecked = priorities.contains(PRIORITY_ONE)
        menu.findItem(R.id.priority_2).isChecked = priorities.contains(PRIORITY_TWO)
        menu.findItem(R.id.priority_3).isChecked = priorities.contains(PRIORITY_THREE)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // Background colour preference selected
            R.id.change_note_background_color -> {
                showNoteBackgroundColourDialog()
                true
            }
            R.id.sort_by_date_last_modified_asc -> {
                item.isChecked = true
                updateNoteSortOrder(NoteSortOrder.DATE_LAST_MOD_ASC)
                true
            }
            R.id.sort_by_date_last_modified_desc -> {
                item.isChecked = true
                updateNoteSortOrder(NoteSortOrder.DATE_LAST_MOD_DESC)
                true
            }
            R.id.sort_by_filename_asc -> {
                item.isChecked = true
                updateNoteSortOrder(NoteSortOrder.FILENAME_ASC)
                true
            }
            R.id.sort_by_filename_desc -> {
                item.isChecked = true
                updateNoteSortOrder(NoteSortOrder.FILENAME_DESC)
                true
            }
            R.id.sort_by_priority_asc -> {
                item.isChecked = true
                updateNoteSortOrder(NoteSortOrder.PRIORITY_ASC)
                true
            }
            R.id.sort_by_priority_desc -> {
                item.isChecked = true
                updateNoteSortOrder(NoteSortOrder.PRIORITY_DESC)
                true
            }
            R.id.priority_1 -> {
                item.isChecked = !item.isChecked
                togglePriorityState(PRIORITY_ONE, item.isChecked)

                updateNotePrioritiesFilter(priorities)
                true
            }
            R.id.priority_2 -> {
                item.isChecked = !item.isChecked
                togglePriorityState(PRIORITY_TWO, item.isChecked)

                updateNotePrioritiesFilter(priorities)
                true
            }
            R.id.priority_3 -> {
                item.isChecked = !item.isChecked
                togglePriorityState(PRIORITY_THREE, item.isChecked)

                updateNotePrioritiesFilter(priorities)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun showNoteBackgroundColourDialog() {
        val dialog = Dialog(this)
        val binding = DialogRadioGroupBinding.inflate(layoutInflater)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(binding.root)

        val radioButtons = setOf(binding.greenOption, binding.orangeOption, binding.purpleOption)

        val currentBackgroundColour = getCurrentBackgroundColourString()
        binding.greenOption.isChecked = binding.greenOption.text == currentBackgroundColour
        binding.orangeOption.isChecked = binding.orangeOption.text == currentBackgroundColour
        binding.purpleOption.isChecked = binding.purpleOption.text == currentBackgroundColour

        binding.radioGroup.setOnCheckedChangeListener{ _, checkedId ->
            val selectedRadioButton = radioButtons.firstOrNull { it.id == checkedId }

            if (selectedRadioButton != null) {
                notePrefs.saveNoteBackgroundColour(selectedRadioButton.text.toString())
                changeNotesBackgroundColour()
            }

            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showNoteDialog(note: Note? = null) {
        val newFragment = NoteDialogFragment.newInstance(note, listener = this)
        newFragment.show(supportFragmentManager, getString(R.string.title_notes))
    }

    private fun changeNotesBackgroundColour() =
        window.decorView.setBackgroundResource(getCurrentBackgroundColourInt())

    private fun getCurrentBackgroundColourInt(): Int =
        notePrefs.getAppBackgroundColour().intColour

    private fun getCurrentBackgroundColourString() : String =
        notePrefs.getAppBackgroundColour().displayString


    override fun onNoteDialogPositiveClick(note: Note, isEdited: Boolean) {
        if (isEdited) noteAdapter.editNote(note)
        else {
            if (noteAdapter.addNote(note)) showToast(getString(R.string.note_add_success))
            else showToast(getString(R.string.file_exist))
        }
    }

    override fun onNoteDialogNegativeClick(note: Note) = noteAdapter.deleteNote(note.fileName)

    private fun updateNoteSortOrder(sortOrder: NoteSortOrder) {
        noteAdapter.updateNotesFilters(order = sortOrder)
        notePrefs.saveNoteSortOrder(sortOrder)
    }

    private fun updateNotePrioritiesFilter(priorities: Set<String>) {
        noteAdapter.updateNotesFilters(priorities = priorities)
        notePrefs.saveNotePriorityFilters(priorities)
    }

    private fun togglePriorityState(priority: String, isActive: Boolean) {
        if (isActive) priorities.add(priority)
        else priorities.remove(priority)

        updateNotePrioritiesFilter(priorities)
    }
}