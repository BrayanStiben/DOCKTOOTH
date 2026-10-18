package com.example.docktooth.features.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.docktooth.R
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SplashScreen(
    onLoadingComplete: () -> Unit
) {
    var progress by remember { mutableFloatStateOf(0f) }

    // Progress counting effect from 0% to 100%
    LaunchedEffect(Unit) {
        val durationMs = 2800L
        val steps = 100
        val delayPerStep = durationMs / steps

        for (i in 1..steps) {
            progress = i / 100f
            delay(delayPerStep)
        }
        delay(200L)
        onLoadingComplete()
    }

    // Continuous rotation for dental icons orbit ring
    val infiniteTransition = rememberInfiniteTransition(label = "OrbitRotation")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "angle"
    )

    // Pulse animation for central logo
    val logoScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logoScale"
    )

    // Dynamic loading text message based on progress
    val loadingStatusText = remember(progress) {
        when {
            progress < 0.30f -> "Iniciando sistema..."
            progress < 0.65f -> "Sincronizando historias clínicas..."
            progress < 0.90f -> "Cargando perfiles y agenda..."
            else -> "¡Carga completada!"
        }
    }

    // List of dental representative icons/emojis orbiting the logo
    val dentalIcons = remember {
        listOf(
            "🦷", "🪞", "🩺", "👑", "😬", "💉",
            "✨", "🪥", "🔬", "🛡️", "💎", "🦷"
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0D4B66),
                        Color(0xFF082D3E),
                        Color(0xFF041824)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Central Ring Area with Logo and Orbiting Dental Icons
            Box(
                modifier = Modifier
                    .size(290.dp),
                contentAlignment = Alignment.Center
            ) {
                // Background Concentric Aura Rings
                Box(
                    modifier = Modifier
                        .size(260.dp)
                        .clip(CircleShape)
                        .background(Color(0x1A38BDF8))
                        .border(1.dp, Color(0x3338BDF8), CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .background(Color(0x2638BDF8))
                        .border(1.dp, Color(0x5538BDF8), CircleShape)
                )

                // Central App Logo Container
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .scale(logoScale)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(3.dp, Color(0xFF38BDF8), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo Doc-Tooth",
                        modifier = Modifier
                            .size(105.dp)
                            .padding(6.dp)
                    )
                }

                // Orbiting Dental Icons along a circle radius R = 115 dp
                val radiusPx = 115f
                val count = dentalIcons.size

                dentalIcons.forEachIndexed { index, iconSymbol ->
                    val angleDeg = rotationAngle + (index * (360f / count))
                    val angleRad = Math.toRadians(angleDeg.toDouble())
                    val xOffset = (radiusPx * cos(angleRad)).toFloat()
                    val yOffset = (radiusPx * sin(angleRad)).toFloat()

                    Box(
                        modifier = Modifier
                            .offset { IntOffset(xOffset.toInt(), yOffset.toInt()) }
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color(0x33FFFFFF))
                            .border(1.dp, Color(0x66E2F1F8), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = iconSymbol,
                            fontSize = 16.sp,
                            modifier = Modifier.rotate(-rotationAngle) // keep upright
                        )
                    }
                }
            }

            // Title & Subtitle
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 20.dp)
            ) {
                Text(
                    text = "Doc-Tooth",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Gestión Odontológica Integral",
                    fontSize = 14.sp,
                    color = Color(0xFF93C5FD),
                    fontWeight = FontWeight.Medium
                )
            }

            // Bottom Progress Loading Indicator
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier.size(50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = { progress },
                        color = Color(0xFF38BDF8),
                        trackColor = Color(0x33FFFFFF),
                        strokeWidth = 3.dp,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = loadingStatusText,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${(progress * 100).toInt()}%...",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF38BDF8)
                )

                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = "Tu sonrisa, nuestra prioridad",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}
