package com.ajaz.kotlinlab.ui.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ajaz.kotlinlab.data.LanguageCatalog
import com.ajaz.kotlinlab.data.ModuleCatalog
import com.ajaz.kotlinlab.data.RoutingCatalog
import com.ajaz.kotlinlab.data.SearchHit
import com.ajaz.kotlinlab.data.storage.ContentKind
import com.ajaz.kotlinlab.data.storage.ProgressSnapshot

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    progress: ProgressSnapshot,
    onBack: () -> Unit,
    onOpenHit: (SearchHit) -> Unit
) {
    val hits = progress.favorites.mapNotNull { token ->
        val parts = token.split(":", limit = 2)
        if (parts.size != 2) return@mapNotNull null
        val kind = runCatching { ContentKind.valueOf(parts[0]) }.getOrNull() ?: return@mapNotNull null
        val id = parts[1]
        when (kind) {
            ContentKind.MODULE -> ModuleCatalog.byId(id)?.let {
                SearchHit(kind, id, it.title, it.shortSummary, "module/$id")
            }
            ContentKind.LANGUAGE -> LanguageCatalog.byId(id)?.let {
                SearchHit(kind, id, it.title, it.shortSummary, "language_topic/$id")
            }
            ContentKind.ROUTING -> RoutingCatalog.byId(id)?.let {
                SearchHit(kind, id, it.title, it.shortSummary, "routing_topic/$id")
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favorites", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (hits.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Outlined.FavoriteBorder, contentDescription = null)
                Text("No favorites yet", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(
                    "Open any module or lecture and tap the heart to save it here.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(hits, key = { "${it.kind}:${it.id}" }) { hit ->
                    ListItem(
                        headlineContent = { Text(hit.title, fontWeight = FontWeight.SemiBold) },
                        supportingContent = { Text(hit.subtitle) },
                        overlineContent = { Text(hit.kind.name) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenHit(hit) }
                    )
                }
            }
        }
    }
}
