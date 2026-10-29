package com.ajaz.kotlinlab.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ajaz.kotlinlab.ui.theme.Amber500
import com.ajaz.kotlinlab.ui.theme.Sky500
import com.ajaz.kotlinlab.ui.theme.Slate900
import com.ajaz.kotlinlab.ui.theme.Teal500

@Composable
fun HeroHeader(
    title: String,
    subtitle: String,
    trailing: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
            .background(
                Brush.linearGradient(
                    listOf(Slate900, Color(0xFF134E4A), Color(0xFF0C4A6E))
                )
            )
            .padding(horizontal = 20.dp, vertical = 28.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "KOTLIN COMPOSE LAB",
                        style = MaterialTheme.typography.labelLarge,
                        color = Teal500,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.displayMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                trailing?.invoke()
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.82f)
            )
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SoftAccentChip(text = "Compose", accent = Teal500, soft = Color.White.copy(alpha = 0.12f))
                SoftAccentChip(text = "Coroutines", accent = Sky500, soft = Color.White.copy(alpha = 0.12f))
                SoftAccentChip(text = "Motion", accent = Amber500, soft = Color.White.copy(alpha = 0.12f))
            }
        }
    }
}
