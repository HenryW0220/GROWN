package com.example.myapplication

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.DarkText
import com.example.myapplication.ui.theme.PageBackground

// ==========================================
// Metric Report Screen
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetricReportScreen(onNavigate: (String) -> Unit = {}) {
    val plantType = PlantDashboardHolder.selectedPlant
    val metricType = MetricReportHolder.selectedMetric
    val data = getMetricReportData(plantType, metricType)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(data.title, fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { onNavigate(Routes.UV_LIGHT_DASHBOARD) }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PageBackground)
            )
        },
        containerColor = PageBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            // Trends Section
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Trends",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DarkText
                        )
                        if (data.score > 0) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = data.scoreColor
                            ) {
                                Text(
                                    text = "${data.score}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    ReportTrendChart(
                        dataPoints = data.trendPoints,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Current Trends Analysis Section
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Current Trends Analysis",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = data.analysisText,
                        fontSize = 13.sp,
                        lineHeight = 20.sp,
                        color = Color(0xFF555555)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Reminders / Notifications Section
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "REMINDERS / NOTIFICATIONS",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = data.remindersText,
                        fontSize = 13.sp,
                        lineHeight = 20.sp,
                        color = Color(0xFF555555)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ==========================================
// Trend Chart for report screens
// ==========================================
@Composable
fun ReportTrendChart(
    dataPoints: List<Float>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val spacing = width / (dataPoints.size - 1)
        val chartPadding = 8f

        // Horizontal grid lines
        for (i in 0..4) {
            val y = chartPadding + (height - chartPadding * 2) * i / 4
            drawLine(
                color = Color(0xFFE8E8E0),
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 1f
            )
        }

        // Line path
        val path = Path()
        dataPoints.forEachIndexed { index, point ->
            val x = index * spacing
            val y = chartPadding + (height - chartPadding * 2) * (1 - point)
            if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        drawPath(path = path, color = Color(0xFF333333), style = Stroke(width = 2.5f))

        // Data points
        dataPoints.forEachIndexed { index, point ->
            val x = index * spacing
            val y = chartPadding + (height - chartPadding * 2) * (1 - point)
            drawCircle(color = Color(0xFF333333), radius = 4f, center = Offset(x, y))
        }
    }
}

// ==========================================
// Preview
// ==========================================
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MetricReportScreenPreview() {
    MaterialTheme {
        MetricReportScreen()
    }
}
