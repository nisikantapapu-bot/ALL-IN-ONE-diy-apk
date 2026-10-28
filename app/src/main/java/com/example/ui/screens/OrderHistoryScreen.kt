package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.data.local.entity.OrderEntity
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

@Composable
fun OrderHistoryScreen(
    orders: List<OrderEntity>,
    storeConfig: StoreConfigEntity,
    onUpdateUtr: (orderId: String, utr: String) -> Unit,
    onContinueShopping: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedFilter by remember { mutableStateOf("All") } // "All", "Active", "Delivered", "Cancelled"

    val filteredOrders = remember(orders, selectedFilter) {
        when (selectedFilter) {
            "Active" -> orders.filter { it.orderStatus in listOf("Pending", "Confirmed", "Preparing", "Out for Delivery") }
            "Delivered" -> orders.filter { it.orderStatus == "Delivered" }
            "Cancelled" -> orders.filter { it.orderStatus == "Cancelled" }
            else -> orders
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "My Orders",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "${filteredOrders.size} orders",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Filter chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("All", "Active", "Delivered", "Cancelled").forEach { filter ->
                val isSelected = selectedFilter == filter
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            if (isSelected) SaffronPrimary else MaterialTheme.colorScheme.surfaceVariant
                        )
                        .clickable { selectedFilter = filter }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("order_filter_$filter")
                ) {
                    Text(
                        text = filter,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (filteredOrders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Receipt,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No $selectedFilter Orders Found",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Items you order from ALL IN ONE Ragadi will appear here with live tracking.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            return
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(filteredOrders, key = { it.id }) { order ->
                OrderCardItem(
                    order = order,
                    storeConfig = storeConfig,
                    onUpdateUtr = { utr -> onUpdateUtr(order.id, utr) },
                    onWhatsAppInquire = {
                        openWhatsApp(context, storeConfig.whatsappPhone, order.id)
                    },
                    onCallStore = {
                        dialPhoneNumber(context, storeConfig.phone)
                    }
                )
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun OrderCardItem(
    order: OrderEntity,
    storeConfig: StoreConfigEntity,
    onUpdateUtr: (String) -> Unit,
    onWhatsAppInquire: () -> Unit,
    onCallStore: () -> Unit
) {
    val dateFormatter = remember { SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()) }
    val formattedDate = remember(order.orderTime) { dateFormatter.format(Date(order.orderTime)) }

    var isEditingUtr by remember { mutableStateOf(false) }
    var utrInput by remember { mutableStateOf(order.upiTransactionRef) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("order_item_${order.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: ID + Total
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Order #${order.id}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = SaffronDark
                        )
                    )
                    Text(
                        text = formattedDate,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "₹${order.totalAmount.toInt()}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        text = "${order.paymentMethod} • ${order.paymentStatus}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (order.paymentStatus.contains("Verified")) FreshGreen else SaffronPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Order Status Stepper
            OrderStatusTracker(currentStatus = order.orderStatus)

            Spacer(modifier = Modifier.height(10.dp))

            if (order.statusNote.isNotBlank()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = SaffronPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = order.statusNote,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Delivery Details & Items Summary
            Text(
                text = "Items: ${order.itemsSummary}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Delivery to: ${order.customerAddress} (${order.deliveryDistanceKm} km from Ragadi store)",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // UPI UTR reference box if payment is UPI
            if (order.paymentMethod == "UPI") {
                Spacer(modifier = Modifier.height(10.dp))
                if (isEditingUtr) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = utrInput,
                            onValueChange = { utrInput = it },
                            label = { Text("12-digit UPI UTR") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                onUpdateUtr(utrInput.trim())
                                isEditingUtr = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
                        ) {
                            Text("Save")
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(CobaltBlue.copy(alpha = 0.08f))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (order.upiTransactionRef.isNotBlank()) "UPI Ref: ${order.upiTransactionRef}" else "UPI Ref: Not entered",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CobaltBlue
                        )
                        Text(
                            text = "Edit Ref",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SaffronPrimary,
                            modifier = Modifier.clickable { isEditingUtr = true }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Divider()
            Spacer(modifier = Modifier.height(8.dp))

            // Action Buttons: WhatsApp Inquire & Call Store
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onCallStore,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Call Store", fontSize = 11.sp)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onWhatsAppInquire,
                    colors = ButtonDefaults.buttonColors(containerColor = FreshGreen),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Icon(imageVector = Icons.Outlined.Chat, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("WhatsApp Help", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun OrderStatusTracker(currentStatus: String) {
    val steps = listOf("Pending", "Confirmed", "Preparing", "Out for Delivery", "Delivered")
    val currentIndex = when (currentStatus) {
        "Pending" -> 0
        "Confirmed" -> 1
        "Preparing" -> 2
        "Out for Delivery" -> 3
        "Delivered" -> 4
        "Cancelled" -> -1
        else -> 0
    }

    if (currentStatus == "Cancelled") {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(StatusCancelled.copy(alpha = 0.12f))
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = StatusCancelled,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Order Cancelled",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = StatusCancelled
            )
        }
        return
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            steps.forEachIndexed { index, step ->
                val isCompleted = index < currentIndex
                val isCurrent = index == currentIndex
                val color = when {
                    isCompleted -> FreshGreen
                    isCurrent -> SaffronPrimary
                    else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                }

                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(color),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCompleted) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                    } else if (isCurrent) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                    }
                }

                if (index < steps.size - 1) {
                    val lineColor = if (index < currentIndex) FreshGreen else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(3.dp)
                            .background(lineColor)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Pending", fontSize = 9.sp, color = if (currentIndex >= 0) SaffronPrimary else Color.Gray)
            Text(text = "Preparing", fontSize = 9.sp, color = if (currentIndex >= 2) SaffronPrimary else Color.Gray)
            Text(text = "Out for Delivery", fontSize = 9.sp, color = if (currentIndex >= 3) SaffronPrimary else Color.Gray)
            Text(text = "Delivered", fontSize = 9.sp, color = if (currentIndex >= 4) FreshGreen else Color.Gray)
        }
    }
}
