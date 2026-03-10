package com.example.myapplication

import android.util.Log
import android.widget.Toast
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantCareResultScreen(onNavigate: (String) -> Unit = {}) {
    val name = PlantDataHolder.plantName
    val age = PlantDataHolder.plantAge
    val nickname = PlantDataHolder.plantNickname

    var aiAdviceText by remember { mutableStateOf("") }
    var isLoadingData by remember { mutableStateOf(true) }
    var errorLabelText by remember { mutableStateOf<String?>(null) }
    var isAddedToShelf by remember { mutableStateOf(false) }
    var isGeneratingImage by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val db = remember { PlantCareDatabase.getDatabase(context) }

    val screenBg = Color(0xFFF5F7F5)
    val cardContentBg = Color(0xFFE8F5E9)
    val greenPrimary = Color(0xFF4CAF50)

    val alreadyOnShelf = remember {
        AppState.savedPlants.any {
            it.name.equals(name, ignoreCase = true) &&
            it.nickname.equals(nickname, ignoreCase = true)
        }
    }

    LaunchedEffect(Unit) {
        try {
            val response = requestGeminiAdvice(name, age, nickname)
            aiAdviceText = response
            isLoadingData = false
            AppState.logCareGuideSearch(name, nickname, age, response)

            // Save to Room database for Care Guide history
            db.careGuideDao().insertGuide(
                CareGuideEntity(
                    plantName = name,
                    plantAge = age,
                    plantNickname = nickname,
                    aiAdvice = response
                )
            )
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
                            fontWeight = FontWeight.Bold, fontSize = 18.sp
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
                        Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = greenPrimary)
                        }
                    } else if (errorLabelText != null) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                            Text("Connection failed: $errorLabelText", color = Color.Red, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = {
                                isLoadingData = true
                                errorLabelText = null
                                coroutineScope.launch {
                                    try {
                                        val response = requestGeminiAdvice(name, age, nickname)
                                        aiAdviceText = response
                                        isLoadingData = false
                                        AppState.logCareGuideSearch(name, nickname, age, response)

                                        // Save to Room database on retry
                                        db.careGuideDao().insertGuide(
                                            CareGuideEntity(
                                                plantName = name,
                                                plantAge = age,
                                                plantNickname = nickname,
                                                aiAdvice = response
                                            )
                                        )
                                    } catch (e: Exception) {
                                        errorLabelText = e.message
                                        isLoadingData = false
                                    }
                                }
                            }) { Text("Try Again") }
                        }
                    } else {
                        Text(
                            text = aiAdviceText,
                            fontSize = 15.sp, lineHeight = 24.sp,
                            color = Color(0xFF2D3436)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Add to Plant Shelf button
            if (!isLoadingData && errorLabelText == null) {
                if (isAddedToShelf || alreadyOnShelf) {
                    OutlinedButton(
                        onClick = { },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        enabled = false
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (isGeneratingImage) "Generating image..." else "Added to Plant Shelf", fontSize = 16.sp)
                    }
                } else {
                    Button(
                        onClick = {
                            AppState.addPlantToShelf(name, nickname, age, aiAdviceText)
                            isAddedToShelf = true

                            // Generate image in background
                            isGeneratingImage = true
                            coroutineScope.launch {
                                val imagePath = generatePlantImage(name.trim(), context)
                                if (imagePath != null) {
                                    PlantImageCache.saveImagePath(name, imagePath)
                                    Toast.makeText(context, "Plant image generated!", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Image generation failed (check Logcat: PLANT_IMAGE)", Toast.LENGTH_LONG).show()
                                }
                                isGeneratingImage = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = greenPrimary)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add to Plant Shelf", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = { onNavigate(Routes.DASHBOARD) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back to Dashboard")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

suspend fun requestGeminiAdvice(pName: String, pAge: String, pNick: String): String {
    return withContext(Dispatchers.IO) {
        val agePart = if (pAge.isNotBlank()) ", approximately $pAge old" else ""
        val nickPart = if (pNick.isNotBlank()) " (nicknamed $pNick)" else ""
        val promptText = "Act as a professional gardener. Give me short care tips for a $pName$nickPart$agePart. " +
                "Use sections: Water, Sunlight, Soil. Use bullet points and clear headings."

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

            val partObj = JSONObject().put("text", promptText)
            val partsArr = JSONArray().put(partObj)
            val contentObj = JSONObject().put("parts", partsArr)
            val contentsArr = JSONArray().put(contentObj)
            val jsonBody = JSONObject().put("contents", contentsArr)

            val writer = OutputStreamWriter(conn.outputStream, "UTF-8")
            writer.write(jsonBody.toString())
            writer.flush()
            writer.close()

            val responseCode = conn.responseCode
            if (responseCode == 200) {
                val responseText = conn.inputStream.bufferedReader().readText()
                val root = JSONObject(responseText)
                val candidates = root.getJSONArray("candidates")
                val content = candidates.getJSONObject(0).getJSONObject("content")
                val parts = content.getJSONArray("parts")
                parts.getJSONObject(0).getString("text")
            } else {
                val errorStreamText = conn.errorStream?.bufferedReader()?.readText() ?: "No details"
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

suspend fun requestGeminiCustomPrompt(promptText: String): String {
    return withContext(Dispatchers.IO) {
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

            val reqJson = JSONObject().apply {
                put("contents", JSONArray().put(JSONObject().apply {
                    put("parts", JSONArray().put(JSONObject().apply {
                        put("text", promptText)
                    }))
                }))
            }

            OutputStreamWriter(conn.outputStream).use { it.write(reqJson.toString()) }

            val responseCode = conn.responseCode
            val stream = if (responseCode in 200..299) conn.inputStream else conn.errorStream
            val responseText = stream.bufferedReader().use { it.readText() }

            if (responseCode !in 200..299) {
                throw Exception("HTTP $responseCode: $responseText")
            }

            val root = JSONObject(responseText)
            val candidates = root.optJSONArray("candidates")
            if (candidates == null || candidates.length() == 0) {
                throw Exception("No response from AI")
            }

            val text = candidates.getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text")

            text
        } catch (e: Exception) {
            Log.e("GEMINI_API", "requestGeminiCustomPrompt failed", e)
            throw e
        } finally {
            conn.disconnect()
        }
    }
}
