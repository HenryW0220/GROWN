package com.example.myapplication

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

private const val TAG = "PLANT_IMAGE"

/**
 * Generates a plant image using Gemini API and saves it locally.
 * Returns the local file path, or null if generation fails.
 */
suspend fun generatePlantImage(plantName: String, context: Context): String? {
    return withContext(Dispatchers.IO) {
        try {
            val apiKey = BuildConfig.GEMINI_API_KEY
            if (apiKey.isBlank()) {
                Log.e(TAG, "GEMINI_API_KEY is blank! Check local.properties and build.gradle.kts")
                return@withContext null
            }

            // gemini-2.5-flash-image supports image generation
            val apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-image:generateContent?key=$apiKey"

            val promptText = "Generate a beautiful botanical illustration of a $plantName plant " +
                    "in a cute terrarium style, clean white background, digital art style, high quality, no text."

            // Build request JSON
            val partObj = JSONObject().put("text", promptText)
            val partsArr = JSONArray().put(partObj)
            val contentObj = JSONObject().put("parts", partsArr)
            val contentsArr = JSONArray().put(contentObj)

            // Request image output
            val genConfig = JSONObject()
            genConfig.put("responseModalities", JSONArray().put("TEXT").put("IMAGE"))

            val jsonBody = JSONObject()
            jsonBody.put("contents", contentsArr)
            jsonBody.put("generationConfig", genConfig)

            Log.d(TAG, "Requesting image for: $plantName")

            val url = URL(apiUrl)
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true
            conn.connectTimeout = 30000
            conn.readTimeout = 60000

            val writer = OutputStreamWriter(conn.outputStream, "UTF-8")
            writer.write(jsonBody.toString())
            writer.flush()
            writer.close()

            val responseCode = conn.responseCode
            Log.d(TAG, "Response code: $responseCode")

            if (responseCode == 200) {
                val responseText = conn.inputStream.bufferedReader().readText()
                Log.d(TAG, "Response length: ${responseText.length}")

                val root = JSONObject(responseText)
                val candidates = root.getJSONArray("candidates")
                val content = candidates.getJSONObject(0).getJSONObject("content")
                val parts = content.getJSONArray("parts")

                for (i in 0 until parts.length()) {
                    val part = parts.getJSONObject(i)
                    Log.d(TAG, "Part $i keys: ${part.keys().asSequence().toList()}")

                    if (part.has("inlineData")) {
                        val inlineData = part.getJSONObject("inlineData")
                        val mimeType = inlineData.optString("mimeType", "unknown")
                        val base64Data = inlineData.getString("data")
                        Log.d(TAG, "Found image! mimeType=$mimeType, dataLength=${base64Data.length}")

                        val imageBytes = Base64.decode(base64Data, Base64.DEFAULT)
                        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

                        if (bitmap == null) {
                            Log.e(TAG, "Failed to decode bitmap")
                            conn.disconnect()
                            return@withContext null
                        }

                        val safeFileName = plantName.replace(Regex("[^a-zA-Z0-9]"), "_").lowercase()
                        val fileName = "plant_${safeFileName}_${System.currentTimeMillis()}.png"
                        val file = File(context.filesDir, fileName)
                        FileOutputStream(file).use { fos ->
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                        }

                        Log.d(TAG, "Image saved: ${file.absolutePath}")
                        conn.disconnect()
                        return@withContext file.absolutePath
                    }
                }

                Log.e(TAG, "No inlineData in ${parts.length()} parts")
                for (i in 0 until parts.length()) {
                    val part = parts.getJSONObject(i)
                    if (part.has("text")) {
                        Log.d(TAG, "Got text instead: ${part.getString("text").take(200)}")
                    }
                }
                conn.disconnect()
                return@withContext null

            } else {
                val errorText = conn.errorStream?.bufferedReader()?.readText() ?: "No error message"
                Log.e(TAG, "API Error $responseCode: ${errorText.take(500)}")
                conn.disconnect()
                return@withContext null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception: ${e.message}", e)
            return@withContext null
        }
    }
}
