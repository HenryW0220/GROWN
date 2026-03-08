# 🌱 GROWN

A smart plant care companion app built with **Kotlin** and **Jetpack Compose** that helps you monitor and manage your plants' health using real-time environmental data and AI-powered care recommendations.

## Features

### 🏠 Home Dashboard
- View your plant shelf at a glance
- Quick access to alerts, recent activity, and care guides
- Trend visualization for plant health over time

### 🌿 Plant Dashboards
Each plant has a dedicated dashboard with real-time metrics:
- **Plant Health Score** — circular progress indicator showing overall plant condition
- **Soil Moisture** — current moisture levels with optimal range tracking
- **Light Level** — ambient light exposure monitoring
- **CO₂ Levels** — atmospheric carbon dioxide concentration
- **Humidity** — environmental humidity tracking

Supports three lighting environments:
- ☀️ UV Light
- 🌤️ Natural Light
- 🌑 No Light

### 📊 Detailed Metric Reports
Tap any metric card to view:
- Trend charts with historical data
- Current trends analysis with detailed insights
- Reminders and recommended actions

### 🌡️ Environment Monitor
- Soil moisture with threshold indicators
- Light exposure charts
- Leaf temperature with gradient visualization
- Humidity tracking with progress bars
- AI-generated insights on plant stress factors

### 🤖 AI Care Guide
- Add a plant by name, age, and nickname
- Get personalized care recommendations powered by **Google Gemini API**
- Structured advice covering watering, sunlight, and soil needs

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin |
| UI Framework | Jetpack Compose + Material 3 |
| AI Backend | Google Gemini API (REST) |
| Architecture | Single-activity, state-driven navigation |
| Min SDK | 21 (Android 5.0) |
| Target SDK | 35 |
