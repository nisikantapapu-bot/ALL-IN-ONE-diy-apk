package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.entity.ProductEntity
import com.example.ui.components.DeliveryLocationSheet
import com.example.ui.components.DownloadAppSheet
import com.example.ui.components.StoreHeader
import com.example.ui.components.SupportSheet
import com.example.ui.components.dialPhoneNumber
import com.example.ui.components.openWhatsApp
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.CartScreen
import com.example.ui.screens.CustomerProfileScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.OrderHistoryScreen
import com.example.ui.screens.ProductDetailDialog
import com.example.ui.screens.PublishingGuideDialog
import com.example.ui.theme.FreshGreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.SaffronDark
import com.example.ui.theme.SaffronPrimary
import com.example.ui.viewmodel.ShopViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AllInOneApp()
            }
        }
    }
}

enum class ScreenTab {
    HOME, CART, ORDERS, PROFILE, ADMIN
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllInOneApp(viewModel: ShopViewModel = viewModel()) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Observable ViewModel State
    val products by viewModel.filteredProducts.collectAsStateWithLifecycle()
    val categories by viewModel.allCategories.collectAsStateWithLifecycle()
    val allOrders by viewModel.allOrders.collectAsStateWithLifecycle()
    val storeConfig by viewModel.storeConfig.collectAsStateWithLifecycle()
    val customerProfile by viewModel.customerProfile.collectAsStateWithLifecycle()
    val wishlistIds by viewModel.wishlistIds.collectAsStateWithLifecycle()
    val cart by viewModel.cart.collectAsStateWithLifecycle()
    val cartDetails by viewModel.cartDetails.collectAsStateWithLifecycle()
    val cartItemCount by viewModel.cartItemCount.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val isAdminLoggedIn by viewModel.isAdminLoggedIn.collectAsStateWithLifecycle()

    // Active Tab Navigation
    var currentTab by remember { mutableStateOf(ScreenTab.HOME) }

    // Bottom Sheets State
    var showLocationSheet by remember { mutableStateOf(false) }
    val locationSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var showSupportSheet by remember { mutableStateOf(false) }
    val supportSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var selectedProductForDetail by remember { mutableStateOf<ProductEntity?>(null) }
    val productDetailSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var showGuideSheet by remember { mutableStateOf(false) }
    val guideSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var showDownloadSheet by remember { mutableStateOf(false) }
    val downloadSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Admin PIN Dialog
    var showAdminLoginDialog by remember { mutableStateOf(false) }
    var enteredAdminPin by remember { mutableStateOf("") }
    var adminPinError by remember { mutableStateOf(false) }

    // Order Success Dialog
    var placedOrderId by remember { mutableStateOf<String?>(null) }

    // Hardware back press handling
    BackHandler(enabled = currentTab != ScreenTab.HOME) {
        if (currentTab == ScreenTab.ADMIN && isAdminLoggedIn) {
            currentTab = ScreenTab.HOME
        } else {
            currentTab = ScreenTab.HOME
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (currentTab != ScreenTab.ADMIN) {
                StoreHeader(
                    storeConfig = storeConfig,
                    customerProfile = customerProfile,
                    onLocationClick = { showLocationSheet = true },
                    onCallClick = { dialPhoneNumber(context, storeConfig.phone) },
                    onWhatsAppClick = { openWhatsApp(context, storeConfig.whatsappPhone, null) },
                    onAdminToggleClick = {
                        if (isAdminLoggedIn) {
                            currentTab = ScreenTab.ADMIN
                        } else {
                            showAdminLoginDialog = true
                        }
                    },
                    isAdmin = isAdminLoggedIn
                )
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp
            ) {
                NavigationBarItem(
                    selected = currentTab == ScreenTab.HOME,
                    onClick = { currentTab = ScreenTab.HOME },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == ScreenTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                            contentDescription = "Home"
                        )
                    },
                    label = { Text("Home", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SaffronPrimary,
                        selectedTextColor = SaffronPrimary,
                        indicatorColor = SaffronPrimary.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_item_home")
                )

                NavigationBarItem(
                    selected = currentTab == ScreenTab.CART,
                    onClick = { currentTab = ScreenTab.CART },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (cartItemCount > 0) {
                                    Badge(
                                        containerColor = SaffronPrimary,
                                        contentColor = Color.White
                                    ) {
                                        Text("$cartItemCount")
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (currentTab == ScreenTab.CART) Icons.Filled.ShoppingCart else Icons.Outlined.ShoppingCart,
                                contentDescription = "Cart"
                            )
                        }
                    },
                    label = { Text("Cart", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SaffronPrimary,
                        selectedTextColor = SaffronPrimary,
                        indicatorColor = SaffronPrimary.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_item_cart")
                )

                NavigationBarItem(
                    selected = currentTab == ScreenTab.ORDERS,
                    onClick = { currentTab = ScreenTab.ORDERS },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == ScreenTab.ORDERS) Icons.Filled.Receipt else Icons.Outlined.Receipt,
                            contentDescription = "Orders"
                        )
                    },
                    label = { Text("Orders", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SaffronPrimary,
                        selectedTextColor = SaffronPrimary,
                        indicatorColor = SaffronPrimary.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_item_orders")
                )

                NavigationBarItem(
                    selected = currentTab == ScreenTab.PROFILE,
                    onClick = { currentTab = ScreenTab.PROFILE },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == ScreenTab.PROFILE) Icons.Filled.Person else Icons.Outlined.Person,
                            contentDescription = "Profile"
                        )
                    },
                    label = { Text("Profile", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SaffronPrimary,
                        selectedTextColor = SaffronPrimary,
                        indicatorColor = SaffronPrimary.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_item_profile")
                )

                if (isAdminLoggedIn) {
                    NavigationBarItem(
                        selected = currentTab == ScreenTab.ADMIN,
                        onClick = { currentTab = ScreenTab.ADMIN },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.AdminPanelSettings,
                                contentDescription = "Admin"
                            )
                        },
                        label = { Text("Admin", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SaffronDark,
                            selectedTextColor = SaffronDark,
                            indicatorColor = SaffronDark.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.testTag("nav_item_admin")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                ScreenTab.HOME -> {
                    HomeScreen(
                        products = products,
                        categories = categories,
                        cart = cart,
                        wishlistIds = wishlistIds,
                        searchQuery = searchQuery,
                        selectedCategory = selectedCategory,
                        onSearchChange = viewModel::setSearchQuery,
                        onCategorySelect = viewModel::setSelectedCategory,
                        onProductClick = { prod ->
                            selectedProductForDetail = prod
                        },
                        onAddToCart = { prodId ->
                            viewModel.addToCart(prodId, 1)
                            Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show()
                        },
                        onIncreaseQty = { prodId, newQty ->
                            viewModel.updateCartQuantity(prodId, newQty)
                        },
                        onDecreaseQty = { prodId, newQty ->
                            viewModel.updateCartQuantity(prodId, newQty)
                        },
                        onToggleWishlist = viewModel::toggleWishlist,
                        onSupportClick = { showSupportSheet = true },
                        onDownloadAppClick = { showDownloadSheet = true }
                    )
                }

                ScreenTab.CART -> {
                    CartScreen(
                        cartItems = cartDetails,
                        storeConfig = storeConfig,
                        customerProfile = customerProfile,
                        onIncreaseQty = { prodId, newQty ->
                            viewModel.updateCartQuantity(prodId, newQty)
                        },
                        onDecreaseQty = { prodId, newQty ->
                            viewModel.updateCartQuantity(prodId, newQty)
                        },
                        onRemoveItem = viewModel::removeFromCart,
                        onChangeLocationClick = { showLocationSheet = true },
                        onPlaceOrder = { method, upiRef ->
                            viewModel.placeOrder(
                                paymentMethod = method,
                                upiRef = upiRef,
                                onSuccess = { orderId ->
                                    placedOrderId = orderId
                                    currentTab = ScreenTab.ORDERS
                                },
                                onError = { msg ->
                                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                }
                            )
                        },
                        onContinueShopping = { currentTab = ScreenTab.HOME }
                    )
                }

                ScreenTab.ORDERS -> {
                    OrderHistoryScreen(
                        orders = allOrders,
                        storeConfig = storeConfig,
                        onUpdateUtr = { orderId, utr ->
                            viewModel.updateOrderPayment(orderId, "Verification Pending", utr)
                            Toast.makeText(context, "UPI reference updated for #$orderId", Toast.LENGTH_SHORT).show()
                        },
                        onContinueShopping = { currentTab = ScreenTab.HOME }
                    )
                }

                ScreenTab.PROFILE -> {
                    CustomerProfileScreen(
                        profile = customerProfile,
                        storeConfig = storeConfig,
                        onUpdateProfile = viewModel::updateCustomerProfile,
                        onAdminLoginClick = {
                            if (isAdminLoggedIn) {
                                currentTab = ScreenTab.ADMIN
                            } else {
                                showAdminLoginDialog = true
                            }
                        },
                        onOpenGuideClick = { showGuideSheet = true },
                        onDownloadAppClick = { showDownloadSheet = true },
                        onChangeLocationClick = { showLocationSheet = true }
                    )
                }

                ScreenTab.ADMIN -> {
                    AdminDashboardScreen(
                        products = products,
                        categories = categories,
                        orders = allOrders,
                        storeConfig = storeConfig,
                        onSaveProduct = viewModel::saveProduct,
                        onDeleteProduct = viewModel::deleteProduct,
                        onSaveCategory = viewModel::saveCategory,
                        onDeleteCategory = viewModel::deleteCategory,
                        onUpdateOrderStatus = viewModel::updateOrderStatus,
                        onUpdateOrderPayment = viewModel::updateOrderPayment,
                        onUpdateStoreConfig = viewModel::updateStoreConfig,
                        onExitAdmin = {
                            viewModel.logoutAdmin()
                            currentTab = ScreenTab.HOME
                        }
                    )
                }
            }
        }
    }

    // Modal Sheet 1: Delivery Location Selector
    if (showLocationSheet) {
        DeliveryLocationSheet(
            sheetState = locationSheetState,
            storeConfig = storeConfig,
            currentProfile = customerProfile,
            onSaveLocation = { updated ->
                viewModel.updateCustomerProfile(updated)
                Toast.makeText(context, "Delivery location set to ${updated.selectedArea}", Toast.LENGTH_SHORT).show()
            },
            onDismiss = { showLocationSheet = false }
        )
    }

    // Modal Sheet 2: Customer Support (WhatsApp / Phone)
    if (showSupportSheet) {
        SupportSheet(
            sheetState = supportSheetState,
            storeConfig = storeConfig,
            onDismiss = { showSupportSheet = false }
        )
    }

    // Modal Sheet 3: Product Detail Dialog
    selectedProductForDetail?.let { prod ->
        val currentQty = cart[prod.id] ?: 0
        val isWishlisted = wishlistIds.contains(prod.id)

        ProductDetailDialog(
            product = prod,
            sheetState = productDetailSheetState,
            isWishlisted = isWishlisted,
            currentCartQty = currentQty,
            onAddToCart = { qty ->
                viewModel.addToCart(prod.id, qty)
                Toast.makeText(context, "Added $qty of ${prod.name} to cart", Toast.LENGTH_SHORT).show()
            },
            onBuyNow = { qty ->
                viewModel.addToCart(prod.id, qty)
                currentTab = ScreenTab.CART
            },
            onToggleWishlist = { viewModel.toggleWishlist(prod.id) },
            onDismiss = { selectedProductForDetail = null }
        )
    }

    // Modal Sheet 4: Publishing & Architecture Guide
    if (showGuideSheet) {
        PublishingGuideDialog(
            sheetState = guideSheetState,
            onDismiss = { showGuideSheet = false }
        )
    }

    // Modal Sheet 5: App Download (APK) & Share Sheet
    if (showDownloadSheet) {
        DownloadAppSheet(
            sheetState = downloadSheetState,
            onDismiss = { showDownloadSheet = false }
        )
    }

    // Admin PIN Login Dialog
    if (showAdminLoginDialog) {
        AlertDialog(
            onDismissRequest = {
                showAdminLoginDialog = false
                adminPinError = false
                enteredAdminPin = ""
            },
            title = {
                Text("Shop Owner Admin Login", fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    Text(
                        text = "Enter your secret Admin PIN to access inventory, orders, and sales reports.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = enteredAdminPin,
                        onValueChange = {
                            if (it.length <= 6) {
                                enteredAdminPin = it
                                adminPinError = false
                            }
                        },
                        label = { Text("Admin PIN (Default: 1234)") },
                        visualTransformation = PasswordVisualTransformation(),
                        isError = adminPinError,
                        supportingText = {
                            if (adminPinError) {
                                Text("Incorrect PIN. Please try again (Default: 1234)", color = MaterialTheme.colorScheme.error)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val success = viewModel.loginAdmin(enteredAdminPin.trim())
                        if (success) {
                            showAdminLoginDialog = false
                            enteredAdminPin = ""
                            adminPinError = false
                            currentTab = ScreenTab.ADMIN
                            Toast.makeText(context, "Welcome to ALL IN ONE Admin Panel", Toast.LENGTH_SHORT).show()
                        } else {
                            adminPinError = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
                ) {
                    Text("Unlock Panel")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showAdminLoginDialog = false
                    enteredAdminPin = ""
                    adminPinError = false
                }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Order Success Dialog
    placedOrderId?.let { orderId ->
        AlertDialog(
            onDismissRequest = { placedOrderId = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = FreshGreen,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Order Placed Successfully!", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    Text(
                        text = "Your order #$orderId has been received by ALL IN ONE Store, Ragadi.",
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "We deliver within 15 km in Jajpur Road. You can track preparation and dispatch live in the 'Orders' tab or contact the store directly on WhatsApp.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        placedOrderId = null
                        currentTab = ScreenTab.ORDERS
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
                ) {
                    Text("Track Order Live")
                }
            }
        )
    }
}
