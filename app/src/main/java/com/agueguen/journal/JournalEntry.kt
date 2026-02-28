package com.agueguen.journal

class JournalEntry(val id: Int, val title: String, val content: String, val date: String, val tag: Boolean) {
	override fun toString(): String {
		return "$title - $date"
	}
}