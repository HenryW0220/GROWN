package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.DarkText
import com.example.myapplication.ui.theme.GrayText
import com.example.myapplication.ui.theme.PageBackground

// ==========================================
// Trends Screen
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendsScreen(onNavigate: (String) -> Unit = {}) {
    Scaffold(
        bottomBar = { BottomNavBar(currentRoute = Routes.TRENDS, onNavigate = onNavigate) },
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
                text = "Trends",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Placeholder content
            Icon(
                imageVector = Icons.Outlined.ShowChart,
                contentDescription = null,
                tint = GrayText,
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Trends Coming Soon",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = DarkText
            )

            Text(
                text = "Track your plant health over time",
                fontSize = 14.sp,
                color = GrayText
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ==========================================
// Settings Screen
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onNavigate: (String) -> Unit = {}) {
    Scaffold(
        bottomBar = { BottomNavBar(currentRoute = Routes.SETTINGS, onNavigate = onNavigate) },
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
                text = "Settings",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )

            Spacer(modifier = Modifier.height(24.dp))

            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = null,
                tint = GrayText,
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Settings Coming Soon",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = DarkText
            )

            Text(
                text = "Customize your app preferences",
                fontSize = 14.sp,
                color = GrayText
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TrendsScreenPreview() {
    MaterialTheme { TrendsScreen() }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SettingsScreenPreview() {
    MaterialTheme { SettingsScreen() }
}
