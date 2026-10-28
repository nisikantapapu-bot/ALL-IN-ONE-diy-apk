package com.example.ui.components

import android.content.Context
import android.content.Intent
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
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.InstallMobile
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.FreshGreen
import com.example.ui.theme.SaffronDark
import com.example.ui.theme.SaffronPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadAppSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

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
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(FreshGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Download ALL IN ONE App",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "ଆପ୍ ଡାଉନଲୋଡ୍ ଏବଂ ଇନଷ୍ଟଲ କରିବା ପ୍ରଣାଳୀ",
                            fontSize = 12.sp,
                            color = SaffronDark,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Notice Card: How to download right now in AI Studio
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SaffronPrimary.copy(alpha = 0.1f)),
                border = CardDefaults.outlinedCardBorder().copy(width = 1.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Android,
                        contentDescription = null,
                        tint = SaffronPrimary,
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "ଏଠାରୁ କିପରି APK ଡାଉନଲୋଡ୍ କରିବେ:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = SaffronDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Google AI Studio ର ଉପର ଡାହାଣ କୋଣରେ ଥିବା Settings/Export ମେନୁ କ୍ଲିକ୍ କରନ୍ତୁ ଏବଂ 'Generate APK' କିମ୍ବା 'Export as ZIP' ଚୟନ କରନ୍ତୁ। APK ଫାଇଲ ତୁରନ୍ତ ଡାଉନଲୋଡ୍ ହୋଇଯିବ।",
                            fontSize = 12.sp,
                            lineHeight = 17.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Step 1
            DownloadStepItem(
                stepNumber = "1",
                titleEn = "Generate & Download APK",
                titleOr = "ପ୍ରଥମେ APK ଡାଉନଲୋଡ୍ କରନ୍ତୁ",
                description = "Click on the top-right Settings/Export menu in AI Studio and select 'Generate APK'. Your 'ALL IN ONE.apk' file will download to your device."
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Step 2
            DownloadStepItem(
                stepNumber = "2",
                titleEn = "Allow Unknown Apps (Mobile)",
                titleOr = "ମୋବାଇଲରେ ଅନୁମତି ଦିଅନ୍ତୁ",
                description = "When opening the APK on your phone, if Android shows 'Install unknown apps', tap 'Settings' and toggle 'Allow from this source' (Chrome / File Manager)."
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Step 3
            DownloadStepItem(
                stepNumber = "3",
                titleEn = "Install & Launch Store",
                titleOr = "Install କରନ୍ତୁ ଓ ଅର୍ଡର କରନ୍ତୁ",
                description = "Click 'Install'. Once completed, open ALL IN ONE to browse groceries, clothes, electronics & place express orders in Ragadi, Jajpur Road!"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Share App Action
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.VerifiedUser,
                            contentDescription = null,
                            tint = FreshGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "App Details • Ragadi, Jajpur Road",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "• Package ID: com.aistudio.allinone.chdaza\n• App Name: ALL IN ONE\n• Coverage: 15 KM Radius Express Delivery\n• Supported: COD & Google Pay / PhonePe UPI",
                        fontSize = 11.sp,
                        lineHeight = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = {
                        shareAppInfo(context)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("share_app_button")
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Share App", fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.width(10.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("download_dismiss_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
                ) {
                    Text("Done / ବନ୍ଦ କରନ୍ତୁ", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun DownloadStepItem(
    stepNumber: String,
    titleEn: String,
    titleOr: String,
    description: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(FreshGreen),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stepNumber,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "$titleEn ($titleOr)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

fun shareAppInfo(context: Context) {
    val shareText = """
        🛍️ ALL IN ONE - Local Online Shopping & Express Delivery
        📍 Ragadi, Jajpur Road, Odisha (15 KM Radius)
        
        Order Groceries, Vegetables, Electronics, Daily Essentials & more!
        Fast home delivery with Cash on Delivery & UPI payment.
        
        Download & Open ALL IN ONE Android Application:
        https://ais-pre-hxtl6bhrvzdtd4frt7rrzn-263251809377.asia-northeast1.run.app
    """.trimIndent()

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "ALL IN ONE Android App - Ragadi, Jajpur Road")
        putExtra(Intent.EXTRA_TEXT, shareText)
    }
    context.startActivity(Intent.createChooser(intent, "Share ALL IN ONE App via"))
}
