package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.CustomerProfileEntity
import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.StoreConfigEntity
import com.example.data.local.entity.WishlistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopDao {

    // --- Products ---
    @Query("SELECT * FROM products ORDER BY isFeatured DESC, createdAt DESC")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :productId LIMIT 1")
    suspend fun getProductById(productId: String): ProductEntity?

    @Query("SELECT * FROM products WHERE category = :category ORDER BY name ASC")
    fun getProductsByCategory(category: String): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Query("DELETE FROM products WHERE id = :productId")
    suspend fun deleteProductById(productId: String)

    @Query("UPDATE products SET stockQuantity = stockQuantity - :qty WHERE id = :productId AND stockQuantity >= :qty")
    suspend fun deductStock(productId: String, qty: Int)

    // --- Categories ---
    @Query("SELECT * FROM categories ORDER BY displayOrder ASC, name ASC")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<CategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)

    @Query("DELETE FROM categories WHERE id = :categoryId")
    suspend fun deleteCategoryById(categoryId: String)

    // --- Orders ---
    @Query("SELECT * FROM orders ORDER BY orderTime DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE customerPhone = :phone ORDER BY orderTime DESC")
    fun getOrdersByPhone(phone: String): Flow<List<OrderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)

    @Update
    suspend fun updateOrder(order: OrderEntity)

    @Query("UPDATE orders SET orderStatus = :status, statusNote = :note WHERE id = :orderId")
    suspend fun updateOrderStatus(orderId: String, status: String, note: String)

    @Query("UPDATE orders SET paymentStatus = :paymentStatus, upiTransactionRef = :utr WHERE id = :orderId")
    suspend fun updateOrderPayment(orderId: String, paymentStatus: String, utr: String)

    // --- Store Config ---
    @Query("SELECT * FROM store_config WHERE id = 1 LIMIT 1")
    fun getStoreConfig(): Flow<StoreConfigEntity?>

    @Query("SELECT * FROM store_config WHERE id = 1 LIMIT 1")
    suspend fun getStoreConfigSync(): StoreConfigEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateConfig(config: StoreConfigEntity)

    // --- Customer Profile ---
    @Query("SELECT * FROM customer_profile WHERE id = 1 LIMIT 1")
    fun getCustomerProfile(): Flow<CustomerProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: CustomerProfileEntity)

    // --- Wishlist ---
    @Query("SELECT * FROM wishlist")
    fun getWishlist(): Flow<List<WishlistEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWishlist(item: WishlistEntity)

    @Query("DELETE FROM wishlist WHERE productId = :productId")
    suspend fun removeFromWishlist(productId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM wishlist WHERE productId = :productId)")
    suspend fun isInWishlist(productId: String): Boolean
}
