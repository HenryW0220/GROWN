package com.example.myapplication

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.CardBackground
import com.example.myapplication.ui.theme.DarkText
import com.example.myapplication.ui.theme.GrayText
import com.example.myapplication.ui.theme.PageBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(onNavigate: (String) -> Unit = {}) {
    val savedPlants = AppState.savedPlants

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

            if (savedPlants.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "My Plants",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DarkText,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    savedPlants.take(2).forEach { plant ->
                        DashboardPlantCard(
                            label = if (plant.nickname.isNotBlank()) plant.nickname else plant.name,
                            imageRes = R.drawable.add,
                            onClick = {
                                PlantDashboardHolder.isCustomPlant = true
                                PlantDashboardHolder.selectedPlant = PlantLightType.UV_LIGHT
                                AppState.selectedCustomPlantId.value = plant.id
                                onNavigate(Routes.UV_LIGHT_DASHBOARD)
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    if (savedPlants.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = CardBackground,
                border = BorderStroke(1.dp, Color(0xFFD5D5C0))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigate(Routes.CARE_GUIDE_AI_SEARCH) }
                        .padding(horizontal = 16.dp, vertical = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "AI Care Search",
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
                        Text(
                            text = "Search plant care tips with AI",
                            fontSize = 13.sp,
                            color = GrayText
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Open AI search",
                        tint = GrayText,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { onNavigate(Routes.RESOURCES) },
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
                    onClick = { onNavigate(Routes.HISTORY) },
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
