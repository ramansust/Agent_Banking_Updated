package com.datasoft.abs.data.dto.config

data class DocumentConfigData (
	val id : Int,
	val name : String = "",
	val isBackRequired : Boolean = false,
	val isIssueDateRequired : Boolean = false,
	val isExpiryDateRequired : Boolean = false
) {
	override fun toString(): String {
		return name
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as DocumentConfigData

		if (id != other.id) return false

		return true
	}

	override fun hashCode(): Int {
		return id
	}
}