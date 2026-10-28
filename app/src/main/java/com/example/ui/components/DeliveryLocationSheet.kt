package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.CustomerProfileEntity
import com.example.data.local.entity.StoreConfigEntity
import com.example.data.sample.SampleData
import com.example.ui.theme.FreshGreen
import com.example.ui.theme.SaffronPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryLocationSheet(
    sheetState: SheetState,
    storeConfig: StoreConfigEntity,
    currentProfile: CustomerProfileEntity,
    onSaveLocation: (CustomerProfileEntity) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedPreset by remember {
        mutableStateOf(
            SampleData.deliveryLocationPresets.firstOrNull { it.name.startsWith(currentProfile.selectedArea) }
                ?: SampleData.deliveryLocationPresets[0]
        )
    }

    var customAddress by remember { mutableStateOf(currentProfile.address) }
    var landmark by remember { mutableStateOf(currentProfile.landmark) }
    var recipientName by remember { mutableStateOf(currentProfile.name) }
    var recipientPhone by remember { mutableStateOf(currentProfile.phone) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Choose Delivery Location",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Shop: Ragadi, Jajpur Road • Max Radius: ${storeConfig.maxDeliveryRadiusKm.toInt()} km",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 15 km Radius Warning/Info Banner
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = SaffronPrimary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Radius",
                        tint = SaffronPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "We deliver up to 15 km from our Ragadi store. Delivery fee: ₹${storeConfig.baseDeliveryFee.toInt()} base + ₹${storeConfig.perKmFee.toInt()}/km (Free above ₹${storeConfig.freeDeliveryMinAmount.toInt()}).",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Select Local Area (Jajpur Road & Ragadi)",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Presets List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(SampleData.deliveryLocationPresets) { preset ->
                    val isSelected = selectedPreset.name == preset.name
                    val isAllowed = preset.distanceKm <= storeConfig.maxDeliveryRadiusKm

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                selectedPreset = preset
                            }
                            .testTag("location_preset_${preset.name.take(10)}"),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) SaffronPrimary.copy(alpha = 0.12f)
                            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                        ),
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, SaffronPrimary) else null
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(if (isAllowed) FreshGreen.copy(alpha = 0.15f) else MaterialTheme.colorScheme.error.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (isAllowed) Icons.Default.LocationOn else Icons.Default.Warning,
                                        contentDescription = null,
                                        tint = if (isAllowed) FreshGreen else MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = preset.name,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "${preset.distanceKm} km from shop • ${if (isAllowed) "Deliverable ✓" else "Exceeds 15km ✗"}",
                                        fontSize = 11.sp,
                                        color = if (isAllowed) FreshGreen else MaterialTheme.colorScheme.error
                                    )
                                }
                            }

                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = SaffronPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Address Details Form
            OutlinedTextField(
                value = customAddress,
                onValueChange = { customAddress = it },
                label = { Text("House / Street Address") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = landmark,
                    onValueChange = { landmark = it },
                    label = { Text("Landmark") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = recipientPhone,
                    onValueChange = { if (it.length <= 10) recipientPhone = it.filter { c -> c.isDigit() } },
                    label = { Text("Mobile (+91)") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            val isSelectedWithinRadius = selectedPreset.distanceKm <= storeConfig.maxDeliveryRadiusKm

            Button(
                onClick = {
                    val updated = currentProfile.copy(
                        name = recipientName.ifBlank { "Valued Customer" },
                        phone = recipientPhone.ifBlank { "9438012345" },
                        address = customAddress.ifBlank { selectedPreset.name },
                        selectedArea = selectedPreset.name,
                        landmark = landmark.ifBlank { selectedPreset.landmark },
                        distanceKm = selectedPreset.distanceKm
                    )
                    onSaveLocation(updated)
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("save_delivery_location_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelectedWithinRadius) SaffronPrimary else MaterialTheme.colorScheme.error
                )
            ) {
                Text(
                    text = if (isSelectedWithinRadius) "Set Delivery Location (${selectedPreset.distanceKm} km)" else "Location Beyond 15km Radius",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
