package io.github.akiomik.seiun.model.app.bsky.graph

import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class Follow(
    val subject: String,
    val createdAt: Date
)
