package com.example.myapplication

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color

// ==========================================
// Data classes
// ==========================================
data class SavedPlant(
    val id: String = "",
    val name: String = "",
    val nickname: String = "",
    val age: String = ""
)

data class AlertEntry(
    val title: String,
    val message: String,
    val severity: String = "info"
)

data class ActivityEntry(
    val description: String,
    val icon: String = "info"
)

// ==========================================
// Enums
// ==========================================
enum class PlantLightType {
    UV_LIGHT, NATURAL_LIGHT, NO_LIGHT
}

enum class MetricType {
    SOIL_MOISTURE, LIGHT_LEVEL, CO2, HUMIDITY
}

// ==========================================
// Global App State
// ==========================================
object AppState {
    val savedPlants = mutableStateListOf<SavedPlant>()
    val alerts = mutableStateListOf(
        AlertEntry("Low Soil Moisture", "UV Light plant needs watering soon", "warning"),
        AlertEntry("High Temperature", "Leaf temperature above optimal range", "critical")
    )
    val activityLog = mutableStateListOf(
        ActivityEntry("Viewed UV Light dashboard", "plant"),
        ActivityEntry("Added new plant: Rose", "plant"),
        ActivityEntry("Watering reminder sent", "water")
    )
    val selectedCustomPlantId = mutableStateOf("")

    fun logActivity(description: String, icon: String = "info") {
        activityLog.add(0, ActivityEntry(description, icon))
        if (activityLog.size > 50) {
            activityLog.removeAt(activityLog.lastIndex)
        }
    }

    fun addPlantToShelf(name: String, nickname: String, age: String, advice: String) {
        val id = "plant_${System.currentTimeMillis()}"
        savedPlants.add(SavedPlant(id = id, name = name, nickname = nickname, age = age))
        logActivity("Added $name to shelf", "plant")
    }

    fun logCareGuideSearch(name: String, nickname: String, age: String, advice: String) {
        logActivity("Searched care guide for $name", "search")
    }
}

// ==========================================
// Navigation state holders
// ==========================================
object PlantDashboardHolder {
    var selectedPlant: PlantLightType = PlantLightType.UV_LIGHT
    var isCustomPlant: Boolean = false
}

object MetricReportHolder {
    var selectedMetric: MetricType = MetricType.SOIL_MOISTURE
}

// ==========================================
// Dashboard data model + helpers
// ==========================================
data class PlantDashboardData(
    val headerTitle: String,
    val healthScore: Int,
    val healthLabel: String,
    val soilMoisture: String,
    val lightLevel: String,
    val humidity: String,
    val waterReminder: String
)

fun getPlantDashboardData(type: PlantLightType): PlantDashboardData {
    return when (type) {
        PlantLightType.UV_LIGHT -> PlantDashboardData(
            headerTitle = "UV Light", healthScore = 78, healthLabel = "Optimal",
            soilMoisture = "22%", lightLevel = "Low", humidity = "45%",
            waterReminder = "Water within 12 hours"
        )
        PlantLightType.NATURAL_LIGHT -> PlantDashboardData(
            headerTitle = "Natural Light", healthScore = 85, healthLabel = "Healthy",
            soilMoisture = "40%", lightLevel = "Medium", humidity = "55%",
            waterReminder = "Water in 2 days"
        )
        PlantLightType.NO_LIGHT -> PlantDashboardData(
            headerTitle = "No Light", healthScore = 60, healthLabel = "Needs Attention",
            soilMoisture = "18%", lightLevel = "Very Low", humidity = "35%",
            waterReminder = "Water immediately"
        )
    }
}

fun getCustomPlantDashboardData(plant: SavedPlant): PlantDashboardData {
    val displayName = if (plant.nickname.isNotBlank()) plant.nickname else plant.name
    return PlantDashboardData(
        headerTitle = displayName, healthScore = 93, healthLabel = "Excellent",
        soilMoisture = "38%", lightLevel = "Good", humidity = "50%",
        waterReminder = "Water in 1 day"
    )
}

// ==========================================
// Metric report data model + helpers
// ==========================================
data class MetricReportData(
    val title: String,
    val score: Int,
    val scoreColor: Color,
    val trendPoints: List<Float>,
    val analysisText: String,
    val remindersText: String
)

fun getMetricReportData(type: PlantLightType, metric: MetricType): MetricReportData {
    return when (metric) {
        MetricType.SOIL_MOISTURE -> MetricReportData(
            title = "Soil Moisture", score = 24, scoreColor = Color(0xFFE6C54A),
            trendPoints = listOf(0.35f, 0.32f, 0.28f, 0.25f, 0.24f, 0.22f, 0.20f),
            analysisText = "Soil moisture has been steadily decreasing over the past week. The current level of 24% is below the optimal range of 35-50%. This indicates the plant needs watering soon to prevent stress.",
            remindersText = "• Water your plant within the next 12 hours\n• Check drainage to ensure proper water retention\n• Consider adding mulch to retain moisture"
        )
        MetricType.LIGHT_LEVEL -> MetricReportData(
            title = "Light Level", score = 62, scoreColor = Color(0xFFE6C54A),
            trendPoints = listOf(0.4f, 0.45f, 0.5f, 0.48f, 0.52f, 0.55f, 0.50f),
            analysisText = "Light exposure is below the optimal range of 8,000-12,000 lux. Current levels around 6,200 lux may slow growth. Consider repositioning the plant closer to a light source.",
            remindersText = "• Move plant to a brighter location\n• Consider supplemental grow lights\n• Rotate plant weekly for even light exposure"
        )
        MetricType.CO2 -> MetricReportData(
            title = "CO2 Levels", score = 0, scoreColor = Color(0xFF66BB6A),
            trendPoints = listOf(0.4f, 0.6f, 0.3f, 0.7f, 0.5f, 0.8f, 0.6f),
            analysisText = "CO2 levels are within normal indoor range. Adequate ventilation is being maintained. No immediate action is needed.",
            remindersText = "• Maintain good air circulation\n• Open windows periodically for fresh air\n• Monitor levels during winter when ventilation decreases"
        )
        MetricType.HUMIDITY -> MetricReportData(
            title = "Humidity", score = 45, scoreColor = Color(0xFFE6C54A),
            trendPoints = listOf(0.50f, 0.48f, 0.45f, 0.42f, 0.40f, 0.38f, 0.45f),
            analysisText = "Humidity is at 45%, which is near the lower end of the optimal 45-60% range. Slight fluctuations may cause leaf tip browning. Consider using a humidifier nearby.",
            remindersText = "• Use a pebble tray or humidifier nearby\n• Group plants together to increase local humidity\n• Mist leaves in the morning if needed"
        )
    }
}

fun getCustomPlantMetricData(plant: SavedPlant, metric: MetricType): MetricReportData {
    val displayName = if (plant.nickname.isNotBlank()) plant.nickname else plant.name
    return when (metric) {
        MetricType.SOIL_MOISTURE -> MetricReportData(
            title = "$displayName - Soil Moisture", score = 38, scoreColor = Color(0xFF66BB6A),
            trendPoints = listOf(0.30f, 0.35f, 0.38f, 0.40f, 0.38f, 0.36f, 0.38f),
            analysisText = "Soil moisture for $displayName is at a healthy 38%, within the optimal range.",
            remindersText = "• Continue current watering schedule\n• Check moisture before watering"
        )
        MetricType.LIGHT_LEVEL -> MetricReportData(
            title = "$displayName - Light Level", score = 70, scoreColor = Color(0xFF66BB6A),
            trendPoints = listOf(0.55f, 0.60f, 0.65f, 0.70f, 0.68f, 0.72f, 0.70f),
            analysisText = "Light exposure for $displayName is good. Current placement provides adequate light.",
            remindersText = "• Maintain current position\n• Rotate weekly for even growth"
        )
        MetricType.CO2 -> MetricReportData(
            title = "$displayName - CO2 Levels", score = 0, scoreColor = Color(0xFF66BB6A),
            trendPoints = listOf(0.5f, 0.55f, 0.45f, 0.60f, 0.50f, 0.55f, 0.52f),
            analysisText = "CO2 levels around $displayName are normal.",
            remindersText = "• Maintain good ventilation"
        )
        MetricType.HUMIDITY -> MetricReportData(
            title = "$displayName - Humidity", score = 50, scoreColor = Color(0xFF66BB6A),
            trendPoints = listOf(0.45f, 0.48f, 0.50f, 0.52f, 0.50f, 0.48f, 0.50f),
            analysisText = "Humidity around $displayName is at an ideal 50%.",
            remindersText = "• Current humidity is perfect\n• Monitor during dry seasons"
        )
    }
}
