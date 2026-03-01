package com.agueguen.journal

import android.text.Editable
import android.text.TextWatcher

class SearchBarTextWatcher(val mainActivity: MainActivity) : TextWatcher
{
    override fun afterTextChanged(s: Editable?) {
        //nothing
    }

    override fun beforeTextChanged(
        s: CharSequence?,
        start: Int,
        count: Int,
        after: Int
    ) {
        //nothing
    }

    override fun onTextChanged(
        s: CharSequence?,
        start: Int,
        before: Int,
        count: Int
    ) {
        mainActivity.filterSelection = "${mainActivity.filterColumn} LIKE '%$s%'"
        mainActivity.updateEntries(
            mainActivity.adapter,
            mainActivity.filterSelection,
            mainActivity.filterColumn
        )
    }
}