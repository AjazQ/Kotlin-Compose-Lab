package com.ajaz.kotlinlab.data

data class LearningModule(
    val id: String,
    val title: String,
    val level: SkillLevel,
    val shortSummary: String,
    val overview: String,
    val steps: List<String>,
    val keyApis: List<String>,
    val tryThis: List<String>,
    val iconHint: String,
    val sampleCode: String = ModuleCodeSamples.forId(id)
)
