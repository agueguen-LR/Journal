package com.agueguen.journal

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import com.agueguen.journal.databinding.ActivityMainBinding

class MainActivity : ComponentActivity() {
    val calendar: Calendar = Calendar.getInstance()
    val database = DatabaseHelper(this)
    var day = calendar.get(Calendar.DAY_OF_MONTH)
    var month = calendar.get(Calendar.MONTH)
    var year = calendar.get(Calendar.YEAR)
    var list = ArrayList<JournalEntry>()
    lateinit var adapter: JournalArrayAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        binding.button.setOnClickListener {
            database.insertData(
                "${binding.title.text}",
                "${binding.content.text}",
                "${day}/${month+1}/${year}",
                binding.tag.isChecked()
            )
            updateEntries(this.adapter)
        }
    }

    fun showDeleteConfirmationDialog(deletedElementId: Int) {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.confirm_delete, null)

        builder.setView(dialogView).setCancelable(true)
        val dialog = builder.create()
        dialogView.findViewById<Button>(R.id.yes).setOnClickListener {
            database.deleteData(deletedElementId)
            updateEntries(adapter)
            dialog.dismiss()
        }
        dialogView.findViewById<Button>(R.id.no).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun updateEntries(adapter: JournalArrayAdapter){
        database.getData(this.list, null, null)
        adapter.notifyDataSetChanged()
    }

}
