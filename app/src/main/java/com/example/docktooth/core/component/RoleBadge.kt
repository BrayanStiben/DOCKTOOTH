package com.example.docktooth.core.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.docktooth.domain.model.UserRole

@Composable
fun RoleBadge(
    currentRole: UserRole,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (currentRole) {
        UserRole.ODONTOLOGO -> Color(0xFF1E3A8A) to Color.White
        UserRole.ESPECIALISTA -> Color(0xFF4338CA) to Color.White
        UserRole.RECEPCIONISTA -> Color(0xFF0F766E) to Color.White
        UserRole.PACIENTE -> Color(0xFFB45309) to Color.White
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = currentRole.label,
                color = textColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
