package com.datasoft.abs.data.dto.config

data class ProductConfig(
    val categoryId: Int = 0,
    val id: Int,
    val name: String = ""
) {
    override fun toString(): String {
        return name
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductConfig

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}