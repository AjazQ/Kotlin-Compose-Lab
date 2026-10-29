package com.ajaz.kotlinlab.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Brush
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.DashboardCustomize
import androidx.compose.material.icons.outlined.DataObject
import androidx.compose.material.icons.outlined.DeviceHub
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Hub
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Rule
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material.icons.outlined.TouchApp
import androidx.compose.material.icons.outlined.Widgets
import androidx.compose.material.icons.outlined.AccountTree
import androidx.compose.material.icons.outlined.Animation
import androidx.compose.material.icons.outlined.AltRoute
import androidx.compose.material.icons.outlined.LeakAdd
import androidx.compose.material.icons.outlined.VerticalAlignTop
import androidx.compose.material.icons.outlined.ViewQuilt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.background
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ajaz.kotlinlab.data.LearningModule
import com.ajaz.kotlinlab.ui.theme.MotionTokens
import com.ajaz.kotlinlab.ui.theme.accent
import com.ajaz.kotlinlab.ui.theme.accentSoft

@Composable
fun ModuleCard(
    module: LearningModule,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.98f else 1f,
        animationSpec = MotionTokens.shortTween(),
        label = "moduleCardScale"
    )
    val accent = module.level.accent()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(interactionSource = interaction, indication = null, onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(module.level.accentSoft()),
                contentAlignment = Alignment.Center
            ) {
                Icon(iconFor(module.iconHint), contentDescription = null, tint = accent)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(module.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(
                    module.shortSummary,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, contentDescription = null, tint = accent)
        }
    }
}

private fun iconFor(hint: String): ImageVector = when (hint) {
    "widgets" -> Icons.Outlined.Widgets
    "view_quilt" -> Icons.Outlined.ViewQuilt
    "sync" -> Icons.Outlined.Sync
    "edit" -> Icons.Outlined.Edit
    "list" -> Icons.Outlined.List
    "palette" -> Icons.Outlined.Palette
    "vertical_align_top" -> Icons.Outlined.VerticalAlignTop
    "account_tree" -> Icons.Outlined.AccountTree
    "bolt" -> Icons.Outlined.Bolt
    "hub" -> Icons.Outlined.Hub
    "leak_add" -> Icons.Outlined.LeakAdd
    "dashboard_customize" -> Icons.Outlined.DashboardCustomize
    "alt_route" -> Icons.Outlined.AltRoute
    "animation" -> Icons.Outlined.Animation
    "sports_esports" -> Icons.Outlined.SportsEsports
    "cloud" -> Icons.Outlined.Cloud
    "storage" -> Icons.Outlined.Storage
    "database" -> Icons.Outlined.DataObject
    "device_hub" -> Icons.Outlined.DeviceHub
    "speed" -> Icons.Outlined.Speed
    "brush" -> Icons.Outlined.Brush
    "touch_app" -> Icons.Outlined.TouchApp
    "rule" -> Icons.Outlined.Rule
    "auto_awesome" -> Icons.Outlined.AutoAwesome
    else -> Icons.Outlined.Widgets
}
