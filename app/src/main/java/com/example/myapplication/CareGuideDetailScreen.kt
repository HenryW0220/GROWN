package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.PageBackground
import java.text.SimpleDateFormat
import java.util.*

/**
 * Screen that displays a previously saved care guide from the database.
 * Reads the guide ID from PlantDataHolder.selectedGuideId.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareGuideDetailScreen(onNavigate: (String) -> Unit = {}) {
    val context = LocalContext.current
    val db = remember { PlantCareDatabase.getDatabase(context) }
    val guideId = PlantDataHolder.selectedGuideId

    var guide by remember { mutableStateOf<CareGuideEntity?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    val greenPrimary = Color(0xFF4CAF50)
    val cardContentBg = Color(0xFFE8F5E9)

    // Load the guide from database
    LaunchedEffect(guideId) {
        guide = db.careGuideDao().getGuideById(guideId)
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Care Guide") },
                navigationIcon = {
                    IconButton(onClick = { onNavigate(Routes.DASHBOARD) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PageBackground
                )
            )
        },
        containerColor = PageBackground
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = greenPrimary)
            }
        } else if (guide == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Care guide not found", color = Color.Gray)
            }
        } else {
            val g = guide!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Plant Profile Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = cardContentBg)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Eco,
                            contentDescription = null,
                            tint = greenPrimary,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (g.plantNickname.isNotBlank()) "${g.plantNickname} (${g.plantName})" else g.plantName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            if (g.plantAge.isNotBlank()) {
                                Text(text = "Age: ${g.plantAge}", fontSize = 14.sp, color = Color.Gray)
                            }
                            // Show creation date
                            val dateFormat = SimpleDateFormat("MMM dd, yyyy  HH:mm", Locale.getDefault())
                            Text(
                                text = "Saved: ${dateFormat.format(Date(g.createdAt))}",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Full AI Advice Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color(0xFFFFB300)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "AI Recommendations",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = g.aiAdvice,
                            fontSize = 15.sp,
                            lineHeight = 24.sp,
                            color = Color(0xFF2D3436)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedButton(
                    onClick = { onNavigate(Routes.DASHBOARD) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Back to Dashboard")
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CareGuideDetailScreenPreview() {
    MaterialTheme {
        CareGuideDetailScreen()
    }
}
