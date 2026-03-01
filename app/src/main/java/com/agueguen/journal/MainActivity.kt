package com.agueguen.journal

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.app.Activity
import com.agueguen.journal.databinding.ActivityMainBinding

class MainActivity : Activity() {
    val calendar: Calendar = Calendar.getInstance()
    lateinit var database : DatabaseHelper
    var day = calendar.get(Calendar.DAY_OF_MONTH)
    var month = calendar.get(Calendar.MONTH)
    var year = calendar.get(Calendar.YEAR)
    var list = ArrayList<JournalEntry>()
    var filterSelection : String? = null
    var filterColumn: DatabaseHelper.COLUMNS? = null
    lateinit var adapter: JournalArrayAdapter
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = DatabaseHelper(this)
        database.getData(list, null, null)

        binding.date.setOnClickListener {
            DatePickerDialog(
                this,
                { view, year, month, day ->
                    this.year = year
                    this.month = month
                    this.day = day
                    binding.date.text = "$day/${month + 1}/$year"
                },
                year,
                month,
                day,
            ).show()
        }

        this.adapter = JournalArrayAdapter(
            this,
            R.layout.journal_entry,
            R.id.textViewItem,
            this.list
        )
        binding.list.adapter = adapter

        binding.searchSpinner.onItemSelectedListener = SearchSpinnerItemSelectedListener(this)
        binding.searchBar.addTextChangedListener(SearchBarTextWatcher(this))

        binding.button.setOnClickListener {
            database.insertData(
                "${binding.title.text}",
                "${binding.content.text}",
                "${day}/${month+1}/${year}",
                binding.tag.isChecked
            )
            updateEntries(this.adapter, filterSelection, filterColumn)
        }
    }



    fun showDeleteConfirmationDialog(deletedElementId: Int) {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.confirm_delete, null)

        builder.setView(dialogView).setCancelable(true)
        val dialog = builder.create()
        dialogView.findViewById<Button>(R.id.yes).setOnClickListener {
            database.deleteData(deletedElementId)
            updateEntries(this.adapter, filterSelection, filterColumn)
            dialog.dismiss()
        }
        dialogView.findViewById<Button>(R.id.no).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


    fun showViewEntryDialog(id: Int) {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.view_journal_entry, null)

        builder.setView(dialogView).setCancelable(true)

        val entry = ArrayList<JournalEntry>()
        database.getData(entry,"id = $id", null)
        dialogView.findViewById<TextView>(R.id.view_title).text = entry[0].title
        dialogView.findViewById<TextView>(R.id.view_content).text = entry[0].content
        dialogView.findViewById<CheckBox>(R.id.view_tag).setChecked(entry[0].tag)
        val dialog = builder.create()
        dialogView.findViewById<Button>(R.id.view_exit_button).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


    fun showEditEntryDialog(id: Int) {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.edit_journal_entry, null)

        builder.setView(dialogView).setCancelable(true)

        val entry = ArrayList<JournalEntry>()
        database.getData(entry,"id = $id", null)
        val title = dialogView.findViewById<EditText>(R.id.edit_title)
        title.setText(entry[0].title)
        val content = dialogView.findViewById<EditText>(R.id.edit_content)
        content.setText(entry[0].content)
        val date = dialogView.findViewById<TextView>(R.id.edit_date)
        date.text = entry[0].date
        date.setOnClickListener {
            DatePickerDialog(
                this,
                { view, year, month, day ->
                    this.year = year
                    this.month = month
                    this.day = day
                    date.text = "$day/${month + 1}/$year"
                },
                year,
                month,
                day,
            ).show()
        }
        val tag = dialogView.findViewById<CheckBox>(R.id.edit_tag)
        tag.setChecked(entry[0].tag)
        val dialog = builder.create()
        dialogView.findViewById<Button>(R.id.edit_exit_button).setOnClickListener {
            database.editData(id,
                "${title.text}",
                "${content.text}",
                "${date.text}",
                tag.isChecked)
            updateEntries(this.adapter, filterSelection, filterColumn)
            Log.i("Main Activity", "$filterSelection")
            dialog.dismiss()
        }

        dialog.show()
    }

    fun updateEntries(adapter: JournalArrayAdapter, selection: String?, orderBy: DatabaseHelper.COLUMNS?){
        this.list.clear()
        database.getData(this.list, selection, orderBy)
        adapter.notifyDataSetChanged()
    }

}
