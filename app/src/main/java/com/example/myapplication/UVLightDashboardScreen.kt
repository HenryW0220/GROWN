package com.example.myapplication

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.CardBackground
import com.example.myapplication.ui.theme.DarkText
import com.example.myapplication.ui.theme.GrayText
import com.example.myapplication.ui.theme.PageBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UVLightDashboardScreen(onNavigate: (String) -> Unit = {}) {
    // Determine if showing built-in or custom plant
    val isCustom = PlantDashboardHolder.isCustomPlant
    val customPlant = if (isCustom) {
        AppState.savedPlants.find { it.id == AppState.selectedCustomPlantId.value }
    } else null

    val data = if (isCustom && customPlant != null) {
        getCustomPlantDashboardData(customPlant)
    } else {
        getPlantDashboardData(PlantDashboardHolder.selectedPlant)
    }

    val scoreColor = when {
        data.healthScore >= 75 -> Color(0xFF8BC34A)
        data.healthScore >= 50 -> Color(0xFFE6C54A)
        else -> Color(0xFFE57373)
    }
    val arcColor = when {
        data.healthScore >= 75 -> Color(0xFFCDDC39)
        data.healthScore >= 50 -> Color(0xFFFFD54F)
        else -> Color(0xFFE57373)
    }

    // Log dashboard view
    LaunchedEffect(data.headerTitle) {
        AppState.logActivity("Viewed ${data.headerTitle} dashboard", "plant")
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

            Text(data.headerTitle, fontSize = 16.sp, fontWeight = FontWeight.Normal, color = DarkText)
            Text("Dashboard", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = DarkText)

            Spacer(modifier = Modifier.height(24.dp))

            PlantHealthScoreCard(score = data.healthScore, label = data.healthLabel, scoreColor = scoreColor, arcColor = arcColor)

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MetricCard(
                    title = "Soil Moisture", value = data.soilMoisture, icon = Icons.Default.WaterDrop,
                    onClick = {
                        MetricReportHolder.selectedMetric = MetricType.SOIL_MOISTURE
                        onNavigate(Routes.METRIC_REPORT)
                    },
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Light Level", value = data.lightLevel, icon = Icons.Default.LightMode,
                    onClick = {
                        MetricReportHolder.selectedMetric = MetricType.LIGHT_LEVEL
                        onNavigate(Routes.METRIC_REPORT)
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MetricCard(
                    title = "Co2 Levels", value = "", icon = Icons.Default.Air, showMiniChart = true,
                    onClick = {
                        MetricReportHolder.selectedMetric = MetricType.CO2
                        onNavigate(Routes.METRIC_REPORT)
                    },
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Humidity", value = data.humidity, icon = Icons.Default.Thermostat,
                    onClick = {
                        MetricReportHolder.selectedMetric = MetricType.HUMIDITY
                        onNavigate(Routes.METRIC_REPORT)
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            WaterReminderRow(reminderText = data.waterReminder)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { },
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, DarkText),
                modifier = Modifier.padding(horizontal = 32.dp)
            ) {
                Text("View Details", color = DarkText, fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun PlantHealthScoreCard(score: Int, label: String = "Optimal", scoreColor: Color = Color(0xFF8BC34A), arcColor: Color = Color(0xFFCDDC39)) {
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), color = CardBackground, border = BorderStroke(1.dp, Color(0xFFD5D5C0))) {
        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(160.dp)) {
                Canvas(modifier = Modifier.size(160.dp)) {
                    val sw = 16.dp.toPx(); val r = (size.minDimension - sw) / 2
                    val tl = Offset((size.width - r * 2) / 2, (size.height - r * 2) / 2)
                    drawArc(Color(0xFFE0E0D8), -90f, 360f, false, tl, Size(r * 2, r * 2), style = Stroke(sw, cap = StrokeCap.Round))
                    drawArc(arcColor, -90f, 360f * score / 100f, false, tl, Size(r * 2, r * 2), style = Stroke(sw, cap = StrokeCap.Round))
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${score}%", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = DarkText)
                    Text(label, fontSize = 14.sp, color = scoreColor, fontWeight = FontWeight.Medium)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("Plant Health Score", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = DarkText)
        }
    }
}

@Composable
fun MetricCard(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier, showMiniChart: Boolean = false, onClick: () -> Unit = {}) {
    Surface(modifier = modifier.height(100.dp).clickable(onClick = onClick), shape = RoundedCornerShape(12.dp), color = Color.White, shadowElevation = 1.dp) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, fontSize = 12.sp, color = GrayText, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                if (showMiniChart) { MiniChart(modifier = Modifier.weight(1f).height(32.dp)) }
                else { Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = DarkText) }
                Spacer(modifier = Modifier.width(8.dp))
                Icon(icon, contentDescription = title, tint = GrayText, modifier = Modifier.size(24.dp))
            }
        }
    }
}

@Composable
fun MiniChart(modifier: Modifier = Modifier) {
    val pts = listOf(0.4f, 0.6f, 0.3f, 0.7f, 0.5f, 0.8f)
    Canvas(modifier = modifier) {
        val w = size.width; val h = size.height; val s = w / (pts.size - 1)
        val p = androidx.compose.ui.graphics.Path()
        pts.forEachIndexed { i, v -> val x = i * s; val y = h * (1 - v); if (i == 0) p.moveTo(x, y) else p.lineTo(x, y) }
        drawPath(p, Color(0xFF333333), style = Stroke(2f))
    }
}

@Composable
fun WaterReminderRow(reminderText: String = "Water within 12 hours") {
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), color = CardBackground, border = BorderStroke(1.dp, Color(0xFFD5D5C0))) {
        Row(modifier = Modifier.fillMaxWidth().clickable { }.padding(horizontal = 16.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.WaterDrop, contentDescription = "Water", tint = DarkText, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(reminderText, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = DarkText, modifier = Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, contentDescription = "Go", tint = GrayText, modifier = Modifier.size(24.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun UVLightDashboardScreenPreview() {
    MaterialTheme { UVLightDashboardScreen() }
}
