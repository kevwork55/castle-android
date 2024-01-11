package com.kitelytech.castlelink.data.local.api.model.product

class ThrottleResponse(
    val settingUid: String,
    val type: Int?,
    val idx: Int?,
    val ttl: Int?,
    val help: Int?,
    val add: Int?,
    val dsz: Int?,
    val dataType: Int?,
    val memArea: Int?,
    val min: Int?,
    val max: Int?,
    val inc: Int?,
    val opts: List<Option>,
) {
}