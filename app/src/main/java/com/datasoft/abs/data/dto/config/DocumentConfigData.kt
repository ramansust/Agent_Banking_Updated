package com.datasoft.abs.data.dto.config

data class DocumentConfigData (
	val id : Int,
	val name : String,
	val isBackRequired : Boolean,
	val isIssueDateRequired : Boolean,
	val isExpiryDateRequired : Boolean
)