package com.example.myapplication

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.CardBackground
import com.example.myapplication.ui.theme.DarkText
import com.example.myapplication.ui.theme.GrayText
import com.example.myapplication.ui.theme.PageBackground

// ==========================================
// Dashboard Screen (Plants page)
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(onNavigate: (String) -> Unit = {}) {
    val context = LocalContext.current
    val db = remember { PlantCareDatabase.getDatabase(context) }

    // Load saved care guides from database
    var careGuides by remember { mutableStateOf<List<CareGuideEntity>>(emptyList()) }
    var isCareGuideExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        careGuides = db.careGuideDao().getAllGuides()
    }

    // Refresh guides when returning to this screen
    LaunchedEffect(careGuides) {
        // This will re-compose when careGuides changes
    }

    Scaffold(
        bottomBar = { BottomNavBar(currentRoute = Routes.DASHBOARD, onNavigate = onNavigate) },
        containerColor = PageBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Dashboard",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )

            Spacer(modifier = Modifier.height(20.dp))

            // First row: UV Light, Natural Light
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DashboardPlantCard(
                    label = "UV Light",
                    imageRes = R.drawable.uv,
                    onClick = { onNavigate(Routes.UV_LIGHT_DASHBOARD) },
                    modifier = Modifier.weight(1f)
                )
                DashboardPlantCard(
                    label = "Natural Light",
                    imageRes = R.drawable.natural,
                    onClick = { },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Second row: No Light, Add Plant
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DashboardPlantCard(
                    label = "No Light",
                    imageRes = R.drawable.nol,
                    onClick = { },
                    modifier = Modifier.weight(1f)
                )
                AddPlantCard(
                    onClick = { onNavigate(Routes.ADD_PLANT) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ==========================================
            // Care Guide section - expandable with saved guides
            // ==========================================
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = CardBackground,
                border = BorderStroke(1.dp, Color(0xFFD5D5C0))
            ) {
                Column {
                    // Header row - toggles expand/collapse
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isCareGuideExpanded = !isCareGuideExpanded }
                            .padding(horizontal = 16.dp, vertical = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Eco,
                            contentDescription = "Care Guide",
                            tint = DarkText,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Care Guide",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = DarkText
                            )
                            if (careGuides.isNotEmpty()) {
                                Text(
                                    text = "${careGuides.size} saved",
                                    fontSize = 13.sp,
                                    color = GrayText
                                )
                            }
                        }
                        Icon(
                            imageVector = if (isCareGuideExpanded) Icons.Default.KeyboardArrowUp
                            else Icons.Default.KeyboardArrowDown,
                            contentDescription = if (isCareGuideExpanded) "Collapse" else "Expand",
                            tint = GrayText,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Expandable content - list of saved care guides
                    AnimatedVisibility(visible = isCareGuideExpanded) {
                        Column(
                            modifier = Modifier.padding(
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 16.dp
                            )
                        ) {
                            if (careGuides.isEmpty()) {
                                // Empty state
                                Text(
                                    text = "No care guides yet. Add a plant to get AI care tips!",
                                    fontSize = 14.sp,
                                    color = GrayText,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            } else {
                                // Show each saved care guide as a clickable card
                                careGuides.forEach { guide ->
                                    CareGuideListItem(
                                        guide = guide,
                                        onClick = {
                                            PlantDataHolder.selectedGuideId = guide.id
                                            onNavigate(Routes.CARE_GUIDE_DETAIL)
                                        }
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Resources and History buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFD5D5C0)),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = CardBackground),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text("Resources", color = DarkText, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
                OutlinedButton(
                    onClick = { },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFD5D5C0)),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = CardBackground),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text("History", color = DarkText, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ==========================================
// Single care guide list item (clickable)
// ==========================================
@Composable
fun CareGuideListItem(
    guide: CareGuideEntity,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Sparkle icon
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = Color(0xFFFFB300),
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                // Plant name
                Text(
                    text = if (guide.plantNickname.isNotBlank()) {
                        "${guide.plantNickname} (${guide.plantName})"
                    } else {
                        guide.plantName
                    },
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DarkText
                )
                // Preview of AI advice (first line)
                Text(
                    text = guide.aiAdvice.take(80) + if (guide.aiAdvice.length > 80) "..." else "",
                    fontSize = 13.sp,
                    color = GrayText,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "View details",
                tint = GrayText,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// ==========================================
// Dashboard plant card
// ==========================================
@Composable
fun DashboardPlantCard(
    label: String,
    imageRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 2.dp
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = label,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = DarkText, textAlign = TextAlign.Center)
    }
}

// ==========================================
// Add Plant card
// ==========================================
@Composable
fun AddPlantCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 2.dp
        ) {
            Image(
                painter = painterResource(id = R.drawable.add),
                contentDescription = "Add Plant",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Add Plant", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = DarkText, textAlign = TextAlign.Center)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardScreenPreview() {
    MaterialTheme { DashboardScreen() }
}
