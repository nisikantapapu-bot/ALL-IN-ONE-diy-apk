package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey
    val id: String, // e.g. "AIO-84920"
    val customerName: String,
    val customerPhone: String,
    val customerAddress: String,
    val landmark: String,
    val deliveryDistanceKm: Double,
    val itemsSummary: String, // Human readable summary
    val itemsJson: String, // Serialized list of items
    val subtotal: Double,
    val deliveryFee: Double,
    val totalAmount: Double,
    val paymentMethod: String, // "COD" or "UPI"
    val paymentStatus: String, // "Pending", "Verified", "Paid on Delivery"
    val upiTransactionRef: String = "", // UTR reference entered by user
    val orderStatus: String, // "Pending", "Confirmed", "Preparing", "Out for Delivery", "Delivered", "Cancelled"
    val orderTime: Long = System.currentTimeMillis(),
    val statusNote: String = ""
)
