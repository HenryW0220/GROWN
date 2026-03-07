package com.example.myapplication

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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

            Text(
                text = "Home",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = DarkText
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Welcome Back!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )

            Spacer(modifier = Modifier.height(20.dp))

            PlantShelfSection()

            Spacer(modifier = Modifier.height(12.dp))

            AddToShelfRow()

            Spacer(modifier = Modifier.height(12.dp))

            TrendsCard()

            Spacer(modifier = Modifier.height(12.dp))

            MenuRow(
                icon = Icons.Outlined.Notifications,
                label = "Alerts",
                onClick = { }
            )

            Spacer(modifier = Modifier.height(12.dp))

            MenuRow(
                icon = Icons.Outlined.Refresh,
                label = "Recent Activity",
                onClick = { }
            )

            Spacer(modifier = Modifier.height(12.dp))

            MenuRow(
                icon = Icons.Outlined.Eco,
                label = "Care Guide",
                onClick = { }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ==========================================
// Your Plant Shelf - plant display section
// ==========================================
@Composable
fun PlantShelfSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Your Plant Shelf",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = DarkText
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            PlantCard(label = "No Light", borderColor = PurpleBorder, imageRes = R.drawable.nol)
            PlantCard(label = "UV Light", borderColor = GreenBorder, imageRes = R.drawable.uv)
            PlantCard(label = "Natural Light", borderColor = PurpleBorder, imageRes = R.drawable.natural)
        }
    }
}

@Composable
fun PlantCard(label: String, borderColor: Color, imageRes: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(90.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFF5F5EC),
            border = BorderStroke(2.dp, borderColor)
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

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = label,
            fontSize = 12.sp,
            color = GrayText,
            textAlign = TextAlign.Center
        )
    }
}

// ==========================================
// Add to Shelf row
// ==========================================
@Composable
fun AddToShelfRow() {
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
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Add to Shelf",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = DarkText
            )
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = DarkText,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

// ==========================================
// Trends card with simple line chart
// ==========================================
@Composable
fun TrendsCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = CardBackground,
        border = BorderStroke(1.dp, Color(0xFFD5D5C0))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Trends",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkText
            )

            Spacer(modifier = Modifier.height(12.dp))

            SimpleTrendChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
        }
    }
}

@Composable
fun SimpleTrendChart(modifier: Modifier = Modifier) {
    val dataPoints = listOf(0.3f, 0.5f, 0.4f, 0.7f, 0.6f, 0.8f, 0.65f, 0.75f)

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val spacing = width / (dataPoints.size - 1)

        for (i in 0..3) {
            val y = height * i / 3
            drawLine(
                color = Color(0xFFD0D0C0),
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 1f
            )
        }

        val path = Path()
        dataPoints.forEachIndexed { index, point ->
            val x = index * spacing
            val y = height * (1 - point)
            if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }

        drawPath(
            path = path,
            color = Color(0xFF333333),
            style = Stroke(width = 2.5f)
        )

        dataPoints.forEachIndexed { index, point ->
            val x = index * spacing
            val y = height * (1 - point)
            drawCircle(
                color = Color(0xFF333333),
                radius = 4f,
                center = Offset(x, y)
            )
        }
    }
}

// ==========================================
// Reusable menu row (Alerts, Recent Activity, Care Guide)
// ==========================================
@Composable
fun MenuRow(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = CardBackground,
        border = BorderStroke(1.dp, Color(0xFFD5D5C0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = DarkText,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = label,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
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
}

// ==========================================
// Bottom navigation bar - shared across all screens
// ==========================================
@Composable
fun BottomNavBar(
    currentRoute: String = Routes.HOME,
    onNavigate: (String) -> Unit = {}
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 4.dp
    ) {
        val items = listOf(
            BottomNavItem("home", Icons.Default.Home, Routes.HOME),
            BottomNavItem("environment", Icons.Default.Yard, Routes.ENVIRONMENT),
            BottomNavItem("plants", Icons.Default.Eco, Routes.DASHBOARD),
            BottomNavItem("trends", Icons.Default.ShowChart, Routes.TRENDS),
            BottomNavItem("settings", Icons.Default.Settings, Routes.SETTINGS),
        )

        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 10.sp
                    )
                },
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PlantGreen,
                    selectedTextColor = PlantGreen,
                    unselectedIconColor = GrayText,
                    unselectedTextColor = GrayText,
                    indicatorColor = LightGreen
                )
            )
        }
    }
}

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

// ==========================================
// Preview
// ==========================================
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen()
    }
}
