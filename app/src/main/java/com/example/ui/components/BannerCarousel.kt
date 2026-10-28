package com.example.ui.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CobaltBlue
import com.example.ui.theme.FreshGreen
import com.example.ui.theme.SaffronDark
import com.example.ui.theme.SaffronPrimary
import kotlinx.coroutines.delay

data class BannerItem(
    val title: String,
    val subtitle: String,
    val tag: String,
    val icon: ImageVector,
    val gradient: List<Color>
)

@Composable
fun BannerCarousel(
    modifier: Modifier = Modifier
) {
    val banners = remember {
        listOf(
            BannerItem(
                title = "Ragadi Express Delivery",
                subtitle = "Delivering within 15 km in Jajpur Road • Free on ₹499+",
                tag = "FAST DELIVERY",
                icon = Icons.Default.LocalShipping,
                gradient = listOf(SaffronPrimary, SaffronDark)
            ),
            BannerItem(
                title = "Wholesale Grocery Deals",
                subtitle = "Atta, Dal, Mustard Oil & Staples at Lowest Daily Prices",
                tag = "MAX SAVINGS",
                icon = Icons.Default.Savings,
                gradient = listOf(FreshGreen, Color(0xFF1B5E20))
            ),
            BannerItem(
                title = "Verified UPI & COD",
                subtitle = "Order via Google Pay, PhonePe, Paytm or Cash on Delivery",
                tag = "EASY PAY",
                icon = Icons.Default.ElectricBolt,
                gradient = listOf(CobaltBlue, Color(0xFF0D47A1))
            )
        )
    }

    var currentIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(4000)
            currentIndex = (currentIndex + 1) % banners.size
        }
    }

    val banner = banners[currentIndex]

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.horizontalGradient(banner.gradient))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.White.copy(alpha = 0.25f))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = banner.tag,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = banner.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        ),
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = banner.subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.9f)
                        ),
                        maxLines = 2
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = banner.icon,
                        contentDescription = banner.title,
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            // Dot indicators
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(bottom = 0.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                banners.indices.forEach { index ->
                    Box(
                        modifier = Modifier
                            .size(if (index == currentIndex) 16.dp else 6.dp, 4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(if (index == currentIndex) Color.White else Color.White.copy(alpha = 0.4f))
                    )
                }
            }
        }
    }
}
