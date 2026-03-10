package com.example.myapplication

import androidx.compose.runtime.*
import androidx.compose.material3.MaterialTheme

// ==========================================
// All screen routes
// ==========================================
object Routes {
    const val HOME = "home"
    const val ENVIRONMENT = "environment"
    const val DASHBOARD = "dashboard"
    const val TRENDS = "trends"
    const val SETTINGS = "settings"
    const val UV_LIGHT_DASHBOARD = "uv_light_dashboard"
    const val ADD_PLANT = "add_plant"
    const val PLANT_CARE_RESULT = "plant_care_result"
    const val CARE_GUIDE_DETAIL = "care_guide_detail"
    const val METRIC_REPORT = "metric_report"
}

// ==========================================
// Main App with navigation
// ==========================================
@Composable
fun PlantApp() {
    var currentScreen by remember { mutableStateOf(Routes.HOME) }

    MaterialTheme {
        when (currentScreen) {
            Routes.HOME -> HomeScreen(
                onNavigate = { currentScreen = it }
            )
            Routes.ENVIRONMENT -> EnvironmentScreen(
                onNavigate = { currentScreen = it }
            )
            Routes.DASHBOARD -> DashboardScreen(
                onNavigate = { currentScreen = it }
            )
            Routes.UV_LIGHT_DASHBOARD -> UVLightDashboardScreen(
                onNavigate = { currentScreen = it }
            )
            Routes.ADD_PLANT -> AddPlantScreen(
                onNavigate = { currentScreen = it }
            )
            Routes.PLANT_CARE_RESULT -> PlantCareResultScreen(
                onNavigate = { currentScreen = it }
            )
            Routes.CARE_GUIDE_DETAIL -> CareGuideDetailScreen(
                onNavigate = { currentScreen = it }
            )
            Routes.METRIC_REPORT -> MetricReportScreen(
                onNavigate = { currentScreen = it }
            )
            Routes.TRENDS -> TrendsScreen(
                onNavigate = { currentScreen = it }
            )
            Routes.SETTINGS -> SettingsScreen(
                onNavigate = { currentScreen = it }
            )
            else -> HomeScreen(
                onNavigate = { currentScreen = it }
            )
        }
    }
}
