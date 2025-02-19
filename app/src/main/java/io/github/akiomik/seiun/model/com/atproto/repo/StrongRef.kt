package io.github.akiomik.seiun.model.com.atproto.repo

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StrongRef(
    val uri: String,
    val cid: String
)
