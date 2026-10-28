package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CobaltBlue
import com.example.ui.theme.FreshGreen
import com.example.ui.theme.SaffronDark
import com.example.ui.theme.SaffronPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublishingGuideDialog(
    sheetState: SheetState,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(FreshGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.HelpOutline,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "ALL IN ONE Architecture & Setup",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Ragadi, Jajpur Road • Complete Technical Guide",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Step 1: Database Architecture
            GuideSectionCard(
                title = "1. Database Schema & Collections",
                badge = "DATA LAYER",
                badgeColor = SaffronPrimary,
                content = """
                • products: id, name, category, price, mrp, unit, stockQuantity, description, iconType, isAvailable
                • categories: id, name, iconName, displayOrder
                • orders: id (e.g. AIO-72910), customerName, customerPhone, customerAddress, landmark, deliveryDistanceKm, itemsSummary, itemsJson, subtotal, deliveryFee, totalAmount, paymentMethod (COD/UPI), paymentStatus, upiTransactionRef, orderStatus, orderTime
                • store_config: storeName ('ALL IN ONE'), storeLat (20.9507), storeLng (86.1344), maxDeliveryRadiusKm (15.0), baseDeliveryFee (₹20), baseDistanceKm (3.0), perKmFee (₹5), freeDeliveryMinAmount (₹499), adminPin ('1234')
                • customer_profile: name, phone, address, selectedArea, landmark, distanceKm
                """.trimIndent()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Step 2: 15 KM Delivery Radius & Fee Calculation
            GuideSectionCard(
                title = "2. 15 KM Radius & Delivery Charges",
                badge = "DELIVERY LOGIC",
                badgeColor = FreshGreen,
                content = """
                • Shop Anchor: Ragadi, Jajpur Road (20.9507° N, 86.1344° E).
                • Haversine formula calculates real distance between customer coordinates and Ragadi store.
                • Radius Gate: If distance > 15.0 km, app displays service boundary warning and prevents order checkout.
                • Delivery Fee Formula:
                   - If subtotal >= ₹499 -> FREE DELIVERY
                   - If distance <= 3 km -> Base Fee (₹20)
                   - If distance > 3 km -> ₹20 + (Distance - 3 km) * ₹5/km
                """.trimIndent()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Step 3: Payments Setup (COD & UPI)
            GuideSectionCard(
                title = "3. Payment System & Verification",
                badge = "PAYMENTS",
                badgeColor = CobaltBlue,
                content = """
                • Cash on Delivery (COD): Customer selects COD at checkout; delivery partner collects exact amount upon doorstep delivery.
                • UPI Payments:
                   - Uses Android UPI Intent uri: `upi://pay?pa=allinone.ragadi@okaxis&pn=ALL+IN+ONE&am=Amount&cu=INR`
                   - Works with Google Pay, PhonePe, Paytm, BHIM.
                   - Security Policy: Orders are marked as 'Verification Pending' until the shop owner checks their UPI bank SMS/app and clicks 'Verify UPI' in Admin Dashboard.
                """.trimIndent()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Step 4: Admin Portal Controls
            GuideSectionCard(
                title = "4. Shop Owner Admin Dashboard",
                badge = "ADMIN ACCESS",
                badgeColor = SaffronDark,
                content = """
                • Default Access PIN: '1234' (changeable anytime in Settings).
                • Full Inventory Control: Add new items, edit selling prices, change stock numbers, delete discontinued goods.
                • Order Status Pipeline: Pending -> Confirmed -> Preparing -> Out for Delivery -> Delivered.
                • Instant Customer Communication: Direct Call and WhatsApp buttons automatically pre-fill customer mobile number and Order ID!
                """.trimIndent()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Step 5: Android APK Generation & Play Store
            GuideSectionCard(
                title = "5. APK Building & Play Store Publishing",
                badge = "PUBLISHING",
                badgeColor = Color(0xFF6A1B9A),
                content = """
                • Export Project: In Google AI Studio, click top-right menu -> 'Download ZIP' or 'Generate APK'.
                • To build release APK / AAB locally:
                   Run: `gradle :app:assembleRelease` or `gradle :app:bundleRelease`
                • Google Play Console:
                   1. Create app listing as 'ALL IN ONE' (shopping / grocery).
                   2. Upload the generated `.aab` (Android App Bundle).
                   3. Add Privacy Policy URL and Store description (serving Jajpur Road, Ragadi, Odisha).
                """.trimIndent()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("guide_got_it_button"),
                colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
            ) {
                Text("Got It! Back to Store", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun GuideSectionCard(
    title: String,
    badge: String,
    badgeColor: Color,
    content: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(badgeColor.copy(alpha = 0.15f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = badge,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = content,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
