package com.kitelytech.castlelink.data.local.api.model.product

class Option(
    val uid: String,
    val idx: Int?,
    val def: Int?,
    val value: Int?,
    val ttl: Int?,
    val hlp: Int?,
    val nmTtl: Double?,
    val dis: Int?,
    val dsblsList: List<Dsbls>
) {
}