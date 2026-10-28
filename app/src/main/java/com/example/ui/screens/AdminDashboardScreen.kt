package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.StoreConfigEntity
import com.example.ui.components.dialPhoneNumber
import com.example.ui.components.openWhatsApp
import com.example.ui.theme.CobaltBlue
import com.example.ui.theme.FreshGreen
import com.example.ui.theme.SaffronDark
import com.example.ui.theme.SaffronPrimary
import com.example.ui.theme.StatusCancelled
import com.example.ui.theme.StatusConfirmed
import com.example.ui.theme.StatusDelivered
import com.example.ui.theme.StatusOutForDelivery
import com.example.ui.theme.StatusPending
import com.example.ui.theme.StatusPreparing
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@Composable
fun AdminDashboardScreen(
    products: List<ProductEntity>,
    categories: List<CategoryEntity>,
    orders: List<OrderEntity>,
    storeConfig: StoreConfigEntity,
    onSaveProduct: (ProductEntity) -> Unit,
    onDeleteProduct: (String) -> Unit,
    onSaveCategory: (String, String) -> Unit,
    onDeleteCategory: (String) -> Unit,
    onUpdateOrderStatus: (orderId: String, status: String, note: String) -> Unit,
    onUpdateOrderPayment: (orderId: String, paymentStatus: String, utr: String) -> Unit,
    onUpdateStoreConfig: (StoreConfigEntity) -> Unit,
    onExitAdmin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Orders, 1: Products, 2: Categories, 3: Delivery/Settings, 4: Reports

    val tabs = listOf("Orders", "Products", "Categories", "Delivery/Rates", "Sales Reports")

    // State for Add/Edit Product Dialog
    var showProductDialog by remember { mutableStateOf(false) }
    var editingProduct by remember { mutableStateOf<ProductEntity?>(null) }

    // State for Add Category Dialog
    var showCategoryDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Admin Top Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = SaffronDark,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Admin Panel",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.White.copy(alpha = 0.2f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Ragadi Store",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                    Text(
                        text = "Manage inventory, orders, radius & sales",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }

                Button(
                    onClick = onExitAdmin,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(34.dp).testTag("exit_admin_button")
                ) {
                    Text("Exit", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Sub Navigation Tabs
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            edgePadding = 12.dp,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = SaffronPrimary
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = when (index) {
                                0 -> "Orders (${orders.count { it.orderStatus != "Delivered" && it.orderStatus != "Cancelled" }})"
                                1 -> "Products (${products.size})"
                                else -> title
                            },
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    },
                    modifier = Modifier.testTag("admin_tab_$index")
                )
            }
        }

        // Tab Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            when (selectedTab) {
                0 -> AdminOrdersTab(
                    orders = orders,
                    onUpdateStatus = onUpdateOrderStatus,
                    onVerifyPayment = onUpdateOrderPayment
                )
                1 -> AdminProductsTab(
                    products = products,
                    categories = categories,
                    onAddClick = {
                        editingProduct = null
                        showProductDialog = true
                    },
                    onEditClick = { prod ->
                        editingProduct = prod
                        showProductDialog = true
                    },
                    onDeleteClick = onDeleteProduct
                )
                2 -> AdminCategoriesTab(
                    categories = categories,
                    onAddCategoryClick = { showCategoryDialog = true },
                    onDeleteCategory = onDeleteCategory
                )
                3 -> AdminDeliverySettingsTab(
                    config = storeConfig,
                    onSaveConfig = onUpdateStoreConfig
                )
                4 -> AdminSalesReportsTab(
                    orders = orders
                )
            }
        }
    }

    // Add / Edit Product Dialog
    if (showProductDialog) {
        ProductEditDialog(
            product = editingProduct,
            categories = categories,
            onDismiss = { showProductDialog = false },
            onSave = { prod ->
                onSaveProduct(prod)
                showProductDialog = false
            }
        )
    }

    // Add Category Dialog
    if (showCategoryDialog) {
        CategoryAddDialog(
            onDismiss = { showCategoryDialog = false },
            onSave = { name ->
                onSaveCategory(name, "category")
                showCategoryDialog = false
            }
        )
    }
}

// -------------------------------------------------------------
// TAB 0: Orders Management (Status Updates, Payment Verification, Call/WhatsApp)
// -------------------------------------------------------------
@Composable
fun AdminOrdersTab(
    orders: List<OrderEntity>,
    onUpdateStatus: (orderId: String, status: String, note: String) -> Unit,
    onVerifyPayment: (orderId: String, paymentStatus: String, utr: String) -> Unit
) {
    val context = LocalContext.current
    var statusFilter by remember { mutableStateOf("All") }

    val filteredOrders = remember(orders, statusFilter) {
        when (statusFilter) {
            "Pending" -> orders.filter { it.orderStatus == "Pending" }
            "Active" -> orders.filter { it.orderStatus in listOf("Pending", "Confirmed", "Preparing", "Out for Delivery") }
            "Delivered" -> orders.filter { it.orderStatus == "Delivered" }
            "COD" -> orders.filter { it.paymentMethod == "COD" }
            "UPI" -> orders.filter { it.paymentMethod == "UPI" }
            else -> orders
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Quick Filters
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("Active", "Pending", "Delivered", "UPI", "COD", "All").forEach { filter ->
                val isSel = statusFilter == filter
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSel) SaffronPrimary else MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { statusFilter = filter }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = filter,
                        fontSize = 11.sp,
                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSel) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (filteredOrders.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No orders found for '$statusFilter'", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            return
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredOrders, key = { it.id }) { order ->
                AdminOrderCard(
                    order = order,
                    onStatusChange = { newStatus ->
                        val defaultNote = when (newStatus) {
                            "Confirmed" -> "Order accepted by ALL IN ONE Store."
                            "Preparing" -> "Packing items at Ragadi store."
                            "Out for Delivery" -> "Dispatched with delivery partner."
                            "Delivered" -> "Delivered to customer."
                            "Cancelled" -> "Cancelled by store."
                            else -> ""
                        }
                        onUpdateStatus(order.id, newStatus, defaultNote)
                    },
                    onVerifyPayment = {
                        onVerifyPayment(order.id, "Verified", order.upiTransactionRef.ifBlank { "VERIFIED-BY-ADMIN" })
                        Toast.makeText(context, "Payment marked as Verified for #${order.id}", Toast.LENGTH_SHORT).show()
                    },
                    onCallCustomer = { dialPhoneNumber(context, order.customerPhone) },
                    onWhatsAppCustomer = { openWhatsApp(context, order.customerPhone, order.id) }
                )
            }
        }
    }
}

@Composable
fun AdminOrderCard(
    order: OrderEntity,
    onStatusChange: (String) -> Unit,
    onVerifyPayment: () -> Unit,
    onCallCustomer: () -> Unit,
    onWhatsAppCustomer: () -> Unit
) {
    val dateFormatter = remember { SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: ID + Total
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Order #${order.id}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = SaffronDark)
                    )
                    Text(
                        text = dateFormatter.format(Date(order.orderTime)),
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "₹${order.totalAmount.toInt()}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
                    )
                    Text(
                        text = "${order.paymentMethod} • ${order.paymentStatus}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (order.paymentStatus.contains("Verified") || order.paymentStatus.contains("Paid")) FreshGreen else SaffronPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Customer info
            Text(
                text = "👤 ${order.customerName} (+91 ${order.customerPhone})",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
            Text(
                text = "📍 ${order.customerAddress} (${order.deliveryDistanceKm} km from Ragadi store)",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (order.landmark.isNotBlank()) {
                Text(text = "Landmark: ${order.landmark}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "🛒 Items: ${order.itemsSummary}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (order.upiTransactionRef.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "UPI Reference (UTR): ${order.upiTransactionRef}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CobaltBlue
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Divider()
            Spacer(modifier = Modifier.height(8.dp))

            // Status Selector Chips
            Text(
                text = "Update Status: Current [${order.orderStatus}]",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOf("Confirmed", "Preparing", "Out for Delivery", "Delivered", "Cancelled").forEach { status ->
                    val isCurrent = order.orderStatus.equals(status, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (isCurrent) SaffronPrimary else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .clickable { onStatusChange(status) }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = status.take(8),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isCurrent) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Action buttons: Verify UPI + Contact Customer
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (order.paymentMethod == "UPI" && !order.paymentStatus.contains("Verified")) {
                    Button(
                        onClick = onVerifyPayment,
                        colors = ButtonDefaults.buttonColors(containerColor = FreshGreen),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Verify UPI", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                Row {
                    OutlinedButton(
                        onClick = onCallCustomer,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Call", fontSize = 11.sp)
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Button(
                        onClick = onWhatsAppCustomer,
                        colors = ButtonDefaults.buttonColors(containerColor = FreshGreen),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Icon(imageVector = Icons.Outlined.Chat, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("WhatsApp", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// TAB 1: Products Management (Add, Edit, Delete, Stock & Price)
// -------------------------------------------------------------
@Composable
fun AdminProductsTab(
    products: List<ProductEntity>,
    categories: List<CategoryEntity>,
    onAddClick: () -> Unit,
    onEditClick: (ProductEntity) -> Unit,
    onDeleteClick: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Store Inventory (${products.size} Products)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            Button(
                onClick = onAddClick,
                colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("admin_add_product_button")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Product", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(products, key = { it.id }) { product ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = product.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = product.category,
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "₹${product.price.toInt()} (MRP ₹${product.mrp.toInt()}) • Unit: ${product.unit}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SaffronDark
                            )
                            Text(
                                text = "Stock: ${product.stockQuantity} units • ${if (product.stockQuantity > 0) "Available" else "OUT OF STOCK"}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (product.stockQuantity > 0) FreshGreen else MaterialTheme.colorScheme.error
                            )
                        }

                        Row {
                            IconButton(
                                onClick = { onEditClick(product) },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", tint = SaffronPrimary, modifier = Modifier.size(18.dp))
                            }
                            IconButton(
                                onClick = { onDeleteClick(product.id) },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// TAB 2: Categories Management
// -------------------------------------------------------------
@Composable
fun AdminCategoriesTab(
    categories: List<CategoryEntity>,
    onAddCategoryClick: () -> Unit,
    onDeleteCategory: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Product Categories (${categories.size})",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            Button(
                onClick = onAddCategoryClick,
                colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("New Category", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories, key = { it.id }) { cat ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Category,
                                contentDescription = null,
                                tint = SaffronPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = cat.name,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                        }

                        IconButton(
                            onClick = { onDeleteCategory(cat.id) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// TAB 3: Delivery System & Store Rates Configuration
// -------------------------------------------------------------
@Composable
fun AdminDeliverySettingsTab(
    config: StoreConfigEntity,
    onSaveConfig: (StoreConfigEntity) -> Unit
) {
    val context = LocalContext.current

    var maxRadius by remember(config.maxDeliveryRadiusKm) { mutableStateOf(config.maxDeliveryRadiusKm.toString()) }
    var baseFee by remember(config.baseDeliveryFee) { mutableStateOf(config.baseDeliveryFee.toString()) }
    var baseDistance by remember(config.baseDistanceKm) { mutableStateOf(config.baseDistanceKm.toString()) }
    var perKmFee by remember(config.perKmFee) { mutableStateOf(config.perKmFee.toString()) }
    var freeMinAmount by remember(config.freeDeliveryMinAmount) { mutableStateOf(config.freeDeliveryMinAmount.toString()) }
    var storePhone by remember(config.phone) { mutableStateOf(config.phone) }
    var storeWhatsapp by remember(config.whatsappPhone) { mutableStateOf(config.whatsappPhone) }
    var upiId by remember(config.upiId) { mutableStateOf(config.upiId) }
    var adminPin by remember(config.adminPin) { mutableStateOf(config.adminPin) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Delivery & Store Configuration",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "Configure 15 km delivery radius, rates per km, and store contacts",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(14.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(text = "Delivery Boundary & Rates", fontWeight = FontWeight.Bold, color = SaffronDark)
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = maxRadius,
                    onValueChange = { maxRadius = it },
                    label = { Text("Maximum Delivery Radius (KM)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = baseDistance,
                        onValueChange = { baseDistance = it },
                        label = { Text("Base Distance (KM)") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = baseFee,
                        onValueChange = { baseFee = it },
                        label = { Text("Base Fee (₹)") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = perKmFee,
                        onValueChange = { perKmFee = it },
                        label = { Text("Per KM Rate (₹)") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = freeMinAmount,
                        onValueChange = { freeMinAmount = it },
                        label = { Text("Free Delivery Above (₹)") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(text = "Store Contact & Payments", fontWeight = FontWeight.Bold, color = SaffronDark)
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = storePhone,
                    onValueChange = { storePhone = it },
                    label = { Text("Calling Phone (+91)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = storeWhatsapp,
                    onValueChange = { storeWhatsapp = it },
                    label = { Text("WhatsApp Phone") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = upiId,
                    onValueChange = { upiId = it },
                    label = { Text("Store UPI ID for Payments") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = adminPin,
                    onValueChange = { adminPin = it },
                    label = { Text("Admin Access PIN") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val updated = config.copy(
                    maxDeliveryRadiusKm = maxRadius.toDoubleOrNull() ?: 15.0,
                    baseDeliveryFee = baseFee.toDoubleOrNull() ?: 20.0,
                    baseDistanceKm = baseDistance.toDoubleOrNull() ?: 3.0,
                    perKmFee = perKmFee.toDoubleOrNull() ?: 5.0,
                    freeDeliveryMinAmount = freeMinAmount.toDoubleOrNull() ?: 499.0,
                    phone = storePhone,
                    whatsappPhone = storeWhatsapp,
                    upiId = upiId,
                    adminPin = adminPin.ifBlank { "1234" }
                )
                onSaveConfig(updated)
                Toast.makeText(context, "Settings saved successfully", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
        ) {
            Text("Save Delivery Settings", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// -------------------------------------------------------------
// TAB 4: Sales Analytics & Business Reports
// -------------------------------------------------------------
@Composable
fun AdminSalesReportsTab(
    orders: List<OrderEntity>
) {
    val totalRevenue = orders.filter { it.orderStatus != "Cancelled" }.sumOf { it.totalAmount }
    val deliveredCount = orders.count { it.orderStatus == "Delivered" }
    val activeOrdersCount = orders.count { it.orderStatus in listOf("Pending", "Confirmed", "Preparing", "Out for Delivery") }
    val pendingPaymentTotal = orders.filter { it.orderStatus != "Cancelled" && (it.paymentStatus == "Pending" || it.paymentStatus == "Verification Pending") }.sumOf { it.totalAmount }
    val upiOrdersTotal = orders.filter { it.paymentMethod == "UPI" && it.orderStatus != "Cancelled" }.sumOf { it.totalAmount }
    val codOrdersTotal = orders.filter { it.paymentMethod == "COD" && it.orderStatus != "Cancelled" }.sumOf { it.totalAmount }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Business Sales & Revenue Reports",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "Track total sales, pending payments & order metrics for ALL IN ONE Ragadi",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Big Revenue Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = SaffronPrimary)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(text = "Total Store Revenue", color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "₹${totalRevenue.toInt()}",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Across ${orders.size} customer orders placed in Ragadi & Jajpur Road",
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 2x2 Metrics Grid
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ReportMetricCard(
                title = "Delivered Orders",
                value = "$deliveredCount orders",
                subtitle = "Fulfilled successfully",
                color = FreshGreen,
                modifier = Modifier.weight(1f)
            )
            ReportMetricCard(
                title = "Active In-Flight",
                value = "$activeOrdersCount orders",
                subtitle = "Pending / Preparing",
                color = SaffronDark,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ReportMetricCard(
                title = "UPI Payments",
                value = "₹${upiOrdersTotal.toInt()}",
                subtitle = "Google Pay / PhonePe",
                color = CobaltBlue,
                modifier = Modifier.weight(1f)
            )
            ReportMetricCard(
                title = "COD Collections",
                value = "₹${codOrdersTotal.toInt()}",
                subtitle = "Cash on Delivery",
                color = Color(0xFF6A1B9A),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Pending Payments Box
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(text = "Pending Receivables / Verification", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "₹${pendingPaymentTotal.toInt()} awaiting door collection or UPI verification",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun ReportMetricCard(
    title: String,
    value: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = title, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = color)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = subtitle, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

// -------------------------------------------------------------
// Edit / Add Product Dialog
// -------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductEditDialog(
    product: ProductEntity?,
    categories: List<CategoryEntity>,
    onDismiss: () -> Unit,
    onSave: (ProductEntity) -> Unit
) {
    var name by remember { mutableStateOf(product?.name ?: "") }
    var category by remember { mutableStateOf(product?.category ?: categories.firstOrNull()?.name ?: "Groceries & Staples") }
    var price by remember { mutableStateOf(product?.price?.toInt()?.toString() ?: "100") }
    var mrp by remember { mutableStateOf(product?.mrp?.toInt()?.toString() ?: "120") }
    var unit by remember { mutableStateOf(product?.unit ?: "1 kg") }
    var stock by remember { mutableStateOf(product?.stockQuantity?.toString() ?: "20") }
    var description by remember { mutableStateOf(product?.description ?: "") }

    var expandedCatDropdown by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (product == null) "Add New Product" else "Edit Product", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Product Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Category selector
                ExposedDropdownMenuBox(
                    expanded = expandedCatDropdown,
                    onExpandedChange = { expandedCatDropdown = !expandedCatDropdown }
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCatDropdown) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedCatDropdown,
                        onDismissRequest = { expandedCatDropdown = false }
                    ) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat.name) },
                                onClick = {
                                    category = cat.name
                                    expandedCatDropdown = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        label = { Text("Selling Price (₹)") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = mrp,
                        onValueChange = { mrp = it },
                        label = { Text("MRP (₹)") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = unit,
                        onValueChange = { unit = it },
                        label = { Text("Unit / Pack Size") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = stock,
                        onValueChange = { stock = it },
                        label = { Text("Stock Quantity") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isBlank()) return@Button
                    val p = ProductEntity(
                        id = product?.id ?: ("prod_" + UUID.randomUUID().toString().take(8)),
                        name = name.trim(),
                        category = category,
                        price = price.toDoubleOrNull() ?: 50.0,
                        mrp = mrp.toDoubleOrNull() ?: 60.0,
                        unit = unit.ifBlank { "1 piece" },
                        stockQuantity = stock.toIntOrNull() ?: 10,
                        description = description.ifBlank { "Fresh authentic product from ALL IN ONE Ragadi." },
                        iconType = when {
                            category.contains("Household", ignoreCase = true) -> "household"
                            category.contains("Care", ignoreCase = true) -> "care"
                            category.contains("Electronic", ignoreCase = true) -> "electronic"
                            category.contains("Clothing", ignoreCase = true) -> "clothing"
                            category.contains("Snack", ignoreCase = true) -> "snack"
                            else -> "grocery"
                        }
                    )
                    onSave(p)
                },
                colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

// -------------------------------------------------------------
// Add Category Dialog
// -------------------------------------------------------------
@Composable
fun CategoryAddDialog(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var categoryName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Category", fontWeight = FontWeight.Bold) },
        text = {
            OutlinedTextField(
                value = categoryName,
                onValueChange = { categoryName = it },
                label = { Text("Category Name (e.g. Toys, Hardware)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (categoryName.isNotBlank()) {
                        onSave(categoryName.trim())
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
