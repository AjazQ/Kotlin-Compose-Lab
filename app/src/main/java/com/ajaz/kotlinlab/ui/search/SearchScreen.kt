package com.ajaz.kotlinlab.ui.search

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
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ajaz.kotlinlab.data.SearchHit
import com.ajaz.kotlinlab.data.SearchIndex
import com.ajaz.kotlinlab.data.storage.ContentKind

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBack: () -> Unit,
    onOpenHit: (SearchHit) -> Unit
) {
    var query by rememberSaveable { mutableStateOf("") }
    val results = SearchIndex.query(query)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                placeholder = { Text("Modules, language, routing…") },
                singleLine = true
            )
            LazyColumn(
                contentPadding = PaddingValues(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (query.isBlank()) {
                    item {
                        Text(
                            "Try “null”, “NavHost”, “Room”, or “coroutines”.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                } else if (results.isEmpty()) {
                    item {
                        Text(
                            "No matches for \"$query\"",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                } else {
                    items(results, key = { "${it.kind}:${it.id}" }) { hit ->
                        ListItem(
                            headlineContent = { Text(hit.title, fontWeight = FontWeight.SemiBold) },
                            supportingContent = { Text(hit.subtitle) },
                            overlineContent = {
                                Text(
                                    when (hit.kind) {
                                        ContentKind.MODULE -> "MODULE"
                                        ContentKind.LANGUAGE -> "LANGUAGE"
                                        ContentKind.ROUTING -> "ROUTING"
                                    }
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onOpenHit(hit) }
                        )
                    }
                }
            }
        }
    }
}
