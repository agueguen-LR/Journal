package com.agueguen.journal

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast

class JournalArrayAdapter(
    context: Context,
    resource: Int,
    textViewResourceId: Int,
    list: List<JournalEntry>
) : ArrayAdapter<JournalEntry>(context, resource, textViewResourceId, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val clickContainer = view.findViewById<LinearLayout>(R.id.clickContainer)

        clickContainer.setOnClickListener {
            val journalEntry = getItem(position)
            Log.i("JournalEntryAdapter", "Short click at $position")
            Toast.makeText(
                context,
                "${journalEntry?.title}\n${journalEntry?.content}\n${journalEntry?.date}\n${journalEntry?.tag}",
                Toast.LENGTH_LONG
            ).show()
        }

        clickContainer.setOnLongClickListener {
            Log.i("JournalEntryAdapter", "Long click at $position")
            val mainActivity = (this.context as MainActivity)
            val spinner = view.findViewById<Spinner>(R.id.spinner)
            val spinnerEntries = mainActivity.resources.getStringArray(R.array.spinner_entries)
            when (spinner.selectedItem) {
                // modify
                spinnerEntries[0] -> {

                }
                // edit
                spinnerEntries[1] -> {

                }
                // delete
                spinnerEntries[2] -> {
                    mainActivity.showDeleteConfirmationDialog(getItem(position)!!.id)
                }
            }
            true
        }

        return view
    }
}
