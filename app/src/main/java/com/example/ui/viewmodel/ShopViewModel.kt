package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.CustomerProfileEntity
import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.StoreConfigEntity
import com.example.data.repository.ShopRepository
import com.example.data.sample.SampleData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

data class CartItemDetail(
    val product: ProductEntity,
    val quantity: Int
) {
    val total: Double get() = product.price * quantity
}

class ShopViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val repository = ShopRepository(database.shopDao())

    init {
        viewModelScope.launch {
            repository.initializeIfEmpty()
        }
    }

    val allProducts: StateFlow<List<ProductEntity>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCategories: StateFlow<List<CategoryEntity>> = repository.allCategories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allOrders: StateFlow<List<OrderEntity>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val storeConfig: StateFlow<StoreConfigEntity> = repository.storeConfig
        .map { it ?: SampleData.defaultStoreConfig }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SampleData.defaultStoreConfig)

    val customerProfile: StateFlow<CustomerProfileEntity> = repository.customerProfile
        .map { it ?: SampleData.defaultCustomer }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SampleData.defaultCustomer)

    val wishlistIds: StateFlow<Set<String>> = repository.wishlist
        .map { list -> list.map { it.productId }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    // Cart State: ProductId -> Quantity
    private val _cart = MutableStateFlow<Map<String, Int>>(emptyMap())
    val cart: StateFlow<Map<String, Int>> = _cart.asStateFlow()

    // Filter & Search
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    // Admin Authentication State
    private val _isAdminLoggedIn = MutableStateFlow(false)
    val isAdminLoggedIn: StateFlow<Boolean> = _isAdminLoggedIn.asStateFlow()

    // Filtered Products for customer home & catalog
    val filteredProducts: StateFlow<List<ProductEntity>> = combine(
        allProducts,
        _searchQuery,
        _selectedCategory
    ) { products, query, cat ->
        products.filter { prod ->
            val matchesCategory = cat == null || prod.category.equals(cat, ignoreCase = true)
            val matchesQuery = query.isBlank() ||
                    prod.name.contains(query, ignoreCase = true) ||
                    prod.category.contains(query, ignoreCase = true) ||
                    prod.description.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Cart Details with product objects
    val cartDetails: StateFlow<List<CartItemDetail>> = combine(allProducts, _cart) { products, cartMap ->
        val productMap = products.associateBy { it.id }
        cartMap.mapNotNull { (productId, qty) ->
            productMap[productId]?.let { CartItemDetail(it, qty) }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cartSubtotal: StateFlow<Double> = cartDetails.map { items ->
        items.sumOf { it.total }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val cartItemCount: StateFlow<Int> = _cart.map { map ->
        map.values.sum()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Actions
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategory(category: String?) {
        _selectedCategory.value = category
    }

    fun addToCart(productId: String, quantityToAdd: Int = 1) {
        val current = _cart.value.toMutableMap()
        val currentQty = current[productId] ?: 0
        current[productId] = currentQty + quantityToAdd
        _cart.value = current
    }

    fun updateCartQuantity(productId: String, newQuantity: Int) {
        val current = _cart.value.toMutableMap()
        if (newQuantity <= 0) {
            current.remove(productId)
        } else {
            current[productId] = newQuantity
        }
        _cart.value = current
    }

    fun removeFromCart(productId: String) {
        val current = _cart.value.toMutableMap()
        current.remove(productId)
        _cart.value = current
    }

    fun clearCart() {
        _cart.value = emptyMap()
    }

    fun toggleWishlist(productId: String) {
        viewModelScope.launch {
            repository.toggleWishlist(productId)
        }
    }

    fun updateCustomerProfile(profile: CustomerProfileEntity) {
        viewModelScope.launch {
            repository.updateCustomerProfile(profile)
        }
    }

    fun updateStoreConfig(config: StoreConfigEntity) {
        viewModelScope.launch {
            repository.updateStoreConfig(config)
        }
    }

    fun loginAdmin(pin: String): Boolean {
        val currentPin = storeConfig.value.adminPin
        return if (pin == currentPin) {
            _isAdminLoggedIn.value = true
            true
        } else {
            false
        }
    }

    fun logoutAdmin() {
        _isAdminLoggedIn.value = false
    }

    // Place Order with Radius & Stock Check
    fun placeOrder(
        paymentMethod: String,
        upiRef: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val items = cartDetails.value
            if (items.isEmpty()) {
                onError("Your cart is empty")
                return@launch
            }

            val config = storeConfig.value
            val profile = customerProfile.value

            // 15 km Radius Validation
            if (!repository.isWithinDeliveryRadius(profile.distanceKm, config.maxDeliveryRadiusKm)) {
                onError("Delivery location (${String.format("%.1f", profile.distanceKm)} km) is outside our ${config.maxDeliveryRadiusKm.toInt()} km delivery radius from Ragadi, Jajpur Road.")
                return@launch
            }

            // Check stock
            for (item in items) {
                if (item.product.stockQuantity < item.quantity) {
                    onError("Sorry, only ${item.product.stockQuantity} units of '${item.product.name}' are available in stock.")
                    return@launch
                }
            }

            val subtotal = items.sumOf { it.total }
            val deliveryFee = repository.calculateDeliveryFee(profile.distanceKm, subtotal, config)
            val grandTotal = subtotal + deliveryFee

            val orderCode = "AIO-" + (10000 + (Math.random() * 89999).toInt())
            val summaryText = items.joinToString(", ") { "${it.product.name} (${it.product.unit}) x${it.quantity}" }

            val itemsJsonBuilder = StringBuilder("[")
            items.forEachIndexed { index, item ->
                val escapedName = item.product.name.replace("\"", "\\\"")
                itemsJsonBuilder.append("""{"name":"$escapedName","unit":"${item.product.unit}","price":${item.product.price},"quantity":${item.quantity}}""")
                if (index < items.size - 1) itemsJsonBuilder.append(",")
            }
            itemsJsonBuilder.append("]")

            val paymentStatus = if (paymentMethod == "UPI") {
                if (upiRef.isNotBlank()) "Verification Pending" else "Pending Payment"
            } else {
                "Pending"
            }

            val order = OrderEntity(
                id = orderCode,
                customerName = profile.name,
                customerPhone = profile.phone,
                customerAddress = "${profile.address}, ${profile.selectedArea}",
                landmark = profile.landmark,
                deliveryDistanceKm = profile.distanceKm,
                itemsSummary = summaryText,
                itemsJson = itemsJsonBuilder.toString(),
                subtotal = subtotal,
                deliveryFee = deliveryFee,
                totalAmount = grandTotal,
                paymentMethod = paymentMethod,
                paymentStatus = paymentStatus,
                upiTransactionRef = upiRef,
                orderStatus = "Pending",
                orderTime = System.currentTimeMillis(),
                statusNote = "Order received by ALL IN ONE Store, Ragadi. Awaiting store confirmation."
            )

            val stockDeductions = items.map { it.product.id to it.quantity }
            repository.placeOrder(order, stockDeductions)
            clearCart()
            onSuccess(orderCode)
        }
    }

    // Admin Operations
    fun updateOrderStatus(orderId: String, status: String, note: String) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, status, note)
        }
    }

    fun updateOrderPayment(orderId: String, paymentStatus: String, utr: String) {
        viewModelScope.launch {
            repository.updateOrderPayment(orderId, paymentStatus, utr)
        }
    }

    fun saveProduct(product: ProductEntity) {
        viewModelScope.launch {
            repository.insertProduct(product)
        }
    }

    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            repository.deleteProduct(productId)
        }
    }

    fun saveCategory(name: String, iconName: String) {
        viewModelScope.launch {
            val id = "cat_" + name.lowercase().replace(" ", "_").replace("&", "and")
            val count = allCategories.value.size
            repository.insertCategory(CategoryEntity(id, name, iconName, count + 1))
        }
    }

    fun deleteCategory(categoryId: String) {
        viewModelScope.launch {
            repository.deleteCategory(categoryId)
        }
    }
}
