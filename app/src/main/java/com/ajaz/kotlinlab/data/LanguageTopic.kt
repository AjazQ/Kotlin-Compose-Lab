package com.ajaz.kotlinlab.data

data class LanguageTopic(
    val id: String,
    val title: String,
    val shortSummary: String,
    val overview: String,
    val keyPoints: List<String>,
    val sampleCode: String,
    val relatedApis: List<String>
)
