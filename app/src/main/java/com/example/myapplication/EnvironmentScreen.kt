package com.example.myapplication

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.DarkText
import com.example.myapplication.ui.theme.GrayText
import com.example.myapplication.ui.theme.PageBackground

// ==========================================
// Color definitions for Environment screen
// ==========================================
val WarningYellow = Color(0xFFE6C54A)
val StatusOrange = Color(0xFFE8A040)
val StatusGreen = Color(0xFF66BB6A)
val StatusBlue = Color(0xFF42A5F5)
val BarGreen = Color(0xFF81C784)
val BarYellow = Color(0xFFFFD54F)
val BarOrange = Color(0xFFFFB74D)
val BarRed = Color(0xFFE57373)

// ==========================================
// Environment Screen
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnvironmentScreen(onNavigate: (String) -> Unit = {}) {
    Scaffold(
        bottomBar = { BottomNavBar(currentRoute = Routes.ENVIRONMENT, onNavigate = onNavigate) },
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
                text = "Environment",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )

            Spacer(modifier = Modifier.height(16.dp))

            PlantInfoHeader()
            Spacer(modifier = Modifier.height(16.dp))

            SoilMoistureCard()
            Spacer(modifier = Modifier.height(12.dp))

            LightExposureCard()
            Spacer(modifier = Modifier.height(12.dp))

            LeafTemperatureCard()
            Spacer(modifier = Modifier.height(12.dp))

            HumidityCard()
            Spacer(modifier = Modifier.height(16.dp))

            InsightRow(
                icon = Icons.Outlined.WaterDrop,
                boldText = "Low humidity",
                normalText = " may be increasing transpiration rate."
            )
            Spacer(modifier = Modifier.height(8.dp))

            InsightRow(
                icon = Icons.Outlined.LightMode,
                boldText = "Moderate stress",
                normalText = " correlates with light below optimal."
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun PlantInfoHeader() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Aloe Vera Plant", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = DarkText)
            Text("  |  Indoors", fontSize = 16.sp, color = GrayText)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = RoundedCornerShape(4.dp), color = WarningYellow) {
                Text("Mild Stress", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.White,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text("Last Updated: 2 min ago", fontSize = 12.sp, color = GrayText)
        }
    }
}

@Composable
fun SoilMoistureCard() {
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), color = Color.White, shadowElevation = 1.dp) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Text("Soil Moisture", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = DarkText)
                Text("24%", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = DarkText)
            }
            Text("Status: Moderate Stress", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = StatusOrange)
            Spacer(modifier = Modifier.height(10.dp))
            SoilMoistureProgressBar(value = 0.24f, threshold = 0.35f)
            Spacer(modifier = Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Optimal: 35-50%", fontSize = 11.sp, color = GrayText)
                Text("Below Threshold", fontSize = 11.sp, color = GrayText)
            }
        }
    }
}

@Composable
fun SoilMoistureProgressBar(value: Float, threshold: Float) {
    Canvas(modifier = Modifier.fillMaxWidth().height(12.dp)) {
        val w = size.width; val h = size.height
        drawRoundRect(color = Color(0xFFE0E0D8), cornerRadius = CornerRadius(h / 2), size = Size(w, h))
        drawRoundRect(color = BarGreen, cornerRadius = CornerRadius(h / 2), size = Size(w * threshold, h))
        drawRect(color = BarYellow, topLeft = Offset(w * threshold, 0f), size = Size(w * 0.35f, h))
        val tx = w * value
        drawCircle(color = Color.White, radius = h * 0.7f, center = Offset(tx, h / 2))
        drawCircle(color = Color(0xFF999999), radius = h * 0.7f, center = Offset(tx, h / 2), style = Stroke(width = 2f))
    }
}

@Composable
fun LightExposureCard() {
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), color = Color.White, shadowElevation = 1.dp) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Text("Light Exposure", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = DarkText)
                Row(verticalAlignment = Alignment.Bottom) {
                    Text("6,200", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = DarkText)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("lux", fontSize = 14.sp, color = GrayText, modifier = Modifier.padding(bottom = 3.dp))
                }
            }
            Text("Status: Below Optimal", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = StatusOrange)
            Spacer(modifier = Modifier.height(10.dp))
            LightExposureChart(modifier = Modifier.fillMaxWidth().height(50.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text("8,000-12,000 lux", fontSize = 11.sp, color = GrayText, modifier = Modifier.align(Alignment.End))
            Spacer(modifier = Modifier.height(4.dp))
            Text("Optimal: 72-78\u00B0F", fontSize = 11.sp, color = GrayText)
        }
    }
}

@Composable
fun LightExposureChart(modifier: Modifier = Modifier) {
    val pts = listOf(0.2f, 0.25f, 0.22f, 0.3f, 0.28f, 0.35f, 0.5f, 0.48f, 0.52f)
    Canvas(modifier = modifier) {
        val w = size.width; val h = size.height; val sp = w / (pts.size - 1)
        val fill = Path().apply {
            moveTo(0f, h)
            pts.forEachIndexed { i, p -> lineTo(i * sp, h * (1 - p)) }
            lineTo(w, h); close()
        }
        drawPath(path = fill, color = Color(0xFFE8F5E9))
        val line = Path()
        pts.forEachIndexed { i, p -> val x = i * sp; val y = h * (1 - p); if (i == 0) line.moveTo(x, y) else line.lineTo(x, y) }
        drawPath(path = line, color = StatusGreen, style = Stroke(width = 2f))
    }
}

@Composable
fun LeafTemperatureCard() {
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), color = Color.White, shadowElevation = 1.dp) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Text("Leaf Temperature", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = DarkText)
                Row(verticalAlignment = Alignment.Bottom) {
                    Text("83.4", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = DarkText)
                    Text("\u00B0F", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DarkText, modifier = Modifier.padding(bottom = 2.dp))
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Optimal: 72-78\u00B0F", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = StatusOrange)
                Text("+6\u00B0F Above Ambient", fontSize = 12.sp, color = GrayText)
            }
            Spacer(modifier = Modifier.height(10.dp))
            TemperatureGradientBar(value = 0.78f)
            Spacer(modifier = Modifier.height(6.dp))
            Text("Optimal: 45-60%", fontSize = 11.sp, color = GrayText)
        }
    }
}

@Composable
fun TemperatureGradientBar(value: Float) {
    Canvas(modifier = Modifier.fillMaxWidth().height(12.dp)) {
        val w = size.width; val h = size.height
        drawRoundRect(brush = Brush.horizontalGradient(listOf(StatusBlue, BarGreen, BarYellow, BarOrange, BarRed)), cornerRadius = CornerRadius(h / 2), size = Size(w, h))
        val tx = w * value
        drawCircle(color = Color.White, radius = h * 0.7f, center = Offset(tx, h / 2))
        drawCircle(color = Color(0xFF999999), radius = h * 0.7f, center = Offset(tx, h / 2), style = Stroke(width = 2f))
    }
}

@Composable
fun HumidityCard() {
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), color = Color.White, shadowElevation = 1.dp) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Text("Humidity", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = DarkText)
                Text("32%", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = DarkText)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Optimal: 45-60%", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = StatusOrange)
                Text("Status: Low", fontSize = 12.sp, color = GrayText)
            }
            Spacer(modifier = Modifier.height(10.dp))
            HumidityProgressBar(value = 0.32f)
        }
    }
}

@Composable
fun HumidityProgressBar(value: Float) {
    Canvas(modifier = Modifier.fillMaxWidth().height(12.dp)) {
        val w = size.width; val h = size.height
        drawRoundRect(color = Color(0xFFE0E0D8), cornerRadius = CornerRadius(h / 2), size = Size(w, h))
        drawRoundRect(color = BarYellow, cornerRadius = CornerRadius(h / 2), size = Size(w * value, h))
    }
}

@Composable
fun InsightRow(icon: androidx.compose.ui.graphics.vector.ImageVector, boldText: String, normalText: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp), verticalAlignment = Alignment.Top) {
        Icon(imageVector = icon, contentDescription = null, tint = GrayText, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append(boldText) }
                append(normalText)
            },
            fontSize = 13.sp, color = DarkText, lineHeight = 18.sp
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EnvironmentScreenPreview() {
    MaterialTheme { EnvironmentScreen() }
}
