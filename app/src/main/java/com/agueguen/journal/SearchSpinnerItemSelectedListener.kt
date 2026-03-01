package com.agueguen.journal

import android.view.View
import android.widget.AdapterView

class SearchSpinnerItemSelectedListener(val mainActivity: MainActivity) : AdapterView.OnItemSelectedListener{
    override fun onItemSelected(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ) {
        when (position) {
            //title
            0 -> {
                mainActivity.binding.searchBar.hint = mainActivity.resources.getString(R.string.search) +
                        " " + mainActivity.resources.getString(R.string.title)
                mainActivity.filterColumn = DatabaseHelper.COLUMNS.TITLE
            }

            //content
            1 -> {
                mainActivity.binding.searchBar.hint = mainActivity.resources.getString(R.string.search) +
                        " " + mainActivity.resources.getString(R.string.content)
                mainActivity.filterColumn = DatabaseHelper.COLUMNS.CONTENT
            }

            //date
            2 -> {
                mainActivity.binding.searchBar.hint = mainActivity.resources.getString(R.string.search) +
                        " Date (dd/mm/yyyy)"
                mainActivity.filterColumn = DatabaseHelper.COLUMNS.DATE
            }

            //tag
            3 -> {
                mainActivity.binding.searchBar.hint = mainActivity.resources.getString(R.string.search) +
                        " Tag (0/1)"
                mainActivity.filterColumn = DatabaseHelper.COLUMNS.TAG
            }
        }
        mainActivity.filterSelection = "${mainActivity.filterColumn} LIKE '%${mainActivity.binding.searchBar.text}%'"
        mainActivity.updateEntries(mainActivity.adapter, mainActivity.filterSelection, mainActivity.filterColumn)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        //nothing
    }
}