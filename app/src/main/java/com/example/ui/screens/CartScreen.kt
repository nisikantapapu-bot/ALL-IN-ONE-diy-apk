package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.CustomerProfileEntity
import com.example.data.local.entity.StoreConfigEntity
import com.example.ui.components.getCategoryIconAndColor
import com.example.ui.theme.CobaltBlue
import com.example.ui.theme.FreshGreen
import com.example.ui.theme.SaffronDark
import com.example.ui.theme.SaffronPrimary
import com.example.ui.viewmodel.CartItemDetail

@Composable
fun CartScreen(
    cartItems: List<CartItemDetail>,
    storeConfig: StoreConfigEntity,
    customerProfile: CustomerProfileEntity,
    onIncreaseQty: (String, Int) -> Unit,
    onDecreaseQty: (String, Int) -> Unit,
    onRemoveItem: (String) -> Unit,
    onChangeLocationClick: () -> Unit,
    onPlaceOrder: (paymentMethod: String, upiRef: String) -> Unit,
    onContinueShopping: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedPaymentMethod by remember { mutableStateOf("COD") } // "COD" or "UPI"
    var upiRefInput by remember { mutableStateOf("") }

    val isWithinRadius = customerProfile.distanceKm <= storeConfig.maxDeliveryRadiusKm

    val subtotal = cartItems.sumOf { it.total }
    val isFreeDelivery = subtotal >= storeConfig.freeDeliveryMinAmount
    val deliveryFee = if (isFreeDelivery) {
        0.0
    } else if (customerProfile.distanceKm <= storeConfig.baseDistanceKm) {
        storeConfig.baseDeliveryFee
    } else {
        val extraKm = customerProfile.distanceKm - storeConfig.baseDistanceKm
        storeConfig.baseDeliveryFee + (extraKm * storeConfig.perKmFee)
    }

    val totalPayable = subtotal + deliveryFee
    val totalSavings = cartItems.sumOf { (it.product.mrp - it.product.price).coerceAtLeast(0.0) * it.quantity }

    if (cartItems.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(SaffronPrimary.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = "Empty Cart",
                        tint = SaffronPrimary,
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Your Shopping Cart is Empty",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Explore groceries, daily essentials, and clothes from our Ragadi store.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onContinueShopping,
                    colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                    modifier = Modifier.testTag("empty_cart_browse_button")
                ) {
                    Text("Start Shopping", fontWeight = FontWeight.Bold)
                }
            }
        }
        return
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Shopping Cart (${cartItems.size} items)",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        }

        // Delivery Address & 15km Radius Validation Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isWithinRadius) MaterialTheme.colorScheme.surface
                    else MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isWithinRadius) Icons.Default.LocationOn else Icons.Default.Warning,
                                contentDescription = null,
                                tint = if (isWithinRadius) SaffronPrimary else MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Delivery Address",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Text(
                            text = "Change",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SaffronPrimary,
                            modifier = Modifier
                                .clickable { onChangeLocationClick() }
                                .testTag("cart_change_address_button")
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${customerProfile.name} • ${customerProfile.phone}",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Text(
                        text = "${customerProfile.address}, ${customerProfile.selectedArea}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (customerProfile.landmark.isNotBlank()) {
                        Text(
                            text = "Landmark: ${customerProfile.landmark}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Radius Status
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (isWithinRadius) FreshGreen else MaterialTheme.colorScheme.error)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isWithinRadius)
                                "Distance: ${customerProfile.distanceKm} km from Ragadi store (Within ${storeConfig.maxDeliveryRadiusKm.toInt()} km service limit)"
                            else
                                "Distance: ${customerProfile.distanceKm} km exceeds maximum ${storeConfig.maxDeliveryRadiusKm.toInt()} km delivery radius! Please choose a closer location.",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isWithinRadius) FreshGreen else MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }

        // Cart Items List
        items(cartItems, key = { it.product.id }) { item ->
            val iconPair = getCategoryIconAndColor(item.product.iconType, item.product.category)

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(iconPair.second.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = iconPair.first,
                            contentDescription = item.product.name,
                            tint = iconPair.second,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.product.name,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            maxLines = 1
                        )
                        Text(
                            text = "${item.product.unit} • ₹${item.product.price.toInt()} each",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Item Total: ₹${item.total.toInt()}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SaffronDark
                        )
                    }

                    // Quantity +/- controls
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .border(1.dp, SaffronPrimary, RoundedCornerShape(6.dp))
                    ) {
                        IconButton(
                            onClick = { onDecreaseQty(item.product.id, item.quantity - 1) },
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(
                                imageVector = if (item.quantity == 1) Icons.Default.Delete else Icons.Default.Remove,
                                contentDescription = "Decrease",
                                tint = SaffronPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                        }

                        Text(
                            text = "${item.quantity}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        IconButton(
                            onClick = { onIncreaseQty(item.product.id, item.quantity + 1) },
                            enabled = item.quantity < item.product.stockQuantity,
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Increase",
                                tint = if (item.quantity < item.product.stockQuantity) SaffronPrimary else Color.LightGray,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }

        // Bill Summary
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Bill Details",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Items Subtotal", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(text = "₹${subtotal.toInt()}", fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Delivery Fee (${customerProfile.distanceKm} km)", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            if (isFreeDelivery) {
                                Text(text = "Free for orders above ₹${storeConfig.freeDeliveryMinAmount.toInt()}", fontSize = 10.sp, color = FreshGreen)
                            }
                        }
                        if (isFreeDelivery) {
                            Text(text = "FREE", fontWeight = FontWeight.Bold, color = FreshGreen)
                        } else {
                            Text(text = "₹${deliveryFee.toInt()}", fontWeight = FontWeight.SemiBold)
                        }
                    }

                    if (totalSavings > 0) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Total Product Discount", color = FreshGreen)
                            Text(text = "-₹${totalSavings.toInt()}", fontWeight = FontWeight.Bold, color = FreshGreen)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "To Pay",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
                        )
                        Text(
                            text = "₹${totalPayable.toInt()}",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = SaffronDark
                            )
                        )
                    }
                }
            }
        }

        // Payment Method Selector
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Payment Method",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Option 1: Cash on Delivery (COD)
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { selectedPaymentMethod = "COD" }
                            .testTag("payment_option_cod"),
                        color = if (selectedPaymentMethod == "COD") SaffronPrimary.copy(alpha = 0.08f) else Color.Transparent,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (selectedPaymentMethod == "COD") SaffronPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedPaymentMethod == "COD",
                                onClick = { selectedPaymentMethod = "COD" },
                                colors = RadioButtonDefaults.colors(selectedColor = SaffronPrimary)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = "Cash on Delivery (COD)",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "Pay cash when our delivery executive arrives at your doorstep",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Option 2: UPI
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { selectedPaymentMethod = "UPI" }
                            .testTag("payment_option_upi"),
                        color = if (selectedPaymentMethod == "UPI") CobaltBlue.copy(alpha = 0.08f) else Color.Transparent,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (selectedPaymentMethod == "UPI") CobaltBlue else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = selectedPaymentMethod == "UPI",
                                    onClick = { selectedPaymentMethod = "UPI" },
                                    colors = RadioButtonDefaults.colors(selectedColor = CobaltBlue)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = "UPI Payment (Google Pay / PhonePe / Paytm)",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = "Store UPI: ${storeConfig.upiId}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = CobaltBlue
                                    )
                                }
                            }

                            if (selectedPaymentMethod == "UPI") {
                                Spacer(modifier = Modifier.height(8.dp))
                                // Launch UPI app Intent button
                                OutlinedButton(
                                    onClick = {
                                        launchUpiIntent(
                                            context = context,
                                            upiId = storeConfig.upiId,
                                            storeName = storeConfig.storeName,
                                            amount = totalPayable
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CobaltBlue)
                                ) {
                                    Icon(imageVector = Icons.Default.Payment, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Pay ₹${totalPayable.toInt()} via Installed UPI App")
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = upiRefInput,
                                    onValueChange = { upiRefInput = it },
                                    label = { Text("UPI UTR / Reference No. (12 digits)") },
                                    placeholder = { Text("Enter UTR after payment") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null,
                                        tint = SaffronPrimary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Payment verified by store before order dispatch",
                                        fontSize = 11.sp,
                                        color = SaffronDark,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Place Order Button
        item {
            Button(
                onClick = {
                    if (!isWithinRadius) {
                        Toast.makeText(
                            context,
                            "Cannot place order: Delivery location exceeds maximum ${storeConfig.maxDeliveryRadiusKm.toInt()} km radius.",
                            Toast.LENGTH_LONG
                        ).show()
                        return@Button
                    }
                    onPlaceOrder(selectedPaymentMethod, upiRefInput.trim())
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("cart_place_order_button"),
                enabled = isWithinRadius,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isWithinRadius) SaffronPrimary else MaterialTheme.colorScheme.error
                )
            ) {
                Text(
                    text = if (isWithinRadius) "Place Order • ₹${totalPayable.toInt()}" else "Service Unavailable (> 15 km)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

fun launchUpiIntent(context: Context, upiId: String, storeName: String, amount: Double) {
    val uri = Uri.Builder()
        .scheme("upi")
        .authority("pay")
        .appendQueryParameter("pa", upiId)
        .appendQueryParameter("pn", storeName)
        .appendQueryParameter("am", String.format("%.2f", amount))
        .appendQueryParameter("cu", "INR")
        .appendQueryParameter("tn", "Payment to ALL IN ONE Ragadi")
        .build()

    val intent = Intent(Intent.ACTION_VIEW, uri)
    try {
        context.startActivity(Intent.createChooser(intent, "Pay via UPI App"))
    } catch (_: Exception) {
        Toast.makeText(context, "No UPI app found on this device. Store UPI ID: $upiId", Toast.LENGTH_LONG).show()
    }
}
