package com.example.myapplication

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

// ==========================================
// Dashboard Screen (Plants page)
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(onNavigate: (String) -> Unit = {}) {
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
                    onClick = {
                        PlantDashboardHolder.selectedPlant = PlantLightType.UV_LIGHT
                        onNavigate(Routes.UV_LIGHT_DASHBOARD)
                    },
                    modifier = Modifier.weight(1f)
                )
                DashboardPlantCard(
                    label = "Natural Light",
                    imageRes = R.drawable.natural,
                    onClick = {
                        PlantDashboardHolder.selectedPlant = PlantLightType.NATURAL_LIGHT
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
                    label = "No Light",
                    imageRes = R.drawable.nol,
                    onClick = {
                        PlantDashboardHolder.selectedPlant = PlantLightType.NO_LIGHT
                        onNavigate(Routes.UV_LIGHT_DASHBOARD)
                    },
                    modifier = Modifier.weight(1f)
                )
                // Add Plant - navigates to Add Plant screen
                AddPlantCard(
                    onClick = { onNavigate(Routes.ADD_PLANT) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Care Guide row
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = CardBackground,
                border = BorderStroke(1.dp, Color(0xFFD5D5C0))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { }
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
                    Text(
                        text = "Care Guide",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DarkText,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Go",
                        tint = GrayText,
                        modifier = Modifier.size(24.dp)
                    )
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
