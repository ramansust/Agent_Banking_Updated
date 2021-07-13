package com.datasoft.abs.data.dto.config

data class DocumentConfigData (
	val id : Int,
	val name : String,
	val isBackRequired : Boolean = false,
	val isIssueDateRequired : Boolean,
	val isExpiryDateRequired : Boolean
) {
	override fun toString(): String {
		return name
	}
}