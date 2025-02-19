package io.github.akiomik.seiun.model.app.bsky.feed

import com.squareup.moshi.JsonClass
import io.github.akiomik.seiun.model.com.atproto.repo.StrongRef
import java.util.*

@JsonClass(generateAdapter = true)
data class Like(
    val subject: StrongRef,
    val createdAt: Date
)
