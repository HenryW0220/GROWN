package com.example.myapplication

// Shared data holder for plant information (Add Plant flow)
object PlantDataHolder {
    var plantName: String = ""
    var plantAge: String = ""
    var plantNickname: String = ""
}

// ==========================================
// Plant light types for dashboard screens
// ==========================================
enum class PlantLightType {
    UV_LIGHT, NATURAL_LIGHT, NO_LIGHT
}

// Shared holder for which plant dashboard to display
object PlantDashboardHolder {
    var selectedPlant: PlantLightType = PlantLightType.UV_LIGHT
}

// ==========================================
// Metric types for the report screens
// ==========================================
enum class MetricType {
    SOIL_MOISTURE, LIGHT_LEVEL, CO2, HUMIDITY
}

// Shared holder for which metric report to display
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

fun getMetricReportData(
    plant: PlantLightType,
    metric: MetricType
): MetricReportData {
    return when (plant) {
        PlantLightType.UV_LIGHT -> getUVMetricData(metric)
        PlantLightType.NATURAL_LIGHT -> getNaturalMetricData(metric)
        PlantLightType.NO_LIGHT -> getNoLightMetricData(metric)
    }
}

// --- UV Light report data ---
private fun getUVMetricData(metric: MetricType): MetricReportData {
    return when (metric) {
        MetricType.SOIL_MOISTURE -> MetricReportData(
            title = "Soil Moisture Report",
            score = 326,
            scoreColor = androidx.compose.ui.graphics.Color(0xFFE57373),
            trendPoints = listOf(0.6f, 0.55f, 0.5f, 0.45f, 0.42f, 0.38f, 0.35f, 0.3f),
            analysisText = "Soil moisture levels have declined steadily over the past 72 hours, dropping from 42% to 19% volumetric water content. The current reading places this plant in the lower moderate stress range.\n\nThe rate of decline suggests increased transpiration relative to recent days, potentially influenced by elevated leaf temperature and consistent light exposure.\n\nIf current conditions persist, projected soil moisture is expected to fall below 15% within the next 24 hours, entering severe stress territory.\n\nNo abnormal sensor fluctuations detected.\nData consistency is within expected range.",
            remindersText = "Moderate water stress detected.\nRecommended action: Water within the next 12 hours to restore soil moisture to 35–45%.\nLast watering event: 4 days ago.\nReminder scheduled: Tomorrow at 9:00 AM if no intervention is logged.\nTip: Ensure even saturation throughout the soil column. Avoid surface-only watering."
        )
        MetricType.LIGHT_LEVEL -> MetricReportData(
            title = "Light Level Report",
            score = 0,
            scoreColor = androidx.compose.ui.graphics.Color(0xFF66BB6A),
            trendPoints = listOf(0.3f, 0.35f, 0.4f, 0.38f, 0.42f, 0.4f, 0.38f, 0.35f),
            analysisText = "Average light exposure over the past 24 hours has remained at 6,200 lux, slightly below the optimal range for this species (8,000–12,000 lux).\n\nLight intensity has been consistent but reduced during afternoon hours, likely due to indirect positioning relative to the primary light source.\n\nNo sudden light fluctuations detected.\nDuration of exposure remains stable at approximately 8 hours per day.\n\nIf current exposure persists, photosynthetic activity may decrease over extended periods, particularly under water stress conditions.",
            remindersText = "Light levels below optimal threshold.\nRecommended action: Relocate plant closer to natural light source or increase artificial grow light duration by 1–2 hours daily.\nNext evaluation scheduled in 48 hours.\nTip: Maintain consistent daily light exposure to prevent stress-related metabolic shifts."
        )
        MetricType.CO2 -> MetricReportData(
            title = "Co2 Report",
            score = 1014,
            scoreColor = androidx.compose.ui.graphics.Color(0xFFE6C54A),
            trendPoints = listOf(0.5f, 0.52f, 0.48f, 0.55f, 0.53f, 0.5f, 0.52f, 0.54f),
            analysisText = "24-Hour Trend Analysis\nAverage CO₂ concentration over the past 24 hours: 416 ppm.\n\nPeak concentration: 428 ppm (evening).\nLowest concentration: 392 ppm (midday).\nCO₂ levels remain stable and consistent with typical indoor atmospheric conditions. No prolonged elevation or depletion events detected.\n\nMinor evening increases may reflect reduced ventilation or increased respiration in enclosed conditions.",
            remindersText = "Elevated leaf temperatures detected.\nRecommended action: Assess soil moisture and consider watering if below 30%.\nOptional intervention: Reduce direct light intensity during peak hours to minimize thermal stress.\nNext evaluation scheduled in 24 hours.\nMonitoring continues in real time."
        )
        MetricType.HUMIDITY -> MetricReportData(
            title = "Humidity Report",
            score = 132,
            scoreColor = androidx.compose.ui.graphics.Color(0xFFE57373),
            trendPoints = listOf(0.45f, 0.42f, 0.38f, 0.35f, 0.33f, 0.32f, 0.3f, 0.28f),
            analysisText = "Ambient humidity is currently 32%, below the optimal range of 45–60% for this species.\n\nHumidity levels have remained consistently low over the past 72 hours, increasing evaporative demand on leaf tissue.\n\nCombined with reduced soil moisture, low humidity may accelerate stress response pathways.\n\nNo rapid environmental fluctuations detected.",
            remindersText = "Low humidity environment detected.\nRecommended action: Increase ambient humidity using a humidifier or relocate plant away from direct air vents.\nReassessment scheduled in 24 hours.\nTip: Sustained low humidity may lead to leaf edge browning or reduced growth rate."
        )
    }
}

// --- Natural Light report data ---
private fun getNaturalMetricData(metric: MetricType): MetricReportData {
    return when (metric) {
        MetricType.SOIL_MOISTURE -> MetricReportData(
            title = "Soil Moisture Report",
            score = 0,
            scoreColor = androidx.compose.ui.graphics.Color(0xFF66BB6A),
            trendPoints = listOf(0.4f, 0.42f, 0.45f, 0.43f, 0.4f, 0.38f, 0.36f, 0.38f),
            analysisText = "Soil moisture has remained within the moderate range over the past 72 hours, fluctuating between 34% and 42%. The current reading of 38% places this plant within the acceptable range, though trending slightly downward.\n\nEvaporation rates are consistent with the plant's exposure to indirect natural sunlight. Soil drainage appears normal with no signs of waterlogging.\n\nProjected moisture levels suggest watering may be needed within the next 36 hours to maintain optimal hydration.",
            remindersText = "Soil moisture is within acceptable range.\nRecommended action: Monitor over the next 24 hours. Water if levels drop below 30%.\nLast watering event: 2 days ago.\nTip: Natural light plants benefit from consistent, moderate watering rather than heavy saturation."
        )
        MetricType.LIGHT_LEVEL -> MetricReportData(
            title = "Light Level Report",
            score = 0,
            scoreColor = androidx.compose.ui.graphics.Color(0xFF66BB6A),
            trendPoints = listOf(0.3f, 0.5f, 0.7f, 0.75f, 0.7f, 0.5f, 0.3f, 0.15f),
            analysisText = "Light exposure follows a natural diurnal cycle, peaking at approximately 9,800 lux during midday hours. Average daily exposure over the past 24 hours: 8,400 lux.\n\nThis falls within the optimal range for this species (8,000–12,000 lux). Morning and late afternoon readings are naturally lower due to the sun's angle.\n\nCloud cover caused a brief 20% reduction in light intensity during early afternoon, but overall exposure duration remained at approximately 10 hours.",
            remindersText = "Light levels are within optimal range.\nNo immediate action required.\nTip: Rotate the plant 90° weekly to ensure even light distribution across all foliage.\nNext evaluation scheduled in 48 hours."
        )
        MetricType.CO2 -> MetricReportData(
            title = "Co2 Report",
            score = 0,
            scoreColor = androidx.compose.ui.graphics.Color(0xFF66BB6A),
            trendPoints = listOf(0.45f, 0.43f, 0.4f, 0.38f, 0.42f, 0.45f, 0.48f, 0.46f),
            analysisText = "24-Hour Trend Analysis\nAverage CO₂ concentration: 405 ppm, consistent with well-ventilated indoor conditions near a window.\n\nPeak concentration: 418 ppm (early morning).\nLowest concentration: 388 ppm (midday, likely due to photosynthetic uptake with ample light).\n\nCO₂ levels show a healthy inverse correlation with light intensity, indicating active photosynthesis during daylight hours.",
            remindersText = "CO₂ levels are within normal range.\nNo intervention required.\nTip: Ensure adequate ventilation near the plant to maintain healthy gas exchange.\nMonitoring continues automatically."
        )
        MetricType.HUMIDITY -> MetricReportData(
            title = "Humidity Report",
            score = 0,
            scoreColor = androidx.compose.ui.graphics.Color(0xFF66BB6A),
            trendPoints = listOf(0.5f, 0.52f, 0.55f, 0.53f, 0.52f, 0.5f, 0.48f, 0.52f),
            analysisText = "Ambient humidity is currently 52%, within the optimal range of 45–60% for this species.\n\nHumidity has remained stable over the past 72 hours, benefiting from the plant's proximity to a window with moderate air circulation.\n\nNo rapid fluctuations or dry spells detected. Current conditions support healthy transpiration rates.",
            remindersText = "Humidity levels are optimal.\nNo immediate action required.\nTip: Grouping plants together can help maintain local humidity levels naturally.\nReassessment scheduled in 48 hours."
        )
    }
}

// --- No Light report data ---
private fun getNoLightMetricData(metric: MetricType): MetricReportData {
    return when (metric) {
        MetricType.SOIL_MOISTURE -> MetricReportData(
            title = "Soil Moisture Report",
            score = 482,
            scoreColor = androidx.compose.ui.graphics.Color(0xFFE57373),
            trendPoints = listOf(0.35f, 0.3f, 0.25f, 0.22f, 0.18f, 0.16f, 0.15f, 0.13f),
            analysisText = "Soil moisture has dropped significantly over the past 72 hours, from 35% down to 15%. The plant is entering the severe stress zone.\n\nWithout light, the plant's metabolic rate is reduced, but soil moisture continues to decline due to ambient evaporation and root absorption.\n\nThe rate of moisture loss is slower than light-exposed plants but still critical given the already low starting point.\n\nImmediate watering is recommended to prevent irreversible root damage.",
            remindersText = "Severe water stress detected.\nRecommended action: Water immediately to restore soil moisture above 30%.\nLast watering event: 6 days ago.\nReminder: Overdue watering alert active.\nTip: Low-light plants need less frequent but consistent watering. Do not allow soil to fully dry out."
        )
        MetricType.LIGHT_LEVEL -> MetricReportData(
            title = "Light Level Report",
            score = 89,
            scoreColor = androidx.compose.ui.graphics.Color(0xFFE57373),
            trendPoints = listOf(0.05f, 0.04f, 0.05f, 0.03f, 0.04f, 0.05f, 0.04f, 0.03f),
            analysisText = "Light exposure has been near zero over the past 72 hours, averaging approximately 50 lux. This is far below the minimum threshold of 2,000 lux for low-light tolerant species.\n\nThe plant is receiving only ambient room lighting with no direct or indirect natural light exposure.\n\nProlonged absence of light will slow growth, reduce chlorophyll production, and may cause leaf yellowing and drop over time.",
            remindersText = "Critical: Light exposure far below minimum threshold.\nRecommended action: Relocate plant to an area with at least indirect light, or introduce a grow light for 6–8 hours daily.\nTip: Even shade-tolerant species need some light exposure to maintain basic metabolic functions."
        )
        MetricType.CO2 -> MetricReportData(
            title = "Co2 Report",
            score = 1180,
            scoreColor = androidx.compose.ui.graphics.Color(0xFFE57373),
            trendPoints = listOf(0.55f, 0.58f, 0.6f, 0.62f, 0.6f, 0.63f, 0.65f, 0.64f),
            analysisText = "24-Hour Trend Analysis\nAverage CO₂ concentration: 445 ppm, elevated compared to well-ventilated areas.\n\nPeak concentration: 462 ppm (overnight).\nLowest concentration: 430 ppm (afternoon).\n\nWithout photosynthetic activity due to lack of light, the plant is only respiring, contributing to slightly elevated local CO₂ levels. Poor ventilation in the plant's location may be exacerbating this trend.",
            remindersText = "Elevated CO₂ levels detected in the plant's vicinity.\nRecommended action: Improve air circulation around the plant.\nOptional: Relocate to a better-ventilated area.\nTip: Without light, plants cannot photosynthesize and will only produce CO₂ through respiration."
        )
        MetricType.HUMIDITY -> MetricReportData(
            title = "Humidity Report",
            score = 245,
            scoreColor = androidx.compose.ui.graphics.Color(0xFFE57373),
            trendPoints = listOf(0.35f, 0.32f, 0.3f, 0.28f, 0.27f, 0.25f, 0.24f, 0.22f),
            analysisText = "Ambient humidity is critically low at 28%, well below the optimal range of 45–60% for this species.\n\nHumidity has been declining steadily over the past 72 hours. The plant's enclosed, low-ventilation environment combined with indoor heating is accelerating moisture loss from the air.\n\nLeaf tips may begin browning within 48 hours if conditions are not improved.",
            remindersText = "Critical low humidity detected.\nRecommended action: Place a humidifier nearby or set the plant on a pebble tray with water.\nAlternative: Mist leaves lightly every 12 hours.\nReassessment scheduled in 12 hours.\nTip: Low-light environments often have poor air circulation, which compounds humidity issues."
        )
    }
}
