package com.agueguen.journal

class JournalEntry(val titre: String, val contenu: String, val date: String, val tag: Boolean) {
	override fun toString(): String {
		return "$titre - $date"
	}
}