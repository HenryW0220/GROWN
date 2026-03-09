package com.example.myapplication

import android.graphics.BitmapFactory
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
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
import java.io.File

// ==========================================
// Dashboard Screen (Plants page)
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(onNavigate: (String) -> Unit = {}) {
    val savedPlants = AppState.savedPlants
    val careHistory = AppState.careGuideHistory
    var showCareHistory by remember { mutableStateOf(false) }

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

            Text("Dashboard", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = DarkText)

            Spacer(modifier = Modifier.height(20.dp))

            // First row: UV Light, Natural Light
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DashboardPlantCard(
                    label = "UV Light", imageRes = R.drawable.uv,
                    onClick = {
                        PlantDashboardHolder.selectedPlant = PlantLightType.UV_LIGHT
                        PlantDashboardHolder.isCustomPlant = false
                        onNavigate(Routes.UV_LIGHT_DASHBOARD)
                    },
                    modifier = Modifier.weight(1f)
                )
                DashboardPlantCard(
                    label = "Natural Light", imageRes = R.drawable.natural,
                    onClick = {
                        PlantDashboardHolder.selectedPlant = PlantLightType.NATURAL_LIGHT
                        PlantDashboardHolder.isCustomPlant = false
                        onNavigate(Routes.UV_LIGHT_DASHBOARD)
                    },
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
                    label = "No Light", imageRes = R.drawable.nol,
                    onClick = {
                        PlantDashboardHolder.selectedPlant = PlantLightType.NO_LIGHT
                        PlantDashboardHolder.isCustomPlant = false
                        onNavigate(Routes.UV_LIGHT_DASHBOARD)
                    },
                    modifier = Modifier.weight(1f)
                )
                AddPlantCard(
                    onClick = { onNavigate(Routes.ADD_PLANT) },
                    modifier = Modifier.weight(1f)
                )
            }

            // Dynamic rows for saved plants (2 per row)
            if (savedPlants.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))

                val chunked = savedPlants.chunked(2)
                chunked.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        row.forEach { plant ->
                            SavedPlantDashboardCard(
                                plant = plant,
                                onClick = {
                                    AppState.selectedCustomPlantId.value = plant.id
                                    PlantDashboardHolder.isCustomPlant = true
                                    onNavigate(Routes.UV_LIGHT_DASHBOARD)
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (row.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Care Guide - shows search history
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = CardBackground,
                border = BorderStroke(1.dp, Color(0xFFD5D5C0))
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showCareHistory = !showCareHistory }
                            .padding(horizontal = 16.dp, vertical = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Outlined.Eco, contentDescription = "Care Guide", tint = DarkText, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Care Guide", fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = DarkText)
                            if (careHistory.isNotEmpty()) {
                                Text("${careHistory.size} search${if (careHistory.size > 1) "es" else ""}", fontSize = 12.sp, color = GrayText)
                            }
                        }
                        Icon(
                            imageVector = if (showCareHistory) Icons.Default.ExpandLess else Icons.Default.ChevronRight,
                            contentDescription = "Toggle", tint = GrayText, modifier = Modifier.size(24.dp)
                        )
                    }

                    if (showCareHistory) {
                        if (careHistory.isEmpty()) {
                            Text(
                                "No searches yet. Add a plant to get AI care tips!",
                                fontSize = 13.sp, color = GrayText,
                                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                            )
                        } else {
                            careHistory.take(5).forEach { entry ->
                                CareHistoryItem(entry = entry)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
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
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    Text("Resources", color = DarkText, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
                OutlinedButton(
                    onClick = { },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFD5D5C0)),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = CardBackground),
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    Text("History", color = DarkText, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ==========================================
// Care history item
// ==========================================
@Composable
fun CareHistoryItem(entry: CareGuideEntry) {
    val displayName = if (entry.nickname.isNotBlank()) "${entry.nickname} (${entry.plantName})" else entry.plantName
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(displayName, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = DarkText)
                Text(
                    entry.advice.take(80) + "...",
                    fontSize = 12.sp, color = GrayText, maxLines = 1, overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// ==========================================
// Saved plant card - NOW SHOWS AI GENERATED IMAGE
// ==========================================
@Composable
fun SavedPlantDashboardCard(
    plant: SavedPlant,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val displayName = if (plant.nickname.isNotBlank()) plant.nickname else plant.name

    // Check PlantImageCache for a generated image
    val imagePath = PlantImageCache.getImagePath(plant.name)
    val bitmap = remember(imagePath) {
        if (imagePath != null) {
            val file = File(imagePath)
            if (file.exists()) BitmapFactory.decodeFile(imagePath) else null
        } else null
    }

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 2.dp,
            color = Color(0xFFF5F5EC)
        ) {
            if (bitmap != null) {
                // Show AI-generated image
                Box {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = displayName,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                    // Health score badge overlay
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF4CAF50).copy(alpha = 0.85f),
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(8.dp)
                    ) {
                        Text(
                            "${plant.healthScore}%",
                            fontSize = 12.sp, fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            } else {
                // Fallback: show icon (same as before)
                Box(contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Eco, contentDescription = displayName,
                            tint = Color(0xFF4CAF50), modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFF4CAF50).copy(alpha = 0.15f)) {
                            Text(
                                "${plant.healthScore}%",
                                fontSize = 12.sp, fontWeight = FontWeight.Bold,
                                color = Color(0xFF4CAF50),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            displayName, fontSize = 14.sp, fontWeight = FontWeight.Medium,
            color = DarkText, textAlign = TextAlign.Center,
            maxLines = 1, overflow = TextOverflow.Ellipsis
        )
    }
}

// ==========================================
// Dashboard plant card (built-in)
// ==========================================
@Composable
fun DashboardPlantCard(label: String, imageRes: Int, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            modifier = Modifier.fillMaxWidth().aspectRatio(1f).clickable(onClick = onClick),
            shape = RoundedCornerShape(16.dp), shadowElevation = 2.dp
        ) {
            Image(
                painter = painterResource(id = imageRes), contentDescription = label,
                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(16.dp)),
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
fun AddPlantCard(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            modifier = Modifier.fillMaxWidth().aspectRatio(1f).clickable(onClick = onClick),
            shape = RoundedCornerShape(16.dp), shadowElevation = 2.dp
        ) {
            Image(
                painter = painterResource(id = R.drawable.add), contentDescription = "Add Plant",
                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(16.dp)),
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
