package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "store_config")
data class StoreConfigEntity(
    @PrimaryKey
    val id: Int = 1,
    val storeName: String = "ALL IN ONE",
    val tagline: String = "Apna Supermarket & Express Delivery",
    val address: String = "Ragadi, Jajpur Road, Odisha - 755019",
    val storeLat: Double = 20.9507,
    val storeLng: Double = 86.1344,
    val phone: String = "+919437123456",
    val whatsappPhone: String = "919437123456",
    val upiId: String = "allinone.ragadi@okaxis",
    val upiName: String = "ALL IN ONE STORE",
    val maxDeliveryRadiusKm: Double = 15.0,
    val baseDeliveryFee: Double = 20.0,
    val baseDistanceKm: Double = 3.0,
    val perKmFee: Double = 5.0,
    val freeDeliveryMinAmount: Double = 499.0,
    val adminPin: String = "1234"
)
