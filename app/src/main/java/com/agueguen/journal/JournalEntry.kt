package com.agueguen.journal

class JournalEntry(val title: String, val content: String, val date: String, val tag: Boolean) {
	override fun toString(): String {
		return "$title - $date"
	}
}