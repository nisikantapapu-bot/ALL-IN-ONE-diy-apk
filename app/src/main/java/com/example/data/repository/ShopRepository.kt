package com.example.data.repository

import com.example.data.local.dao.ShopDao
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.CustomerProfileEntity
import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.StoreConfigEntity
import com.example.data.local.entity.WishlistEntity
import com.example.data.sample.SampleData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class ShopRepository(private val shopDao: ShopDao) {

    val allProducts: Flow<List<ProductEntity>> = shopDao.getAllProducts()
    val allCategories: Flow<List<CategoryEntity>> = shopDao.getAllCategories()
    val allOrders: Flow<List<OrderEntity>> = shopDao.getAllOrders()
    val storeConfig: Flow<StoreConfigEntity?> = shopDao.getStoreConfig()
    val customerProfile: Flow<CustomerProfileEntity?> = shopDao.getCustomerProfile()
    val wishlist: Flow<List<WishlistEntity>> = shopDao.getWishlist()

    suspend fun initializeIfEmpty() = withContext(Dispatchers.IO) {
        val existingConfig = shopDao.getStoreConfigSync()
        if (existingConfig == null) {
            shopDao.insertOrUpdateConfig(SampleData.defaultStoreConfig)
        }
        val existingCategories = shopDao.getAllCategories().firstOrNull()
        if (existingCategories.isNullOrEmpty()) {
            shopDao.insertCategories(SampleData.initialCategories)
        }
        val existingProducts = shopDao.getAllProducts().firstOrNull()
        if (existingProducts.isNullOrEmpty()) {
            shopDao.insertProducts(SampleData.initialProducts)
        }
        val existingProfile = shopDao.getCustomerProfile().firstOrNull()
        if (existingProfile == null) {
            shopDao.insertOrUpdateProfile(SampleData.defaultCustomer)
        }
        val existingOrders = shopDao.getAllOrders().firstOrNull()
        if (existingOrders.isNullOrEmpty()) {
            for (order in SampleData.sampleOrders) {
                shopDao.insertOrder(order)
            }
        }
    }

    suspend fun insertProduct(product: ProductEntity) = withContext(Dispatchers.IO) {
        shopDao.insertProduct(product)
    }

    suspend fun updateProduct(product: ProductEntity) = withContext(Dispatchers.IO) {
        shopDao.updateProduct(product)
    }

    suspend fun deleteProduct(productId: String) = withContext(Dispatchers.IO) {
        shopDao.deleteProductById(productId)
    }

    suspend fun insertCategory(category: CategoryEntity) = withContext(Dispatchers.IO) {
        shopDao.insertCategory(category)
    }

    suspend fun deleteCategory(categoryId: String) = withContext(Dispatchers.IO) {
        shopDao.deleteCategoryById(categoryId)
    }

    suspend fun placeOrder(order: OrderEntity, itemsToDeduct: List<Pair<String, Int>>) = withContext(Dispatchers.IO) {
        shopDao.insertOrder(order)
        for ((productId, qty) in itemsToDeduct) {
            shopDao.deductStock(productId, qty)
        }
    }

    suspend fun updateOrderStatus(orderId: String, status: String, note: String) = withContext(Dispatchers.IO) {
        shopDao.updateOrderStatus(orderId, status, note)
    }

    suspend fun updateOrderPayment(orderId: String, paymentStatus: String, utr: String) = withContext(Dispatchers.IO) {
        shopDao.updateOrderPayment(orderId, paymentStatus, utr)
    }

    suspend fun updateStoreConfig(config: StoreConfigEntity) = withContext(Dispatchers.IO) {
        shopDao.insertOrUpdateConfig(config)
    }

    suspend fun updateCustomerProfile(profile: CustomerProfileEntity) = withContext(Dispatchers.IO) {
        shopDao.insertOrUpdateProfile(profile)
    }

    suspend fun toggleWishlist(productId: String) = withContext(Dispatchers.IO) {
        if (shopDao.isInWishlist(productId)) {
            shopDao.removeFromWishlist(productId)
        } else {
            shopDao.addToWishlist(WishlistEntity(productId))
        }
    }

    fun calculateDeliveryFee(
        distanceKm: Double,
        subtotal: Double,
        config: StoreConfigEntity
    ): Double {
        if (subtotal >= config.freeDeliveryMinAmount) return 0.0
        return if (distanceKm <= config.baseDistanceKm) {
            config.baseDeliveryFee
        } else {
            val extraKm = distanceKm - config.baseDistanceKm
            config.baseDeliveryFee + (extraKm * config.perKmFee)
        }
    }

    fun isWithinDeliveryRadius(distanceKm: Double, maxRadiusKm: Double): Boolean {
        return distanceKm <= maxRadiusKm
    }

    // Haversine distance formula in kilometers
    fun calculateHaversineDistance(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val r = 6371.0 // Earth radius in km
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }
}
