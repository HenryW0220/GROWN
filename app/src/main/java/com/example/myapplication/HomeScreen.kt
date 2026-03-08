package com.example.myapplication

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.*

// ==========================================
// Main page - HomeScreen
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onNavigate: (String) -> Unit = {}) {
    val savedPlants = AppState.savedPlants
    val alerts = AppState.alerts
    val activities = AppState.activityLog

    Scaffold(
        bottomBar = { BottomNavBar(currentRoute = Routes.HOME, onNavigate = onNavigate) },
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

            Text("Home", fontSize = 18.sp, fontWeight = FontWeight.Medium, color = DarkText)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Welcome Back!", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = DarkText)

            Spacer(modifier = Modifier.height(20.dp))

            // Your Plant Shelf
            PlantShelfSection(savedPlants = savedPlants, onNavigate = onNavigate)

            Spacer(modifier = Modifier.height(12.dp))

            TrendsCard()

            Spacer(modifier = Modifier.height(12.dp))

            // Alerts - with real data
            AlertsSection(alerts = alerts, onNavigate = onNavigate)

            Spacer(modifier = Modifier.height(12.dp))

            // Recent Activity - with real data
            RecentActivitySection(activities = activities)

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ==========================================
// Plant Shelf - shows built-in + saved plants
// ==========================================
@Composable
fun PlantShelfSection(savedPlants: List<SavedPlant>, onNavigate: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Your Plant Shelf", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = DarkText)
        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Built-in plants
            item {
                PlantCard(
                    label = "No Light",
                    borderColor = PurpleBorder,
                    imageRes = R.drawable.nol,
                    onClick = {
                        PlantDashboardHolder.selectedPlant = PlantLightType.NO_LIGHT
                        PlantDashboardHolder.isCustomPlant = false
                        onNavigate(Routes.UV_LIGHT_DASHBOARD)
                    }
                )
            }
            item {
                PlantCard(
                    label = "UV Light",
                    borderColor = GreenBorder,
                    imageRes = R.drawable.uv,
                    onClick = {
                        PlantDashboardHolder.selectedPlant = PlantLightType.UV_LIGHT
                        PlantDashboardHolder.isCustomPlant = false
                        onNavigate(Routes.UV_LIGHT_DASHBOARD)
                    }
                )
            }
            item {
                PlantCard(
                    label = "Natural Light",
                    borderColor = PurpleBorder,
                    imageRes = R.drawable.natural,
                    onClick = {
                        PlantDashboardHolder.selectedPlant = PlantLightType.NATURAL_LIGHT
                        PlantDashboardHolder.isCustomPlant = false
                        onNavigate(Routes.UV_LIGHT_DASHBOARD)
                    }
                )
            }
            // User-added plants
            items(savedPlants) { plant ->
                SavedPlantCard(
                    plant = plant,
                    onClick = {
                        AppState.selectedCustomPlantId.value = plant.id
                        PlantDashboardHolder.isCustomPlant = true
                        onNavigate(Routes.UV_LIGHT_DASHBOARD)
                    }
                )
            }
        }
    }
}

@Composable
fun PlantCard(label: String, borderColor: Color, imageRes: Int, onClick: () -> Unit = {}) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            modifier = Modifier.size(90.dp).clickable(onClick = onClick),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFF5F5EC),
            border = BorderStroke(2.dp, borderColor)
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = label,
                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(label, fontSize = 12.sp, color = GrayText, textAlign = TextAlign.Center)
    }
}

@Composable
fun SavedPlantCard(plant: SavedPlant, onClick: () -> Unit) {
    val displayName = if (plant.nickname.isNotBlank()) plant.nickname else plant.name
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            modifier = Modifier.size(90.dp).clickable(onClick = onClick),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFF5F5EC),
            border = BorderStroke(2.dp, GreenBorder)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Eco,
                    contentDescription = displayName,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(40.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = displayName,
            fontSize = 12.sp,
            color = GrayText,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width(90.dp)
        )
    }
}

// ==========================================
// Alerts Section - expandable with real data
// ==========================================
@Composable
fun AlertsSection(alerts: List<AlertEntry>, onNavigate: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val displayAlerts = if (expanded) alerts else alerts.take(2)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = CardBackground,
        border = BorderStroke(1.dp, Color(0xFFD5D5C0))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Notifications, contentDescription = "Alerts", tint = DarkText, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Alerts", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = DarkText)
                }
                // Badge
                Surface(shape = RoundedCornerShape(10.dp), color = Color(0xFFE57373)) {
                    Text(
                        text = "${alerts.size}",
                        fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            displayAlerts.forEach { alert ->
                AlertItem(alert = alert)
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (alerts.size > 2) {
                TextButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        if (expanded) "Show Less" else "View All ${alerts.size} Alerts",
                        color = DarkText, fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
fun AlertItem(alert: AlertEntry) {
    val iconColor = when (alert.severity) {
        "critical" -> Color(0xFFE57373)
        "warning" -> Color(0xFFE6C54A)
        else -> Color(0xFF66BB6A)
    }
    val icon = when (alert.severity) {
        "critical" -> Icons.Default.Error
        "warning" -> Icons.Default.Warning
        else -> Icons.Default.CheckCircle
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(alert.title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = DarkText)
            Text(alert.message, fontSize = 12.sp, color = GrayText, lineHeight = 16.sp)
        }
    }
}

// ==========================================
// Recent Activity Section
// ==========================================
@Composable
fun RecentActivitySection(activities: List<ActivityEntry>) {
    var expanded by remember { mutableStateOf(false) }
    val displayActivities = if (expanded) activities.take(10) else activities.take(3)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = CardBackground,
        border = BorderStroke(1.dp, Color(0xFFD5D5C0))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Refresh, contentDescription = "Recent Activity", tint = DarkText, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text("Recent Activity", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = DarkText)
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (activities.isEmpty()) {
                Text("No recent activity", fontSize = 13.sp, color = GrayText)
            } else {
                displayActivities.forEach { entry ->
                    ActivityItem(entry = entry)
                    Spacer(modifier = Modifier.height(6.dp))
                }
                if (activities.size > 3) {
                    TextButton(
                        onClick = { expanded = !expanded },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            if (expanded) "Show Less" else "View All",
                            color = DarkText, fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ActivityItem(entry: ActivityEntry) {
    val icon = when (entry.icon) {
        "plant" -> Icons.Default.Eco
        "water" -> Icons.Default.WaterDrop
        "alert" -> Icons.Default.Warning
        "search" -> Icons.Default.Search
        else -> Icons.Default.Info
    }
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = GrayText, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(entry.description, fontSize = 13.sp, color = DarkText, lineHeight = 18.sp)
    }
}

// ==========================================
// Trends card
// ==========================================
@Composable
fun TrendsCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = CardBackground,
        border = BorderStroke(1.dp, Color(0xFFD5D5C0))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Trends", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = DarkText)
            Spacer(modifier = Modifier.height(12.dp))
            SimpleTrendChart(modifier = Modifier.fillMaxWidth().height(100.dp))
        }
    }
}

@Composable
fun SimpleTrendChart(modifier: Modifier = Modifier) {
    val dataPoints = listOf(0.3f, 0.5f, 0.4f, 0.7f, 0.6f, 0.8f, 0.65f, 0.75f)
    Canvas(modifier = modifier) {
        val width = size.width; val height = size.height; val spacing = width / (dataPoints.size - 1)
        for (i in 0..3) {
            val y = height * i / 3
            drawLine(Color(0xFFD0D0C0), Offset(0f, y), Offset(width, y), 1f)
        }
        val path = Path()
        dataPoints.forEachIndexed { index, point ->
            val x = index * spacing; val y = height * (1 - point)
            if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        drawPath(path, Color(0xFF333333), style = Stroke(width = 2.5f))
        dataPoints.forEachIndexed { index, point ->
            drawCircle(Color(0xFF333333), 4f, Offset(index * spacing, height * (1 - point)))
        }
    }
}

// ==========================================
// Bottom navigation bar
// ==========================================
@Composable
fun BottomNavBar(currentRoute: String = Routes.HOME, onNavigate: (String) -> Unit = {}) {
    NavigationBar(containerColor = Color.White, tonalElevation = 4.dp) {
        val items = listOf(
            BottomNavItem("home", Icons.Default.Home, Routes.HOME),
            BottomNavItem("environment", Icons.Default.Yard, Routes.ENVIRONMENT),
            BottomNavItem("plants", Icons.Default.Eco, Routes.DASHBOARD),
            BottomNavItem("trends", Icons.Default.ShowChart, Routes.TRENDS),
            BottomNavItem("settings", Icons.Default.Settings, Routes.SETTINGS),
        )
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label, modifier = Modifier.size(24.dp)) },
                label = { Text(item.label, fontSize = 10.sp) },
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PlantGreen, selectedTextColor = PlantGreen,
                    unselectedIconColor = GrayText, unselectedTextColor = GrayText,
                    indicatorColor = LightGreen
                )
            )
        }
    }
}

data class BottomNavItem(val label: String, val icon: ImageVector, val route: String)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    MaterialTheme { HomeScreen() }
}
