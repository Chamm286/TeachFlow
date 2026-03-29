package com.example.teachflow.ui.theme

import androidx.compose.ui.graphics.Color

// ============ MÀU CHỦ ĐẠO ============
// Màu chính - Xanh Indigo hiện đại
val Primary = Color(0xFF4F46E5)      // Indigo 600
val PrimaryDark = Color(0xFF4338CA)  // Indigo 700  
val PrimaryLight = Color(0xFF818CF8) // Indigo 400

// Màu phụ - Xanh Teal tươi mát
val Secondary = Color(0xFF0D9488)    // Teal 600
val SecondaryDark = Color(0xFF0F766E) // Teal 700
val SecondaryLight = Color(0xFF14B8A6) // Teal 500

// ============ MÀU NỀN ============
val BackgroundLight = Color(0xFFF8FAFC)  // Slate 50
val BackgroundCard = Color(0xFFFFFFFF)   // White
val Surface = Color(0xFFFFFFFF)

// ============ MÀU CHỮ ============
val TextPrimary = Color(0xFF0F172A)   // Slate 900
val TextSecondary = Color(0xFF475569) // Slate 600
val TextHint = Color(0xFF94A3B8)      // Slate 400
val TextDisabled = Color(0xFFCBD5E1)  // Slate 300

// ============ MÀU TRẠNG THÁI ============
val Success = Color(0xFF10B981)  // Emerald 500
val Error = Color(0xFFEF4444)    // Red 500
val Warning = Color(0xFFF59E0B)  // Amber 500
val Info = Color(0xFF3B82F6)     // Blue 500

// ============ GRADIENT ============
val PrimaryGradient = listOf(Primary, PrimaryLight)
val SecondaryGradient = listOf(Secondary, SecondaryLight)
val DarkGradient = listOf(PrimaryDark, SecondaryDark)

// ============ ALIAS ============
val Background = BackgroundLight
