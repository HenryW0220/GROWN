package com.example.myapplication

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

// ==========================================
// Saved Plant model
// ==========================================
data class SavedPlant(
    val id: String = System.currentTimeMillis().toString(),
    val name: String,
    val nickname: String = "",
    val age: String = "",
    val aiAdvice: String = "",
    val healthScore: Int = (65..95).random(),
    val soilMoisture: String = "${(18..45).random()}%",
    val lightLevel: String = listOf("Low", "Medium", "High").random(),
    val humidity: String = "${(25..60).random()}%",
    val waterReminder: String = "Water within ${listOf(6, 12, 24, 48).random()} hours"
)

// ==========================================
// Activity log entry
// ==========================================
data class ActivityEntry(
    val timestamp: Long = System.currentTimeMillis(),
    val description: String,
    val icon: String = "info" // "plant", "water", "alert", "search", "info"
)

// ==========================================
// Care guide history entry
// ==========================================
data class CareGuideEntry(
    val plantName: String,
    val nickname: String = "",
    val age: String = "",
    val advice: String,
    val timestamp: Long = System.currentTimeMillis()
)

// ==========================================
// Alert entry
// ==========================================
data class AlertEntry(
    val title: String,
    val message: String,
    val severity: String = "warning", // "critical", "warning", "info"
    val timestamp: Long = System.currentTimeMillis()
)

// ==========================================
// Global shared state
// ==========================================
object PlantDataHolder {
    var plantName: String = ""
    var plantAge: String = ""
    var plantNickname: String = ""
}

object AppState {
    // Saved plants from AI care guide
    val savedPlants = mutableStateListOf<SavedPlant>()

    // Activity log
    val activityLog = mutableStateListOf<ActivityEntry>().apply {
        // Pre-populate with some initial activities
        add(ActivityEntry(description = "App launched for the first time", icon = "info"))
        add(ActivityEntry(description = "Environment sensors initialized", icon = "info"))
        add(ActivityEntry(description = "UV Light plant dashboard viewed", icon = "plant"))
    }

    // Care guide search history
    val careGuideHistory = mutableStateListOf<CareGuideEntry>()

    // Simulated alerts
    val alerts = mutableStateListOf<AlertEntry>().apply {
        add(AlertEntry(
            title = "Low Soil Moisture - UV Light Plant",
            message = "Soil moisture dropped to 22%. Water within 12 hours to prevent stress.",
            severity = "warning"
        ))
        add(AlertEntry(
            title = "Critical Humidity - No Light Plant",
            message = "Humidity at 28%, well below the 45% minimum. Consider using a humidifier.",
            severity = "critical"
        ))
        add(AlertEntry(
            title = "Light Exposure Optimal",
            message = "Natural Light plant receiving 8,400 lux average. No action needed.",
            severity = "info"
        ))
        add(AlertEntry(
            title = "Watering Overdue - No Light Plant",
            message = "Last watered 6 days ago. Immediate watering recommended.",
            severity = "critical"
        ))
        add(AlertEntry(
            title = "Temperature Rising",
            message = "Leaf temperature at 83.4°F, above optimal range of 72-78°F.",
            severity = "warning"
        ))
        add(AlertEntry(
            title = "CO₂ Levels Normal",
            message = "Average CO₂ at 416 ppm. Within acceptable indoor range.",
            severity = "info"
        ))
    }

    // Helper: add plant to shelf
    fun addPlantToShelf(name: String, nickname: String, age: String, advice: String) {
        val plant = SavedPlant(
            name = name,
            nickname = nickname,
            age = age,
            aiAdvice = advice
        )
        savedPlants.add(plant)
        activityLog.add(0, ActivityEntry(
            description = "Added \"${if (nickname.isNotBlank()) nickname else name}\" to plant shelf",
            icon = "plant"
        ))
    }

    // Helper: log care guide search
    fun logCareGuideSearch(name: String, nickname: String, age: String, advice: String) {
        careGuideHistory.add(0, CareGuideEntry(
            plantName = name,
            nickname = nickname,
            age = age,
            advice = advice
        ))
        activityLog.add(0, ActivityEntry(
            description = "Searched AI care guide for \"$name\"",
            icon = "search"
        ))
    }

    // Helper: log generic activity
    fun logActivity(description: String, icon: String = "info") {
        activityLog.add(0, ActivityEntry(description = description, icon = icon))
    }

    // Selected custom plant for dashboard
    var selectedCustomPlantId = mutableStateOf<String?>(null)
}

// ==========================================
// Plant light types for built-in dashboard screens
// ==========================================
enum class PlantLightType {
    UV_LIGHT, NATURAL_LIGHT, NO_LIGHT
}

object PlantDashboardHolder {
    var selectedPlant: PlantLightType = PlantLightType.UV_LIGHT
    var isCustomPlant: Boolean = false
}

// ==========================================
// Metric types for the report screens
// ==========================================
enum class MetricType {
    SOIL_MOISTURE, LIGHT_LEVEL, CO2, HUMIDITY
}

object MetricReportHolder {
    var selectedMetric: MetricType = MetricType.SOIL_MOISTURE
}

// ==========================================
// Dashboard data per plant type
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
            headerTitle = "UV Light",
            healthScore = 78,
            healthLabel = "Optimal",
            soilMoisture = "22%",
            lightLevel = "Low",
            humidity = "45%",
            waterReminder = "Water within 12 hours"
        )
        PlantLightType.NATURAL_LIGHT -> PlantDashboardData(
            headerTitle = "Natural Light",
            healthScore = 85,
            healthLabel = "Healthy",
            soilMoisture = "38%",
            lightLevel = "Medium",
            humidity = "52%",
            waterReminder = "Water within 24 hours"
        )
        PlantLightType.NO_LIGHT -> PlantDashboardData(
            headerTitle = "No Light",
            healthScore = 61,
            healthLabel = "Moderate",
            soilMoisture = "15%",
            lightLevel = "None",
            humidity = "28%",
            waterReminder = "Water within 6 hours"
        )
    }
}

fun getCustomPlantDashboardData(plant: SavedPlant): PlantDashboardData {
    val label = when {
        plant.healthScore >= 75 -> "Healthy"
        plant.healthScore >= 50 -> "Moderate"
        else -> "Stressed"
    }
    return PlantDashboardData(
        headerTitle = if (plant.nickname.isNotBlank()) plant.nickname else plant.name,
        healthScore = plant.healthScore,
        healthLabel = label,
        soilMoisture = plant.soilMoisture,
        lightLevel = plant.lightLevel,
        humidity = plant.humidity,
        waterReminder = plant.waterReminder
    )
}

// ==========================================
// Report data per plant type + metric type
// ==========================================
data class MetricReportData(
    val title: String,
    val score: Int,
    val scoreColor: androidx.compose.ui.graphics.Color,
    val trendPoints: List<Float>,
    val analysisText: String,
    val remindersText: String
)

fun getMetricReportData(plant: PlantLightType, metric: MetricType): MetricReportData {
    return when (plant) {
        PlantLightType.UV_LIGHT -> getUVMetricData(metric)
        PlantLightType.NATURAL_LIGHT -> getNaturalMetricData(metric)
        PlantLightType.NO_LIGHT -> getNoLightMetricData(metric)
    }
}

fun getCustomPlantMetricData(plant: SavedPlant, metric: MetricType): MetricReportData {
    val name = if (plant.nickname.isNotBlank()) plant.nickname else plant.name
    return when (metric) {
        MetricType.SOIL_MOISTURE -> MetricReportData(
            title = "Soil Moisture Report",
            score = 0,
            scoreColor = androidx.compose.ui.graphics.Color(0xFF66BB6A),
            trendPoints = listOf(0.5f, 0.48f, 0.45f, 0.43f, 0.4f, 0.38f, 0.36f, 0.35f),
            analysisText = "Soil moisture for $name is currently at ${plant.soilMoisture}. Levels have been gradually declining over the monitoring period.\n\nThe current reading is within the moderate range. Continue monitoring and water when levels approach 20%.\n\nNo abnormal sensor readings detected.",
            remindersText = "Monitor soil moisture levels regularly.\nRecommended action: ${plant.waterReminder}.\nTip: Maintain consistent watering schedule based on the plant's specific needs."
        )
        MetricType.LIGHT_LEVEL -> MetricReportData(
            title = "Light Level Report",
            score = 0,
            scoreColor = androidx.compose.ui.graphics.Color(0xFF66BB6A),
            trendPoints = listOf(0.3f, 0.45f, 0.6f, 0.65f, 0.6f, 0.5f, 0.35f, 0.2f),
            analysisText = "Current light level for $name: ${plant.lightLevel}.\n\nLight exposure follows a typical diurnal pattern. Ensure the plant receives adequate light for its species requirements.\n\nNo sudden fluctuations detected in the monitoring period.",
            remindersText = "Maintain current light exposure.\nTip: Adjust positioning based on seasonal changes in natural light availability.\nNext evaluation in 48 hours."
        )
        MetricType.CO2 -> MetricReportData(
            title = "Co2 Report",
            score = 0,
            scoreColor = androidx.compose.ui.graphics.Color(0xFF66BB6A),
            trendPoints = listOf(0.45f, 0.47f, 0.44f, 0.48f, 0.46f, 0.45f, 0.47f, 0.46f),
            analysisText = "CO₂ levels around $name are within normal indoor range.\n\nAverage concentration: ~410 ppm. Levels remain stable throughout the day with minor fluctuations.\n\nGood air circulation is being maintained.",
            remindersText = "CO₂ levels are normal.\nNo intervention required.\nTip: Ensure adequate ventilation for healthy gas exchange.\nMonitoring continues automatically."
        )
        MetricType.HUMIDITY -> MetricReportData(
            title = "Humidity Report",
            score = 0,
            scoreColor = androidx.compose.ui.graphics.Color(0xFF66BB6A),
            trendPoints = listOf(0.5f, 0.48f, 0.47f, 0.45f, 0.46f, 0.44f, 0.45f, 0.43f),
            analysisText = "Current humidity around $name: ${plant.humidity}.\n\nHumidity levels have been relatively stable. Ensure the environment maintains adequate moisture for the plant's needs.\n\nNo rapid changes detected.",
            remindersText = "Monitor humidity levels.\nTip: Group plants together to maintain local humidity naturally.\nConsider misting if levels drop below 40%."
        )
    }
}

// ==========================================
// Built-in metric data (UV, Natural, No Light)
// ==========================================
private fun getUVMetricData(metric: MetricType): MetricReportData {
    return when (metric) {
        MetricType.SOIL_MOISTURE -> MetricReportData(
            title = "Soil Moisture Report", score = 326,
            scoreColor = androidx.compose.ui.graphics.Color(0xFFE57373),
            trendPoints = listOf(0.6f, 0.55f, 0.5f, 0.45f, 0.42f, 0.38f, 0.35f, 0.3f),
            analysisText = "Soil moisture levels have declined steadily over the past 72 hours, dropping from 42% to 19%.\n\nThe rate of decline suggests increased transpiration. If conditions persist, soil moisture may fall below 15% within 24 hours.\n\nNo abnormal sensor fluctuations detected.",
            remindersText = "Moderate water stress detected.\nWater within 12 hours to restore soil moisture to 35–45%.\nLast watering: 4 days ago.\nTip: Ensure even saturation throughout the soil."
        )
        MetricType.LIGHT_LEVEL -> MetricReportData(
            title = "Light Level Report", score = 0,
            scoreColor = androidx.compose.ui.graphics.Color(0xFF66BB6A),
            trendPoints = listOf(0.3f, 0.35f, 0.4f, 0.38f, 0.42f, 0.4f, 0.38f, 0.35f),
            analysisText = "Average light exposure: 6,200 lux, slightly below optimal (8,000–12,000 lux).\n\nIntensity reduced during afternoon hours. No sudden fluctuations detected.\n\nProlonged sub-optimal exposure may decrease photosynthetic activity.",
            remindersText = "Light below optimal threshold.\nRelocate closer to light source or increase grow light by 1–2 hours.\nNext evaluation in 48 hours."
        )
        MetricType.CO2 -> MetricReportData(
            title = "Co2 Report", score = 1014,
            scoreColor = androidx.compose.ui.graphics.Color(0xFFE6C54A),
            trendPoints = listOf(0.5f, 0.52f, 0.48f, 0.55f, 0.53f, 0.5f, 0.52f, 0.54f),
            analysisText = "Average CO₂: 416 ppm. Peak: 428 ppm (evening), Low: 392 ppm (midday).\n\nLevels stable and consistent with indoor conditions. Minor evening increases from reduced ventilation.",
            remindersText = "CO₂ levels normal.\nAssess soil moisture if below 30%.\nNext evaluation in 24 hours."
        )
        MetricType.HUMIDITY -> MetricReportData(
            title = "Humidity Report", score = 132,
            scoreColor = androidx.compose.ui.graphics.Color(0xFFE57373),
            trendPoints = listOf(0.45f, 0.42f, 0.38f, 0.35f, 0.33f, 0.32f, 0.3f, 0.28f),
            analysisText = "Humidity at 32%, below optimal 45–60%. Consistently low over 72 hours.\n\nCombined with low soil moisture, this may accelerate stress responses.",
            remindersText = "Low humidity detected.\nUse a humidifier or relocate away from air vents.\nReassessment in 24 hours."
        )
    }
}

private fun getNaturalMetricData(metric: MetricType): MetricReportData {
    return when (metric) {
        MetricType.SOIL_MOISTURE -> MetricReportData(
            title = "Soil Moisture Report", score = 0,
            scoreColor = androidx.compose.ui.graphics.Color(0xFF66BB6A),
            trendPoints = listOf(0.4f, 0.42f, 0.45f, 0.43f, 0.4f, 0.38f, 0.36f, 0.38f),
            analysisText = "Soil moisture fluctuating between 34–42%. Current 38% is within acceptable range.\n\nEvaporation consistent with indirect natural sunlight. Watering may be needed within 36 hours.",
            remindersText = "Soil moisture acceptable.\nMonitor and water if levels drop below 30%.\nLast watered: 2 days ago."
        )
        MetricType.LIGHT_LEVEL -> MetricReportData(
            title = "Light Level Report", score = 0,
            scoreColor = androidx.compose.ui.graphics.Color(0xFF66BB6A),
            trendPoints = listOf(0.3f, 0.5f, 0.7f, 0.75f, 0.7f, 0.5f, 0.3f, 0.15f),
            analysisText = "Light follows natural diurnal cycle. Peak: 9,800 lux midday. Average: 8,400 lux (within optimal 8,000–12,000 lux).\n\nExposure duration: ~10 hours.",
            remindersText = "Light levels optimal.\nRotate plant 90° weekly for even distribution.\nNext evaluation in 48 hours."
        )
        MetricType.CO2 -> MetricReportData(
            title = "Co2 Report", score = 0,
            scoreColor = androidx.compose.ui.graphics.Color(0xFF66BB6A),
            trendPoints = listOf(0.45f, 0.43f, 0.4f, 0.38f, 0.42f, 0.45f, 0.48f, 0.46f),
            analysisText = "Average CO₂: 405 ppm. Healthy inverse correlation with light intensity indicates active photosynthesis.",
            remindersText = "CO₂ normal. Maintain adequate ventilation."
        )
        MetricType.HUMIDITY -> MetricReportData(
            title = "Humidity Report", score = 0,
            scoreColor = androidx.compose.ui.graphics.Color(0xFF66BB6A),
            trendPoints = listOf(0.5f, 0.52f, 0.55f, 0.53f, 0.52f, 0.5f, 0.48f, 0.52f),
            analysisText = "Humidity at 52%, within optimal 45–60%. Stable over 72 hours with good air circulation.",
            remindersText = "Humidity optimal. Group plants together to maintain levels naturally."
        )
    }
}

private fun getNoLightMetricData(metric: MetricType): MetricReportData {
    return when (metric) {
        MetricType.SOIL_MOISTURE -> MetricReportData(
            title = "Soil Moisture Report", score = 482,
            scoreColor = androidx.compose.ui.graphics.Color(0xFFE57373),
            trendPoints = listOf(0.35f, 0.3f, 0.25f, 0.22f, 0.18f, 0.16f, 0.15f, 0.13f),
            analysisText = "Soil moisture dropped from 35% to 15% over 72 hours. Plant entering severe stress zone.\n\nImmediate watering recommended to prevent root damage.",
            remindersText = "Severe water stress.\nWater immediately to restore above 30%.\nLast watered: 6 days ago."
        )
        MetricType.LIGHT_LEVEL -> MetricReportData(
            title = "Light Level Report", score = 89,
            scoreColor = androidx.compose.ui.graphics.Color(0xFFE57373),
            trendPoints = listOf(0.05f, 0.04f, 0.05f, 0.03f, 0.04f, 0.05f, 0.04f, 0.03f),
            analysisText = "Light near zero (~50 lux), far below 2,000 lux minimum.\n\nProlonged absence will cause yellowing and leaf drop.",
            remindersText = "Critical: relocate to indirect light or use a grow light 6–8 hours daily."
        )
        MetricType.CO2 -> MetricReportData(
            title = "Co2 Report", score = 1180,
            scoreColor = androidx.compose.ui.graphics.Color(0xFFE57373),
            trendPoints = listOf(0.55f, 0.58f, 0.6f, 0.62f, 0.6f, 0.63f, 0.65f, 0.64f),
            analysisText = "CO₂ elevated at 445 ppm. Without photosynthesis, plant only respires. Poor ventilation may worsen this.",
            remindersText = "Improve air circulation. Relocate to a ventilated area."
        )
        MetricType.HUMIDITY -> MetricReportData(
            title = "Humidity Report", score = 245,
            scoreColor = androidx.compose.ui.graphics.Color(0xFFE57373),
            trendPoints = listOf(0.35f, 0.32f, 0.3f, 0.28f, 0.27f, 0.25f, 0.24f, 0.22f),
            analysisText = "Humidity critically low at 28%. Declining steadily. Leaf browning may occur within 48 hours.",
            remindersText = "Use a humidifier or pebble tray. Mist leaves every 12 hours.\nReassessment in 12 hours."
        )
    }
}
