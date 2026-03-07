package com.example.myapplication

import android.util.Log
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

/**
 * Screen that shows AI-generated plant care advice.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantCareResultScreen(onNavigate: (String) -> Unit = {}) {
    val name = PlantDataHolder.plantName
    val age = PlantDataHolder.plantAge
    val nickname = PlantDataHolder.plantNickname

    var aiAdviceText by remember { mutableStateOf("") }
    var isLoadingData by remember { mutableStateOf(true) }
    var errorLabelText by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

    // UI Style Config
    val screenBg = Color(0xFFF5F7F5)
    val cardContentBg = Color(0xFFE8F5E9)
    val greenPrimary = Color(0xFF4CAF50)

    LaunchedEffect(Unit) {
        try {
            val response = requestGeminiAdvice(name, age, nickname)
            aiAdviceText = response
            isLoadingData = false
        } catch (e: Exception) {
            errorLabelText = e.message
            isLoadingData = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expert Care Guide") },
                navigationIcon = {
                    IconButton(onClick = { onNavigate(Routes.DASHBOARD) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        containerColor = screenBg
    ) { padding ->
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
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Eco, contentDescription = null, tint = greenPrimary, modifier = Modifier.size(40.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (nickname.isNotBlank()) "$nickname ($name)" else name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        if (age.isNotBlank()) {
                            Text(text = "Age: $age", fontSize = 14.sp, color = Color.Gray)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Advice Content Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color(0xFFFFB300))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("AI Recommendations", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (isLoadingData) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = greenPrimary)
                        }
                    } else if (errorLabelText != null) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "Connection failed: $errorLabelText",
                                color = Color.Red,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = {
                                isLoadingData = true
                                errorLabelText = null
                                coroutineScope.launch {
                                    try {
                                        aiAdviceText = requestGeminiAdvice(name, age, nickname)
                                        isLoadingData = false
                                    } catch (e: Exception) {
                                        errorLabelText = e.message
                                        isLoadingData = false
                                    }
                                }
                            }) {
                                Text("Try Again")
                            }
                        }
                    } else {
                        Text(
                            text = aiAdviceText,
                            fontSize = 15.sp,
                            lineHeight = 24.sp,
                            color = Color(0xFF2D3436)
                        )
                    }
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

/**
 * Direct HTTP connection to Google Gemini API.
 * API key is read from BuildConfig (sourced from local.properties).
 */
suspend fun requestGeminiAdvice(pName: String, pAge: String, pNick: String): String {
    return withContext(Dispatchers.IO) {
        // Build a smart prompt that skips empty optional fields
        val agePart = if (pAge.isNotBlank()) ", approximately $pAge old" else ""
        val nickPart = if (pNick.isNotBlank()) " (nicknamed $pNick)" else ""
        val promptText = "Act as a professional gardener. Give me short care tips for a $pName$nickPart$agePart. " +
                "Use sections: Water, Sunlight, Soil. Use bullet points and clear headings."

        // Read API key from BuildConfig (set via local.properties + build.gradle.kts)
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank()) {
            throw Exception("API key not configured. Add GEMINI_API_KEY to local.properties")
        }

        val apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey"

        val url = URL(apiUrl)
        val conn = url.openConnection() as HttpURLConnection

        try {
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true
            conn.connectTimeout = 15000
            conn.readTimeout = 15000

            // Build JSON body
            val partObj = JSONObject().put("text", promptText)
            val partsArr = JSONArray().put(partObj)
            val contentObj = JSONObject().put("parts", partsArr)
            val contentsArr = JSONArray().put(contentObj)
            val jsonBody = JSONObject().put("contents", contentsArr)

            Log.d("GEMINI_DEBUG", "Request body: ${jsonBody.toString()}")

            // Write request
            val writer = OutputStreamWriter(conn.outputStream, "UTF-8")
            writer.write(jsonBody.toString())
            writer.flush()
            writer.close()

            val responseCode = conn.responseCode
            Log.d("GEMINI_DEBUG", "Response code: $responseCode")

            if (responseCode == 200) {
                val responseText = conn.inputStream.bufferedReader().readText()
                val root = JSONObject(responseText)
                val candidates = root.getJSONArray("candidates")
                val content = candidates.getJSONObject(0).getJSONObject("content")
                val parts = content.getJSONArray("parts")
                parts.getJSONObject(0).getString("text")
            } else {
                val errorStreamText = conn.errorStream?.bufferedReader()?.readText() ?: "No error details"
                Log.e("GEMINI_DEBUG", "Error $responseCode: $errorStreamText")
                throw Exception("API Error $responseCode")
            }
        } catch (e: Exception) {
            Log.e("GEMINI_DEBUG", "Exception: ${e.message}", e)
            throw Exception(e.message ?: "Failed to reach AI service")
        } finally {
            conn.disconnect()
        }
    }
}