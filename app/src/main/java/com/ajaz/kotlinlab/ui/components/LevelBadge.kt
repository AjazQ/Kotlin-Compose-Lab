package com.ajaz.kotlinlab.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ajaz.kotlinlab.data.SkillLevel
import com.ajaz.kotlinlab.ui.theme.accent
import com.ajaz.kotlinlab.ui.theme.accentSoft

@Composable
fun LevelBadge(
    level: SkillLevel,
    modifier: Modifier = Modifier
) {
    Text(
        text = level.displayName.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = level.accent(),
        modifier = modifier
            .clip(RoundedCornerShape(999.dp))
            .background(level.accentSoft())
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}

@Composable
fun SoftAccentChip(
    text: String,
    accent: Color,
    soft: Color,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        color = accent,
        modifier = modifier
            .clip(RoundedCornerShape(999.dp))
            .background(soft)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}
