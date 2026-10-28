package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer_profile")
data class CustomerProfileEntity(
    @PrimaryKey
    val id: Int = 1,
    val name: String = "Valued Customer",
    val phone: String = "9876543210",
    val address: String = "Ragadi College Road, Near Shiv Temple",
    val selectedArea: String = "Ragadi Bazar",
    val landmark: String = "Near High School",
    val distanceKm: Double = 1.2,
    val isLoggedIn: Boolean = true
)
