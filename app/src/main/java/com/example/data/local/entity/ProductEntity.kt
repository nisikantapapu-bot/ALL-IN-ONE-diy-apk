package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val category: String,
    val price: Double,
    val mrp: Double,
    val unit: String,
    val stockQuantity: Int,
    val description: String,
    val imageUrl: String = "",
    val iconType: String = "grocery", // grocery, produce, household, care, electronic, clothing, snack
    val isAvailable: Boolean = true,
    val isFeatured: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
) {
    val discountPercent: Int
        get() = if (mrp > price && mrp > 0) (((mrp - price) / mrp) * 100).toInt() else 0
}
