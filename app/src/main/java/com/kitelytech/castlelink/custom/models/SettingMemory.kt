package com.kitelytech.castlelink.custom.models

class SettingMemory {

    var bytes: MutableList<UInt> = mutableListOf()

    fun initSize(size: Int) : SettingMemory = apply {
        // safety check
        if (size > 0) {
            this.bytes = mutableListOf()
            for (i in 0 until size) {
                this.bytes.add(0u)
            }
        }
    }

    fun initMemory(string: String) : SettingMemory = apply {
        // safety check
        if (string.isNotEmpty() && ((string.count() % 2) == 0)) {
            this.bytes = mutableListOf()
            // parse the hex string
            val byteCount = (string.count() shr 1)
            var byteStart = string.indexOf(string)

            for (i in 0 until byteCount) {
                val byteEnd = byteStart + 1
                val byteString = string.substring(byteStart, byteEnd)

                // add the byte
                val byte = byteString.toUInt(16)
                this.bytes.add(byte)
                byteStart = byteEnd
            }
        }
    }

    fun hexString() : String {
        return bytes.joinToString("") { "%02x".format(it.toByte()) }
    }
}