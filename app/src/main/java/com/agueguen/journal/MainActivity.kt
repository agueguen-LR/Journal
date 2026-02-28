package com.agueguen.journal

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AlertDialog
import com.agueguen.journal.databinding.ActivityMainBinding

class MainActivity : ComponentActivity() {
    val calendar: Calendar = Calendar.getInstance()
    var day = calendar.get(Calendar.DAY_OF_MONTH)
    var month = calendar.get(Calendar.MONTH)
    var year = calendar.get(Calendar.YEAR)
    var list = ArrayList<JournalEntry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.date.setOnClickListener {
            val date: DatePickerDialog =
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
                )
            date.show()
        }

        val adapter =
            object :
                ArrayAdapter<JournalEntry>(
                    this,
                    R.layout.journal_entry,
                    R.id.textViewItem,
                    this.list,
                ) {
                override fun getView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup,
                ): View {
                    val view = super.getView(position, convertView, parent)
                    val clickContainer = view.findViewById<LinearLayout>(R.id.clickContainer)

                    clickContainer.setOnClickListener {
                        Log.i("MainActivity", "Short click at $position")
                        Toast
                            .makeText(
                                context,
                                "${super.getItem(position)?.titre}\n" +
                                        "${super.getItem(position)?.contenu}\n" +
                                        "${super.getItem(position)?.date}\n" +
                                        "${super.getItem(position)?.tag}\n",
                                LENGTH_LONG,
                            ).show()
                    }

                    clickContainer.setOnLongClickListener {
                        Log.i("MainActivity", "Long click at $position")
                        true
                    }

                    return view
                }
            }
        binding.list.adapter = adapter

        binding.button.setOnClickListener {
            this.list.add(
                JournalEntry(
                    "${binding.titre.text}",
                    "${binding.contenu.text}",
                    "${day}/${month+1}/${year}",
                    binding.tag.isChecked,
                ),
            )
            adapter.notifyDataSetChanged()
        }
    }

}
