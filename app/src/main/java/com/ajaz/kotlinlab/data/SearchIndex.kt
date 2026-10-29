package com.ajaz.kotlinlab.data

import com.ajaz.kotlinlab.data.storage.ContentKind

data class SearchHit(
    val kind: ContentKind,
    val id: String,
    val title: String,
    val subtitle: String,
    val routeHint: String
)

object SearchIndex {
    fun all(): List<SearchHit> {
        val modules = ModuleCatalog.all.map {
            SearchHit(
                kind = ContentKind.MODULE,
                id = it.id,
                title = it.title,
                subtitle = "${it.level.displayName} · ${it.shortSummary}",
                routeHint = "module/${it.id}"
            )
        }
        val language = LanguageCatalog.all.map {
            SearchHit(
                kind = ContentKind.LANGUAGE,
                id = it.id,
                title = it.title,
                subtitle = "Language · ${it.shortSummary}",
                routeHint = "language_topic/${it.id}"
            )
        }
        val routing = RoutingCatalog.all.map {
            SearchHit(
                kind = ContentKind.ROUTING,
                id = it.id,
                title = it.title,
                subtitle = "Routing · ${it.shortSummary}",
                routeHint = "routing_topic/${it.id}"
            )
        }
        return modules + language + routing
    }

    fun query(raw: String): List<SearchHit> {
        val q = raw.trim().lowercase()
        if (q.isEmpty()) return emptyList()
        return all().filter { hit ->
            hit.title.lowercase().contains(q) ||
                hit.subtitle.lowercase().contains(q) ||
                hit.id.lowercase().contains(q)
        }
    }
}
